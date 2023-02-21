package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.dto.DiscountCardDto;
import by.grigoryev.cashreceipt.dto.ProductDto;
import by.grigoryev.cashreceipt.mapper.DiscountCardMapper;
import by.grigoryev.cashreceipt.mapper.ProductMapper;
import by.grigoryev.cashreceipt.service.CashReceiptInformationService;
import by.grigoryev.cashreceipt.service.DiscountCardService;
import by.grigoryev.cashreceipt.service.ProductService;
import by.grigoryev.cashreceipt.service.UploadFileService;
import by.grigoryev.cashreceipt.util.testbuilder.DiscountCardTestBuilder;
import by.grigoryev.cashreceipt.util.testbuilder.ProductTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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

    @BeforeEach
    void setUp() {
        cashReceiptLogicService = new CashReceiptLogicServiceImpl(productService, discountCardService,
                cashReceiptInformationService, uploadFileService);
    }

    @Test
    @DisplayName("test getTotalSum method should return total(price*quantity)")
    void testGetTotalSumShouldReturnNine() {
        ProductDto mockedProductDto = productMapper.toProductDto(productTestBuilder.build());
        BigDecimal expectedValue = mockedProductDto.total();
        List<ProductDto> mockedProductDtoList = new ArrayList<>();
        mockedProductDtoList.add(mockedProductDto);

        doReturn(mockedProductDto)
                .when(productService)
                .update(mockedProductDto.id(), mockedProductDto.quantity());

        BigDecimal actualValue = cashReceiptLogicService.getTotalSum("1-3", mockedProductDtoList);

        assertThat(actualValue.stripTrailingZeros()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test getDiscount method should return 3")
    void testGetDiscountShouldReturnThree() {
        DiscountCardDto mockedDiscountCardDto = Mappers.getMapper(DiscountCardMapper.class)
                .toDiscountCardDto(DiscountCardTestBuilder.aDiscountCard().build());
        BigDecimal expectedValue = new BigDecimal("3");

        BigDecimal actualValue = cashReceiptLogicService.getDiscount(BigDecimal.valueOf(100), mockedDiscountCardDto);

        assertThat(actualValue.stripTrailingZeros()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test getTotalSumWithDiscount method should return 10")
    void testGetTotalSumWithDiscountShouldReturnTen() {
        BigDecimal expectedValue = new BigDecimal("10");
        ProductDto mockedProductDto = productMapper.toProductDto(productTestBuilder.build());

        BigDecimal actualValue = cashReceiptLogicService.getTotalSumWithDiscount(
                List.of(mockedProductDto),
                BigDecimal.valueOf(10),
                new StringBuilder("checkBuilder"),
                new StringBuilder("promoDiscBuilder")
        );

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test getPromotionDiscount method should return expected value")
    void testGetPromotionDiscountShouldReturnExpectedValue() {
        ProductDto mockedProductDto = productMapper.toProductDto(productTestBuilder.build());
        BigDecimal expectedValue = mockedProductDto.total()
                .divide(BigDecimal.valueOf(10), 4, RoundingMode.HALF_UP)
                .stripTrailingZeros();

        BigDecimal actualValue = cashReceiptLogicService.getPromotionDiscount(mockedProductDto);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

}