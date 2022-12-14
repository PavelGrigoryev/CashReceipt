package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.dto.ProductDto;
import by.grigoryev.cashreceipt.service.CashReceiptInformationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

class CashReceiptInformationServiceImplTest {

    private CashReceiptInformationService cashReceiptInformationService;

    @BeforeEach
    void setUp() {
        cashReceiptInformationService = spy(new CashReceiptInformationServiceImpl());
    }

    @Test
    @DisplayName("testing createCashReceiptHeader method")
    void createCashReceiptHeader() {
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

        StringBuilder checkHeader = cashReceiptInformationService.createCashReceiptHeader();

        assertEquals(expectedValue, checkHeader.toString());
    }

    @Test
    @DisplayName("testing createCashReceiptBody method")
    void createCashReceiptBody() {
        ProductDto mockedProductDto = getMockedProductDto();

        String expectedValue = """
                %-2s  | %-15s | %-6s | %s
                """.formatted(
                mockedProductDto.getQuantity(),
                mockedProductDto.getName(),
                mockedProductDto.getPrice(),
                mockedProductDto.getTotal()
        );

        StringBuilder checkBody = cashReceiptInformationService.createCashReceiptBody(mockedProductDto);

        assertEquals(expectedValue, checkBody.toString());
    }

    @Test
    @DisplayName("testing createCashReceiptResults method")
    void createCashReceiptResults() {
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

        StringBuilder checkResults = cashReceiptInformationService
                .createCashReceiptResults(totalSum, discountCardPercentage, discount,
                        promoDiscBuilder, totalSumWithDiscount);

        assertEquals(expectedValue, checkResults.toString());
    }

    @Test
    @DisplayName("testing createCashReceiptPromoDiscount method")
    void createCashReceiptPromoDiscount() {
        String productName = getMockedProductDto().getName();
        BigDecimal promotionDiscount = getMockedProductDto().getPrice();

        String expectedValue = """
                PromoDiscount -10%s : "%s"
                more than 5 items: -%s
                """.formatted(
                "%",
                productName,
                promotionDiscount
        );

        StringBuilder checkPromoDiscount = cashReceiptInformationService
                .createCashReceiptPromoDiscount(productName, promotionDiscount);

        assertEquals(expectedValue, checkPromoDiscount.toString());
    }

    private ProductDto getMockedProductDto() {
        return ProductDto.builder()
                .id(1L)
                .quantity(3)
                .name("?????????????? ??????????????")
                .price(BigDecimal.valueOf(256.24))
                .total(BigDecimal.valueOf(256.24).multiply(BigDecimal.valueOf(3)))
                .promotion(true)
                .build();
    }

}