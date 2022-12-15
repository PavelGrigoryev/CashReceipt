package by.grigoryev.cashreceipt.service;

import by.grigoryev.cashreceipt.model.Product;

import java.math.BigDecimal;

public interface CheckInformationService {

    StringBuilder createCheckHeader();

    StringBuilder createCheckBody(Product product);

    StringBuilder createCheckResults(BigDecimal totalSum,
                                     BigDecimal discountCardPercentage,
                                     BigDecimal discount,
                                     StringBuilder promoDiscBuilder,
                                     BigDecimal totalSumWithDiscount);

    StringBuilder createCheckPromoDiscount(String productName, BigDecimal promotionDiscount);

}
