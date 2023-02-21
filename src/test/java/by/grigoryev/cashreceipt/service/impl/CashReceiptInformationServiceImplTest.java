package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.dto.ProductDto;
import by.grigoryev.cashreceipt.mapper.ProductMapper;
import by.grigoryev.cashreceipt.service.CashReceiptInformationService;
import by.grigoryev.cashreceipt.util.testbuilder.ProductTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

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
    @DisplayName("test createCashReceiptHeader method should contains expected string with date and time")
    void testCreateCashReceiptHeaderShouldReturnExpectedString() {
        Clock clock = Clock.fixed(Instant.parse("2022-12-20T12:19:36Z"), ZoneId.of("UTC"));
        String expectedValue = "DATE: %s TIME: %s".formatted(LocalDate.now(clock),
                LocalTime.now(clock).format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        StringBuilder actualValue = cashReceiptInformationService
                .createCashReceiptHeader(LocalDate.now(clock), LocalTime.now(clock));

        assertThat(actualValue).contains(expectedValue);
    }

    @Test
    @DisplayName("test createCashReceiptBody method should return expected string")
    void testCreateCashReceiptBodyShouldReturnExpectedString() {
        ProductDto mockedProductDto = Mappers.getMapper(ProductMapper.class)
                .toProductDto(ProductTestBuilder.aProduct().build());

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

    @ParameterizedTest(name = "{arguments} test")
    @DisplayName("test createCashReceiptResults method should return expected string")
    @MethodSource("getArgumentsForTestWithResults")
    void testCreateCashReceiptResultsShouldReturnExpectedString(BigDecimal totalSum,
                                                                BigDecimal discountCardPercentage,
                                                                BigDecimal discount,
                                                                StringBuilder promoDiscBuilder,
                                                                BigDecimal totalSumWithDiscount) {
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
    @DisplayName("test createCashReceiptPromoDiscount method should contains expected string")
    void testCreateCashReceiptPromoDiscountShouldReturnExpectedString() {
        String expectedStringValue = "Samovar";
        BigDecimal expectedDecimalValue = new BigDecimal("256.23");

        StringBuilder actualValue = cashReceiptInformationService
                .createCashReceiptPromoDiscount(expectedStringValue, expectedDecimalValue);

        assertThat(actualValue).contains(expectedStringValue).contains(expectedDecimalValue.toString());
    }

    private static Stream<Arguments> getArgumentsForTestWithResults() {
        return Stream.of(
                Arguments.of(
                        BigDecimal.valueOf(165.00),
                        BigDecimal.valueOf(3),
                        BigDecimal.valueOf(10.2500),
                        new StringBuilder("PromoDiscount -3% : \"Woolen gloves\""),
                        BigDecimal.valueOf(143.137500)
                        ),
                Arguments.of(
                        BigDecimal.valueOf(11.50),
                        BigDecimal.valueOf(10),
                        BigDecimal.valueOf(33.2500),
                        new StringBuilder(),
                        BigDecimal.valueOf(45.227500)
                ),
                Arguments.of(
                        BigDecimal.valueOf(33325.25),
                        BigDecimal.valueOf(10),
                        BigDecimal.valueOf(256.365),
                        new StringBuilder("PromoDiscount -10% : \"Samovar\""),
                        BigDecimal.valueOf(25789.25560000)
                )
        );
    }

}