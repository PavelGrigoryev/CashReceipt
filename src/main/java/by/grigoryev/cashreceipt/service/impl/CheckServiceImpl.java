package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.model.DiscountCard;
import by.grigoryev.cashreceipt.model.Product;
import by.grigoryev.cashreceipt.service.CheckService;
import by.grigoryev.cashreceipt.service.DiscountCardService;
import by.grigoryev.cashreceipt.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckServiceImpl implements CheckService {

    private final ProductService productService;

    private final DiscountCardService discountCardService;

    @Override
    public String createCheck(String idAndQuantity, String discountCardNumber) {
        List<Product> products = new ArrayList<>();
        BigDecimal totalSum = getTotalSum(idAndQuantity, products);

        DiscountCard discountCard = discountCardService.findByDiscountCardNumber(discountCardNumber);
        BigDecimal discount = getDiscount(totalSum, discountCard);

        StringBuilder checkBuilder = new StringBuilder().append("\n")
                .append("Cash Receipt")
                .append("\n")
                .append("DATE: ")
                .append(LocalDate.now())
                .append(" ")
                .append("TIME: ")
                .append(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .append("\n")
                .append("-".repeat(40))
                .append("\n")
                .append(String.format("%-6s %-15s %6s %8s", "QTY", "DESCRIPTION", "PRICE", "TOTAL"))
                .append("\n");

        StringBuilder promoDiscBuilder = new StringBuilder();

        BigDecimal totalSumWithDiscount = totalSum.subtract(discount);

        for (Product product : products) {
            checkBuilder.append(String.format(
                            "%s  | %-12s | %-6s | %s",
                            product.getQuantity(),
                            product.getName(),
                            product.getPrice(),
                            product.getTotal()
                    ))
                    .append("\n");

            if (Boolean.TRUE.equals(product.getPromotion()) && product.getQuantity() > 5) {
                BigDecimal promotionDiscount = product.getTotal()
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.UP)
                        .multiply(BigDecimal.valueOf(10)).stripTrailingZeros();

                totalSumWithDiscount = totalSumWithDiscount.subtract(promotionDiscount);

                promoDiscBuilder.append("PromoDiscount -10% : \"")
                        .append(product.getName())
                        .append("\"\nmore then 5 items: -")
                        .append(promotionDiscount)
                        .append("\n");
            }

        }

        checkBuilder.append("=".repeat(40))
                .append("\n")
                .append("TOTAL: ")
                .append(totalSum.stripTrailingZeros())
                .append("\n")
                .append("DiscountCard -")
                .append(discountCard.getDiscountPercentage())
                .append("% : -")
                .append(discount.stripTrailingZeros())
                .append("\n")
                .append(promoDiscBuilder)
                .append("TOTAL PAID: ")
                .append(totalSumWithDiscount.stripTrailingZeros());

        uploadFile(checkBuilder.toString());

        log.info(checkBuilder.toString());

        return checkBuilder.toString();
    }

    private BigDecimal getTotalSum(String idAndQuantity, List<Product> products) {
        String[] splitSpace = idAndQuantity.split(" ");
        BigDecimal totalSum = new BigDecimal("0");

        for (String string : splitSpace) {
            String[] splitHyphen = string.split("-");
            String id = splitHyphen[0];
            String quantity = splitHyphen[1];

            Product product = productService.update(Long.valueOf(id), Integer.valueOf(quantity));

            products.add(product);
            totalSum = totalSum.add(product.getTotal());
        }
        return totalSum;
    }

    private BigDecimal getDiscount(BigDecimal totalSum, DiscountCard discountCard) {
        return totalSum.divide(BigDecimal.valueOf(100), 4, RoundingMode.UP)
                .multiply(discountCard.getDiscountPercentage());
    }

    private void uploadFile(String check) {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "Check.txt";

        try (FileOutputStream outputStream = new FileOutputStream(fileLocation)) {
            outputStream.write(check.getBytes());
            log.info("uploadFile {}", fileLocation);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
