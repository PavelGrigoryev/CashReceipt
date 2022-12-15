package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.model.Product;
import by.grigoryev.cashreceipt.service.CheckInformationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class CheckInformationServiceImpl implements CheckInformationService {

    @Override
    public StringBuilder createCheckHeader() {
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
    public StringBuilder createCheckBody(Product product) {
        return new StringBuilder("""
                %s  | %-12s | %-6s | %s
                """.formatted(
                product.getQuantity(),
                product.getName(),
                product.getPrice(),
                product.getTotal()
        ));
    }

    @Override
    public StringBuilder createCheckResults(BigDecimal totalSum,
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
                totalSumWithDiscount.setScale(2, RoundingMode.UP).stripTrailingZeros()
        ));
    }

    @Override
    public StringBuilder createCheckPromoDiscount(String productName, BigDecimal promotionDiscount) {
        return new StringBuilder("""
                PromoDiscount -10%s : "%s"
                more then 5 items: -%s
                """.formatted(
                "%",
                productName,
                promotionDiscount
        ));
    }

}
