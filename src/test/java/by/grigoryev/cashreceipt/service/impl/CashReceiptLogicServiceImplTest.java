package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.model.DiscountCard;
import by.grigoryev.cashreceipt.model.Product;
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
        List<Product> products = new ArrayList<>();

        String[] splitSpace = idAndQuantity.split(" ");
        BigDecimal expectedValue = new BigDecimal("0");

        for (String string : splitSpace) {
            String[] splitHyphen = string.split("-");
            String id = splitHyphen[0];
            String quantity = splitHyphen[1];

            doReturn(getMockedProduct(Long.valueOf(id), Integer.valueOf(quantity))).when(productService)
                    .update(Long.valueOf(id), Integer.valueOf(quantity));

            Product product = productService.update(Long.valueOf(id), Integer.valueOf(quantity));

            products.add(product);
            expectedValue = expectedValue.add(product.getTotal());

            assertEquals(product.getId(), Long.valueOf(id));
            assertEquals(product.getQuantity(), Integer.valueOf(quantity));
        }

        BigDecimal totalSum = cashReceiptLogicService.getTotalSum(idAndQuantity, products);

        assertEquals(expectedValue, totalSum);
    }

    @Test
    @DisplayName("testing getDiscount method")
    void getDiscount() {
        DiscountCard discountCard = getMockedDiscountCard();
        BigDecimal totalSum = new BigDecimal("255");

        BigDecimal expectedValue = totalSum.divide(BigDecimal.valueOf(100), 4, RoundingMode.UP)
                .multiply(discountCard.getDiscountPercentage());

        BigDecimal discount = cashReceiptLogicService.getDiscount(totalSum, discountCard);

        assertEquals(expectedValue, discount);
    }

    @Test
    @DisplayName("test getTotalSumWithDiscount method")
    void getTotalSumWithDiscount() {
        BigDecimal expectedValue = new BigDecimal("255");

        List<Product> products = List.of(getMockedProduct(1L, 6));

        BigDecimal totalSumWithDiscount = cashReceiptLogicService.getTotalSumWithDiscount(
                List.of(getMockedProduct(1L, 6)),
                expectedValue,
                new StringBuilder(),
                new StringBuilder()
        );

        for (Product product : products) {

            doReturn(new StringBuilder()).when(cashReceiptInformationService)
                    .createCashReceiptBody(product);

            new StringBuilder(cashReceiptInformationService.createCashReceiptBody(product));

            if (Boolean.TRUE.equals(product.getPromotion()) && product.getQuantity() > 5) {

                BigDecimal promotionDiscount = product.getTotal()
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.UP)
                        .multiply(BigDecimal.valueOf(10)).stripTrailingZeros();
                expectedValue = expectedValue.subtract(promotionDiscount);

                doReturn(new StringBuilder()).when(cashReceiptInformationService)
                        .createCashReceiptPromoDiscount(product.getName(), promotionDiscount);

                new StringBuilder
                        (cashReceiptInformationService.createCashReceiptPromoDiscount(product.getName(), promotionDiscount));
            }

        }

        assertEquals(products.get(0), getMockedProduct(1L, 6));
        assertEquals(expectedValue, totalSumWithDiscount);
    }

    private DiscountCard getMockedDiscountCard() {
        return DiscountCard.builder()
                .id(1L)
                .discountCardNumber("1234")
                .discountPercentage(BigDecimal.valueOf(3))
                .build();
    }

    private Product getMockedProduct(Long id, Integer quantity) {
        return Product.builder()
                .id(id)
                .quantity(quantity)
                .name("Самовар золотой")
                .price(BigDecimal.valueOf(256.24))
                .total(BigDecimal.valueOf(256.24).multiply(BigDecimal.valueOf(3)))
                .promotion(true)
                .build();
    }

}