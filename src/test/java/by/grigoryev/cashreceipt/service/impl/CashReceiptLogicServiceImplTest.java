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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CashReceiptLogicServiceImplTest {

    private ProductService productService;
    private CashReceiptInformationService cashReceiptInformationService;
    private CashReceiptLogicServiceImpl cashReceiptLogicService;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        DiscountCardService discountCardService = mock(DiscountCardService.class);
        cashReceiptInformationService = mock(CashReceiptInformationService.class);
        UploadFileService uploadFileService = mock(UploadFileService.class);
        cashReceiptLogicService = spy(new CashReceiptLogicServiceImpl(productService, discountCardService,
                cashReceiptInformationService, uploadFileService));
    }

    @Test
    @DisplayName("testing getTotalSum method")
    void getTotalSum() {
        String idAndQuantity = "3-6 2-6 1-7";
        List<ProductDto> productDtoList = new ArrayList<>();

        String[] splitSpace = idAndQuantity.split(" ");
        BigDecimal expectedValue = new BigDecimal("0");

        for (String string : splitSpace) {
            String[] splitHyphen = string.split("-");
            String id = splitHyphen[0];
            String quantity = splitHyphen[1];

            doReturn(getMockedProductDto(Long.valueOf(id), Integer.valueOf(quantity))).when(productService)
                    .update(Long.valueOf(id), Integer.valueOf(quantity));

            ProductDto productDto = productService.update(Long.valueOf(id), Integer.valueOf(quantity));

            productDtoList.add(productDto);
            expectedValue = expectedValue.add(productDto.total());

            assertEquals(productDto.id(), Long.valueOf(id));
            assertEquals(productDto.quantity(), Integer.valueOf(quantity));
        }

        BigDecimal totalSum = cashReceiptLogicService.getTotalSum(idAndQuantity, productDtoList);

        assertEquals(expectedValue, totalSum);
    }

    @Test
    @DisplayName("testing getDiscount method")
    void getDiscount() {
        DiscountCardDto discountCardDto = getMockedDiscountCardDto();
        BigDecimal totalSum = new BigDecimal("255");

        BigDecimal expectedValue = totalSum.divide(BigDecimal.valueOf(100), 4, RoundingMode.UP)
                .multiply(discountCardDto.discountPercentage());

        BigDecimal discount = cashReceiptLogicService.getDiscount(totalSum, discountCardDto);

        assertEquals(expectedValue, discount);
    }

    @Test
    @DisplayName("test getTotalSumWithDiscount method")
    void getTotalSumWithDiscount() {
        BigDecimal expectedValue = new BigDecimal("255");

        List<ProductDto> productDtoList = List.of(getMockedProductDto(1L, 6));

        BigDecimal totalSumWithDiscount = cashReceiptLogicService.getTotalSumWithDiscount(
                List.of(getMockedProductDto(1L, 6)),
                expectedValue,
                new StringBuilder(),
                new StringBuilder()
        );

        for (ProductDto productDto : productDtoList) {

            doReturn(new StringBuilder()).when(cashReceiptInformationService)
                    .createCashReceiptBody(productDto);

            new StringBuilder(cashReceiptInformationService.createCashReceiptBody(productDto));

            if (Boolean.TRUE.equals(productDto.promotion()) && productDto.quantity() > 5) {

                BigDecimal promotionDiscount = productDto.total()
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.UP)
                        .multiply(BigDecimal.valueOf(10)).stripTrailingZeros();
                expectedValue = expectedValue.subtract(promotionDiscount);

                doReturn(new StringBuilder()).when(cashReceiptInformationService)
                        .createCashReceiptPromoDiscount(productDto.name(), promotionDiscount);

                new StringBuilder
                        (cashReceiptInformationService.createCashReceiptPromoDiscount(productDto.name(), promotionDiscount));
            }

        }

        assertEquals(productDtoList.get(0), getMockedProductDto(1L, 6));
        assertEquals(expectedValue, totalSumWithDiscount);
    }

    private DiscountCardDto getMockedDiscountCardDto() {
        return new DiscountCardDto(
                1L,
                "1234",
                BigDecimal.valueOf(3)
        );
    }

    private ProductDto getMockedProductDto(Long id, Integer quantity) {
        return new ProductDto(
                id,
                quantity,
                "Самовар золотой",
                BigDecimal.valueOf(256.24),
                BigDecimal.valueOf(256.24).multiply(BigDecimal.valueOf(3)),
                true
        );
    }

}