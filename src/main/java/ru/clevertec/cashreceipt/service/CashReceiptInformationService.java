package ru.clevertec.cashreceipt.service;

import ru.clevertec.cashreceipt.dto.ProductDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public interface CashReceiptInformationService {

    StringBuilder createCashReceiptHeader(LocalDate date, LocalTime time);

    StringBuilder createCashReceiptBody(ProductDto productDto);

    StringBuilder createCashReceiptResults(BigDecimal totalSum,
                                           BigDecimal discountCardPercentage,
                                           BigDecimal discount,
                                           StringBuilder promoDiscBuilder,
                                           BigDecimal totalSumWithDiscount);

    StringBuilder createCashReceiptPromoDiscount(String productName, BigDecimal promotionDiscount);

}
