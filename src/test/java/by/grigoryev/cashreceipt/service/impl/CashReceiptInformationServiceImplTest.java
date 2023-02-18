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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

class CashReceiptInformationServiceImplTest {

    private static final long ID = 1L;
    private static final int QUANTITY = 3;
    private static final String NAME = "Самовар золотой";
    private static final BigDecimal PRICE = BigDecimal.valueOf(256.24);
    private static final boolean PROMOTION = true;

    private CashReceiptInformationService cashReceiptInformationService;

    @BeforeEach
    void setUp() {
        cashReceiptInformationService = spy(new CashReceiptInformationServiceImpl());
    }

    @Test
    @DisplayName("createCashReceiptHeader method should return expected string")
    void createCashReceiptHeaderShouldReturnExpectedString() {
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

        StringBuilder actualValue = cashReceiptInformationService.createCashReceiptHeader();

        assertThat(actualValue).hasToString(expectedValue);
    }

    @Test
    @DisplayName("createCashReceiptBody method should return expected string")
    void createCashReceiptBodyShouldReturnExpectedString() {
        ProductDto mockedProductDto = getMockedProductDto();

        String expectedValue = """
                %-2s  | %-15s | %-6s | %s
                """.formatted(
                mockedProductDto.quantity(),
                mockedProductDto.name(),
                mockedProductDto.price(),
                mockedProductDto.total()
        );

        StringBuilder actualValue = cashReceiptInformationService.createCashReceiptBody(mockedProductDto);

        assertThat(actualValue).hasToString(expectedValue);
    }

    @Test
    @DisplayName("createCashReceiptResults method should return expected string")
    void createCashReceiptResultsShouldReturnExpectedString() {
        BigDecimal totalSum = new BigDecimal("165.00");
        BigDecimal discountCardPercentage = new BigDecimal("3");
        BigDecimal discount = new BigDecimal("10.2500");
        StringBuilder promoDiscBuilder = new StringBuilder("PromoDiscount -10% : \"Woolen gloves\"");
        BigDecimal totalSumWithDiscount = new BigDecimal("143.137500");

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

        StringBuilder actualValue = cashReceiptInformationService
                .createCashReceiptResults(
                        totalSum,
                        discountCardPercentage,
                        discount,
                        promoDiscBuilder,
                        totalSumWithDiscount
                );

        assertThat(actualValue).hasToString(expectedValue);
    }

    @Test
    @DisplayName("createCashReceiptPromoDiscount method should return expected string")
    void createCashReceiptPromoDiscountShouldReturnExpectedString() {
        String expectedValue = """
                PromoDiscount -10%s : "%s"
                more than 5 items: -%s
                """.formatted(
                "%",
                NAME,
                PRICE
        );

        StringBuilder actualValue = cashReceiptInformationService
                .createCashReceiptPromoDiscount(NAME, PRICE);

        assertThat(actualValue).hasToString(expectedValue);
    }

    private ProductDto getMockedProductDto() {
        return new ProductDto(
                ID,
                QUANTITY,
                NAME,
                PRICE,
                PRICE.multiply(BigDecimal.valueOf(QUANTITY)),
                PROMOTION
        );
    }

}