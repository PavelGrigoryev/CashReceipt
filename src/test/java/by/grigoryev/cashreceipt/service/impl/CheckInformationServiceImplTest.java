package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.model.Product;
import by.grigoryev.cashreceipt.service.CheckInformationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

class CheckInformationServiceImplTest {

    private CheckInformationService checkInformationService;

    @BeforeEach
    void setUp() {
        checkInformationService = spy(new CheckInformationServiceImpl());
    }

    @Test
    @DisplayName("test createCheckHeader method")
    void createCheckHeader() {
        String expectedValue = """
                            
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
        );

        StringBuilder checkHeader = checkInformationService.createCheckHeader();

        assertEquals(expectedValue, checkHeader.toString());
    }

    @Test
    @DisplayName("test createCheckBody method")
    void createCheckBody() {
        Product mockedProduct = getMockedProduct();

        String expectedValue = """
                %s  | %-12s | %-6s | %s
                """.formatted(
                mockedProduct.getQuantity(),
                mockedProduct.getName(),
                mockedProduct.getPrice(),
                mockedProduct.getTotal()
        );

        StringBuilder checkBody = checkInformationService.createCheckBody(mockedProduct);

        assertEquals(expectedValue, checkBody.toString());
    }

    @Test
    @DisplayName("test createCheckResults method")
    void createCheckResults() {
        BigDecimal totalSum = new BigDecimal("165");
        BigDecimal discountCardPercentage = new BigDecimal("3");
        BigDecimal discount = new BigDecimal("10");
        StringBuilder promoDiscBuilder = new StringBuilder("Hi");
        BigDecimal totalSumWithDiscount = new BigDecimal("25");

        String expectedValue = """
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
        );

        StringBuilder checkResults = checkInformationService
                .createCheckResults(totalSum, discountCardPercentage, discount, promoDiscBuilder, totalSumWithDiscount);

        assertEquals(expectedValue, checkResults.toString());
    }

    @Test
    @DisplayName("test createCheckPromoDiscount method")
    void createCheckPromoDiscount() {
        String productName = getMockedProduct().getName();
        BigDecimal promotionDiscount = getMockedProduct().getPrice();

        String expectedValue = """
                PromoDiscount -10%s : "%s"
                more then 5 items: -%s
                """.formatted(
                "%",
                productName,
                promotionDiscount
        );

        StringBuilder checkPromoDiscount = checkInformationService
                .createCheckPromoDiscount(productName, promotionDiscount);

        assertEquals(expectedValue, checkPromoDiscount.toString());
    }

    private Product getMockedProduct() {
        return Product.builder()
                .id(1L)
                .quantity(3)
                .name("Самовар золотой")
                .price(BigDecimal.valueOf(256.24))
                .total(BigDecimal.valueOf(256.24).multiply(BigDecimal.valueOf(3)))
                .promotion(true)
                .build();
    }

}