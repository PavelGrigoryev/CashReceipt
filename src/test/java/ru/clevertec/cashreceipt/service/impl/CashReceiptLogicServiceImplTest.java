package ru.clevertec.cashreceipt.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.cashreceipt.dto.DiscountCardDto;
import ru.clevertec.cashreceipt.dto.ProductDto;
import ru.clevertec.cashreceipt.mapper.DiscountCardMapper;
import ru.clevertec.cashreceipt.mapper.ProductMapper;
import ru.clevertec.cashreceipt.model.DiscountCard;
import ru.clevertec.cashreceipt.service.CashReceiptInformationService;
import ru.clevertec.cashreceipt.service.DiscountCardService;
import ru.clevertec.cashreceipt.service.ProductService;
import ru.clevertec.cashreceipt.service.UploadFileService;
import ru.clevertec.cashreceipt.util.testbuilder.DiscountCardTestBuilder;
import ru.clevertec.cashreceipt.util.testbuilder.ProductTestBuilder;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CashReceiptLogicServiceImplTest {

    @Mock
    private ProductService productService;
    @Mock
    private DiscountCardService discountCardService;
    @Mock
    private CashReceiptInformationService cashReceiptInformationService;
    @Mock
    private UploadFileService uploadFileService;
    @InjectMocks
    private CashReceiptLogicServiceImpl cashReceiptLogicService;
    private final ProductTestBuilder productTestBuilder = ProductTestBuilder.aProduct();
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
    private final DiscountCardMapper discountCardMapper = Mappers.getMapper(DiscountCardMapper.class);

    @BeforeEach
    void setUp() {
        cashReceiptLogicService = new CashReceiptLogicServiceImpl(productService, discountCardService,
                cashReceiptInformationService, uploadFileService);
    }

    @Test
    @DisplayName("test getProducts method should return List of expected products")
    void testGetProductsShouldReturnListOfExpectedProducts() {
        ProductDto mockedProductDto = productMapper.toProductDto(productTestBuilder.build());
        List<ProductDto> expectedValues = List.of(mockedProductDto);

        doReturn(mockedProductDto)
                .when(productService)
                .update(mockedProductDto.id(), mockedProductDto.quantity());

        List<ProductDto> actualValues = cashReceiptLogicService.getProducts("1-3");

        assertThat(actualValues).isEqualTo(expectedValues);
    }

    @Test
    @DisplayName("test getCashReceiptResults method should return expected value")
    void testGetCashReceiptResultsShouldReturnExpectedValue() {
        DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();
        DiscountCardDto discountCardDto = discountCardMapper.toDiscountCardDto(discountCard);
        StringBuilder expectedValue = new StringBuilder("Hello");

        doReturn(discountCardDto)
                .when(discountCardService)
                .findByDiscountCardNumber(discountCard.getDiscountCardNumber());

        StringBuilder actualValue = cashReceiptLogicService.getCashReceiptResults(
                "1234",
                new StringBuilder("Hello"),
                new StringBuilder("There"),
                new BigDecimal[]{new BigDecimal("0")},
                new BigDecimal("22")
        );

        assertThat(actualValue).startsWith(expectedValue);
    }

    @Test
    @DisplayName("test getPromotionDiscount method should return expected value")
    void testGetPromotionDiscountShouldReturnExpectedValue() {
        ProductDto mockedProductDto = productMapper.toProductDto(productTestBuilder.build());
        BigDecimal expectedValue = BigDecimal.valueOf(76.872);

        BigDecimal actualValue = cashReceiptLogicService.getPromotionDiscount(mockedProductDto);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test getDiscount method should return expected value")
    void testGetDiscountShouldReturnExpectedValue() {
        BigDecimal totalSum = BigDecimal.TEN;
        BigDecimal percentage = BigDecimal.ONE;
        BigDecimal expectedValue = new BigDecimal("0.10");

        BigDecimal actualValue = cashReceiptLogicService.getDiscount(totalSum, percentage);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

}
