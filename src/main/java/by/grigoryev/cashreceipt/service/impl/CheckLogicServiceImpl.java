package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.model.DiscountCard;
import by.grigoryev.cashreceipt.model.Product;
import by.grigoryev.cashreceipt.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckLogicServiceImpl implements CheckLogicService {

    private final ProductService productService;

    private final DiscountCardService discountCardService;

    private final CheckInformationService checkInformationService;

    private final UploadFileService uploadFileService;

    @Override
    public String createCheck(String idAndQuantity, String discountCardNumber) {
        List<Product> products = new ArrayList<>();
        BigDecimal totalSum = getTotalSum(idAndQuantity, products);

        DiscountCard discountCard = discountCardService.findByDiscountCardNumber(discountCardNumber);
        BigDecimal discount = getDiscount(totalSum, discountCard);
        BigDecimal totalSumWithDiscount = totalSum.subtract(discount);

        StringBuilder checkBuilder = checkInformationService.createCheckHeader();
        StringBuilder promoDiscBuilder = new StringBuilder();

        totalSumWithDiscount = getTotalSumWithDiscount(products, totalSumWithDiscount, checkBuilder, promoDiscBuilder);

        checkBuilder.append(checkInformationService.createCheckResults(totalSum, discountCard.getDiscountPercentage(),
                discount, promoDiscBuilder, totalSumWithDiscount));

        uploadFileService.uploadFile(checkBuilder.toString());

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

    private BigDecimal getTotalSumWithDiscount(List<Product> products,
                                               BigDecimal totalSumWithDiscount,
                                               StringBuilder checkBuilder,
                                               StringBuilder promoDiscBuilder) {
        for (Product product : products) {
            checkBuilder.append(checkInformationService.createCheckBody(product));

            if (Boolean.TRUE.equals(product.getPromotion()) && product.getQuantity() > 5) {
                BigDecimal promotionDiscount = product.getTotal()
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.UP)
                        .multiply(BigDecimal.valueOf(10)).stripTrailingZeros();
                totalSumWithDiscount = totalSumWithDiscount.subtract(promotionDiscount);
                promoDiscBuilder
                        .append(checkInformationService.createCheckPromoDiscount(product.getName(), promotionDiscount));
            }

        }
        return totalSumWithDiscount;
    }

}
