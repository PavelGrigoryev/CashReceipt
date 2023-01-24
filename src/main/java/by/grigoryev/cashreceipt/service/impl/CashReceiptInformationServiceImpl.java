package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.dto.ProductDto;
import by.grigoryev.cashreceipt.service.CashReceiptInformationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class CashReceiptInformationServiceImpl implements CashReceiptInformationService {

    @Override
    public StringBuilder createCashReceiptHeader() {
        return new StringBuilder("""
                            
                Cash Receipt
                DATE: %s TIME: %s
                %s
                %-6s %-15s %6s %8s
                """.formatted(
                LocalDate.now(),
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                "-".repeat(40),
                "QTY",
                "DESCRIPTION",
                "PRICE",
                "TOTAL"
        ));
    }

    @Override
    public StringBuilder createCashReceiptBody(ProductDto productDto) {
        return new StringBuilder("""
                %-2s  | %-15s | %-6s | %s
                """.formatted(
                productDto.quantity(),
                productDto.name(),
                productDto.price(),
                productDto.total()
        ));
    }

    @Override
    public StringBuilder createCashReceiptResults(BigDecimal totalSum,
                                                  BigDecimal discountCardPercentage,
                                                  BigDecimal discount,
                                                  StringBuilder promoDiscBuilder,
                                                  BigDecimal totalSumWithDiscount) {
        return new StringBuilder("""
                %s
                TOTAL: %s
                DiscountCard -%s%s : -%s
                %sTOTAL PAID: %s
                """.formatted(
                "=".repeat(40),
                totalSum.stripTrailingZeros(),
                discountCardPercentage, "%",
                discount.stripTrailingZeros(),
                promoDiscBuilder,
                totalSumWithDiscount.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros()
        ));
    }

    @Override
    public StringBuilder createCashReceiptPromoDiscount(String productName, BigDecimal promotionDiscount) {
        return new StringBuilder("""
                PromoDiscount -10%s : "%s"
                more than 5 items: -%s
                """.formatted(
                "%",
                productName,
                promotionDiscount
        ));
    }

}
