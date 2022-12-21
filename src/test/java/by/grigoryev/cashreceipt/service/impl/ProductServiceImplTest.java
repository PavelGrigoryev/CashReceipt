package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.dto.ProductDto;
import by.grigoryev.cashreceipt.exception.NoSuchProductException;
import by.grigoryev.cashreceipt.mapper.ProductMapper;
import by.grigoryev.cashreceipt.model.Product;
import by.grigoryev.cashreceipt.repository.ProductRepository;
import by.grigoryev.cashreceipt.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

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
    public static final int NEW_QUANTITY = 2;
    public static final long NEW_ID = 2L;

    private ProductService productService;
    private ProductRepository productRepository;
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

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
        List<Product> products = productService.findAll()
                .stream()
                .map(productMapper::fromProductDto)
                .toList();
        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
    }

    @Test
    @DisplayName("testing if exception throws when Product is not found by id")
    void findByIdThrowsException() {
        doThrow(new NoSuchProductException("Product with ID " + ID + " does not exist"))
                .when(productRepository).findById(ID);

        Exception exception = assertThrows(NoSuchProductException.class, () -> productService.findById(ID));

        String expectedMessage = "Product with ID " + ID + " does not exist";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("testing if Product returns when it is found by id")
    void findByIdReturnsProduct() {
        Product mockedProduct = getMockedProduct();
        doReturn(Optional.of(mockedProduct))
                .when(productRepository).findById(ID);

        ProductDto productDto = productService.findById(ID);
        Product product = productMapper.fromProductDto(productDto);

        assertEquals(mockedProduct, product);
    }

    @Test
    @DisplayName("testing save method")
    void save() {
        Product mockedProduct = getMockedProduct();
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(productRepository)
                .save(any(Product.class));

        ProductDto productDto = productService.save(productMapper.toProductDto(getMockedProduct()));
        Product product = productMapper.fromProductDto(productDto);

        assertEquals(mockedProduct, product);
    }

    @Test
    @DisplayName("testing update method")
    void update() {
        Product mockedProduct = getMockedProduct();
        doReturn(Optional.of(mockedProduct))
                .when(productRepository).findById(ID);

        if (!(NEW_QUANTITY == (mockedProduct.getQuantity()))) {
            mockedProduct.setId(NEW_ID);
            mockedProduct.setQuantity(NEW_QUANTITY);
            mockedProduct.setTotal(mockedProduct.getPrice().multiply(BigDecimal.valueOf(NEW_QUANTITY)));

            doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                    .when(productRepository)
                    .save(any(Product.class));

            Product product = productRepository.save(mockedProduct);

            assertEquals(mockedProduct, product);
        }

        ProductDto productDto = productService.update(ID, QUANTITY);
        Product product = productMapper.fromProductDto(productDto);

        assertEquals(mockedProduct, product);
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