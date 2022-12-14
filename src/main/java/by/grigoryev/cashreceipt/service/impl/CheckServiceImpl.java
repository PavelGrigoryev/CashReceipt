package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.model.DiscountCard;
import by.grigoryev.cashreceipt.model.Product;
import by.grigoryev.cashreceipt.service.DiscountCardService;
import by.grigoryev.cashreceipt.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
public class CheckServiceImpl {

    private final ProductService productService;

    private final DiscountCardService discountCardService;

    public String createCheck(String idAndQuantity, String discountCardNumber) {
        List<Product> products = new ArrayList<>();
        BigDecimal totalSum = new BigDecimal("0");

        String[] splitSpace = idAndQuantity.split(" ");

        for (String string : splitSpace) {
            String[] splitHyphen = string.split("-");
            String id = splitHyphen[0];
            String quantity = splitHyphen[1];

            Product product = productService.update(Long.valueOf(id), Integer.valueOf(quantity));

            products.add(product);
            totalSum = totalSum.add(product.getTotal()).stripTrailingZeros();
        }

        DiscountCard discountCard = discountCardService.findByDiscountCardNumber(discountCardNumber);
        BigDecimal totalSumWithDiscount;
        BigDecimal discount;

        discount = totalSum.divide(BigDecimal.valueOf(100), 4, RoundingMode.UP)
                .multiply(discountCard.getDiscountPercentage());
        totalSumWithDiscount = totalSum.subtract(discount);

        StringBuilder checkBuilder = new StringBuilder();
        StringBuilder promoDiscBuilder = new StringBuilder();

        checkBuilder.append("\n")
                .append("Cash Receipt")
                .append("\n")
                .append("DATE: ")
                .append(LocalDate.now())
                .append(" ")
                .append("TIME: ")
                .append(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .append("\n")
                .append("-".repeat(50))
                .append("\n")
                .append("QTY  DESCRIPTION     PRICE   TOTAL")
                .append("\n");

        for (Product product : products) {
            checkBuilder.append(product.getQuantity())
                    .append("   ")
                    .append(product.getName())
                    .append("   ")
                    .append(product.getPrice())
                    .append("   ")
                    .append(product.getTotal())
                    .append("\n");

            if (Boolean.TRUE.equals(product.getPromotion()) && product.getQuantity() > 5) {
                BigDecimal promotionDiscount = product.getTotal()
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.UP)
                        .multiply(BigDecimal.valueOf(10)).stripTrailingZeros();

                totalSumWithDiscount = totalSumWithDiscount.subtract(promotionDiscount);

                promoDiscBuilder.append("PromoDiscount '-10%' for promotional products\n")
                        .append("with name \"")
                        .append(product.getName())
                        .append("\"")
                        .append(" more than 5 items: -")
                        .append(promotionDiscount)
                        .append("\n");
            }

        }

        checkBuilder.append("=".repeat(50))
                .append("\n")
                .append("TOTAL: ")
                .append(totalSum)
                .append("\n")
                .append("DiscountCard '-")
                .append(discountCard.getDiscountPercentage())
                .append("%': -")
                .append(discount.stripTrailingZeros())
                .append("\n")
                .append(promoDiscBuilder)
                .append("TOTAL PAID: ")
                .append(totalSumWithDiscount.stripTrailingZeros());

        log.info(checkBuilder.toString());

        return checkBuilder.toString();
    }

}
