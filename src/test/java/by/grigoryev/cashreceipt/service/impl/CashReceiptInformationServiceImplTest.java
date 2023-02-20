package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.dto.ProductDto;
import by.grigoryev.cashreceipt.service.CashReceiptInformationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CashReceiptInformationServiceImplTest {

    @Spy
    private CashReceiptInformationService cashReceiptInformationService;

    @BeforeEach
    void setUp() {
        cashReceiptInformationService = new CashReceiptInformationServiceImpl();
    }

    @Test
    @DisplayName("test createCashReceiptHeader method should return expected string")
    void testCreateCashReceiptHeaderShouldReturnExpectedString() {
        String expectedValue = "Cash Receipt";

        StringBuilder actualValue = cashReceiptInformationService.createCashReceiptHeader();

        assertThat(actualValue).contains(expectedValue);
    }

    @Test
    @DisplayName("test createCashReceiptBody method should return expected string")
    void testCreateCashReceiptBodyShouldReturnExpectedString() {
        ProductDto mockedProductDto = new ProductDto(
                1L,
                3,
                "Samovar",
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(15),
                true
        );

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
    @DisplayName("test createCashReceiptResults method should return expected string")
    void testCreateCashReceiptResultsShouldReturnExpectedString() {
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
    @DisplayName("test createCashReceiptPromoDiscount method should return expected string")
    void testCreateCashReceiptPromoDiscountShouldReturnExpectedString() {
        String expectedStringValue = "Samovar";
        BigDecimal expectedDecimalValue = new BigDecimal("256");

        StringBuilder actualValue = cashReceiptInformationService
                .createCashReceiptPromoDiscount(expectedStringValue, expectedDecimalValue);

        assertThat(actualValue).contains(expectedStringValue).contains(expectedDecimalValue.toString());
    }

}