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
        return new StringBuilder().append("\n")
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
    }

    @Override
    public StringBuilder createCheckBody(Product product) {
        return new StringBuilder().append(String.format(
                        "%s  | %-12s | %-6s | %s",
                        product.getQuantity(),
                        product.getName(),
                        product.getPrice(),
                        product.getTotal()
                ))
                .append("\n");
    }

    @Override
    public StringBuilder createCheckResults(BigDecimal totalSum,
                                            BigDecimal discountCardPercentage,
                                            BigDecimal discount,
                                            StringBuilder promoDiscBuilder,
                                            BigDecimal totalSumWithDiscount) {
        return new StringBuilder().append("=".repeat(40))
                .append("\n")
                .append("TOTAL: ")
                .append(totalSum.stripTrailingZeros())
                .append("\n")
                .append("DiscountCard -")
                .append(discountCardPercentage)
                .append("% : -")
                .append(discount.stripTrailingZeros())
                .append("\n")
                .append(promoDiscBuilder)
                .append("TOTAL PAID: ")
                .append(totalSumWithDiscount.stripTrailingZeros().setScale(2, RoundingMode.UP));
    }

    @Override
    public StringBuilder createCheckPromoDiscount(String productName, BigDecimal promotionDiscount) {
        return new StringBuilder().append("PromoDiscount -10% : \"")
                .append(productName)
                .append("\"\nmore then 5 items: -")
                .append(promotionDiscount)
                .append("\n");
    }

}
