package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.dto.DiscountCardDto;
import by.grigoryev.cashreceipt.dto.ProductDto;
import by.grigoryev.cashreceipt.service.CashReceiptInformationService;
import by.grigoryev.cashreceipt.service.DiscountCardService;
import by.grigoryev.cashreceipt.service.ProductService;
import by.grigoryev.cashreceipt.service.UploadFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CashReceiptLogicServiceImplTest {

    private static final String ID_AND_QUANTITY = "3-4";
    private static final Long PRODUCT_ID = 3L;
    private static final Integer QUANTITY = 4;
    private static final String NAME = "Самовар золотой";
    private static final BigDecimal PRICE = BigDecimal.valueOf(2.25);
    private static final Boolean PROMOTION = true;
    private static final Long DISCOUNT_CARD_ID = 1L;
    private static final String DISCOUNT_CARD_NUMBER = "1234";
    private static final BigDecimal DISCOUNT_PERCENTAGE = BigDecimal.valueOf(10);

    private ProductService productService;
    private DiscountCardService discountCardService;
    private CashReceiptInformationService cashReceiptInformationService;
    private CashReceiptLogicServiceImpl cashReceiptLogicService;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        discountCardService = mock(DiscountCardService.class);
        cashReceiptInformationService = mock(CashReceiptInformationService.class);
        UploadFileService uploadFileService = mock(UploadFileService.class);
        cashReceiptLogicService = spy(new CashReceiptLogicServiceImpl(productService, discountCardService,
                cashReceiptInformationService, uploadFileService));
    }

    @Test
    @DisplayName("test createCashReceipt method should return expected string")
    void testCreateCashReceiptShouldReturnExpectedString() {
        String expectedValue = "HeaderBodynull";
        DiscountCardDto mockedDiscountCardDto = getMockedDiscountCardDto();
        ProductDto mockedProductDto = getMockedProductDto();

        doReturn(mockedDiscountCardDto)
                .when(discountCardService)
                .findByDiscountCardNumber(DISCOUNT_CARD_NUMBER);

        doReturn(mockedProductDto)
                .when(productService)
                .update(PRODUCT_ID, QUANTITY);

        doReturn(new StringBuilder("Header"))
                .when(cashReceiptInformationService)
                .createCashReceiptHeader();

        doReturn(new StringBuilder("Body"))
                .when(cashReceiptInformationService)
                .createCashReceiptBody(mockedProductDto);

        String actualValue = cashReceiptLogicService.createCashReceipt(ID_AND_QUANTITY, DISCOUNT_CARD_NUMBER);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test getTotalSum method should return 9")
    void testGetTotalSumShouldReturnNine() {
        BigDecimal expectedValue = new BigDecimal("9");
        ProductDto mockedProductDto = getMockedProductDto();
        List<ProductDto> mockedProductDtoList = new ArrayList<>();
        mockedProductDtoList.add(mockedProductDto);

        doReturn(mockedProductDto)
                .when(productService)
                .update(PRODUCT_ID, QUANTITY);

        BigDecimal actualValue = cashReceiptLogicService.getTotalSum(ID_AND_QUANTITY, mockedProductDtoList);

        assertThat(actualValue.stripTrailingZeros()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test getDiscount method should return 3")
    void testGetDiscountShouldReturnThree() {
        DiscountCardDto mockedDiscountCardDto = getMockedDiscountCardDto();
        BigDecimal expectedValue = new BigDecimal("3");

        BigDecimal actualValue = cashReceiptLogicService.getDiscount(BigDecimal.valueOf(30), mockedDiscountCardDto);

        assertThat(actualValue.stripTrailingZeros()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test getTotalSumWithDiscount method should return 10")
    void testGetTotalSumWithDiscountShouldReturnTen() {
        BigDecimal expectedValue = new BigDecimal("10");
        ProductDto mockedProductDto = getMockedProductDto();

        BigDecimal actualValue = cashReceiptLogicService.getTotalSumWithDiscount(
                List.of(mockedProductDto),
                BigDecimal.valueOf(10),
                new StringBuilder("checkBuilder"),
                new StringBuilder("promoDiscBuilder")
        );

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test getPromotionDiscount method should return 0.9")
    void testGetPromotionDiscountShouldReturnZeroPointNine() {
        ProductDto mockedProductDto = getMockedProductDto();
        BigDecimal expectedValue = new BigDecimal("0.9");

        BigDecimal actualValue = cashReceiptLogicService.getPromotionDiscount(mockedProductDto);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    private DiscountCardDto getMockedDiscountCardDto() {
        return new DiscountCardDto(
                DISCOUNT_CARD_ID,
                DISCOUNT_CARD_NUMBER,
                DISCOUNT_PERCENTAGE
        );
    }

    private ProductDto getMockedProductDto() {
        return new ProductDto(
                PRODUCT_ID,
                QUANTITY,
                NAME,
                PRICE,
                PRICE.multiply(BigDecimal.valueOf(QUANTITY)),
                PROMOTION
        );
    }

}