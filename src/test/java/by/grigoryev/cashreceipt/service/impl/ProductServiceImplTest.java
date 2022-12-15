package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.exception.NoSuchProductException;
import by.grigoryev.cashreceipt.model.Product;
import by.grigoryev.cashreceipt.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    public static final long ID = 1L;
    public static final int QUANTITY = 3;
    public static final String NAME = "Самовар золотой";
    public static final BigDecimal PRICE = BigDecimal.valueOf(256.24);
    public static final boolean PROMOTION = true;

    private ProductServiceImpl productService;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = spy(new ProductServiceImpl(productRepository));
    }

    @Test
    @DisplayName("testing findAll method")
    void findAll() {
        Product product = getMockedProduct();

        doReturn(List.of(product)).when(productRepository).findAll();
        List<Product> products = productService.findAll();
        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
    }

    @Test
    @DisplayName("testing if exception throws when Product is not found")
    void findByIdThrowsException() {
        doThrow(new NoSuchProductException("Product with ID " + ID + " does not exist"))
                .when(productRepository).findById(ID);

        Exception exception = assertThrows(NoSuchProductException.class, () -> productService.findById(ID));

        String expectedMessage = "Product with ID " + ID + " does not exist";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("testing if Product returns when it is found")
    void findByIdReturnsProduct() {
        Product mockedProduct = getMockedProduct();
        doReturn(Optional.of(mockedProduct))
                .when(productRepository).findById(ID);

        Product product = productService.findById(ID);

        assertEquals(mockedProduct, product);
    }

    @Test
    @DisplayName("testing if Product saves with same id")
    void saveWithSameId() {
        Product mockedProduct = getMockedProduct();
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(productRepository)
                .save(any(Product.class));

        Product result = productService.save(mockedProduct);
        assertEquals(mockedProduct, result);
        assertEquals(ID, result.getId());
    }

    private Product getMockedProduct() {
        return Product.builder()
                .id(ID)
                .quantity(QUANTITY)
                .name(NAME)
                .price(PRICE)
                .total(PRICE.multiply(BigDecimal.valueOf(QUANTITY)))
                .promotion(PROMOTION)
                .build();
    }

}