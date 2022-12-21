package by.grigoryev.cashreceipt.service;

import by.grigoryev.cashreceipt.dto.ProductDto;

import java.math.BigDecimal;

public interface CashReceiptInformationService {

    StringBuilder createCashReceiptHeader();

    StringBuilder createCashReceiptBody(ProductDto productDto);

    StringBuilder createCashReceiptResults(BigDecimal totalSum,
                                           BigDecimal discountCardPercentage,
                                           BigDecimal discount,
                                           StringBuilder promoDiscBuilder,
                                           BigDecimal totalSumWithDiscount);

    StringBuilder createCashReceiptPromoDiscount(String productName, BigDecimal promotionDiscount);

}
