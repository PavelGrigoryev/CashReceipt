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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private static final Long ID = 1L;
    private static final Integer QUANTITY = 3;
    private static final String NAME = "Самовар золотой";
    private static final BigDecimal PRICE = BigDecimal.valueOf(256.24);
    private static final Boolean PROMOTION = true;
    private static final Long NEW_ID = 2L;
    private static final Integer NEW_QUANTITY = 2;

    private ProductService productService;
    private ProductRepository productRepository;
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = spy(new ProductServiceImpl(productRepository));
    }

    @Test
    @DisplayName("test findAll method should return List of size 1")
    void testFindAllShouldReturnListOfSizeOne() {
        Product mockedProduct = getMockedProduct();
        int expectedSize = 1;

        doReturn(List.of(mockedProduct))
                .when(productRepository)
                .findAll();

        List<ProductDto> actualValues = productService.findAll();

        assertThat(actualValues).hasSize(expectedSize);
    }

    @Test
    @DisplayName("test findAll method should return sorted by id List of ProductDto")
    void testFindAllShouldReturnSortedByIdListOfProductDto() {
        List<Product> mockedProducts = getMockedProducts();
        List<ProductDto> expectedValues = mockedProducts
                .stream()
                .map(productMapper::toProductDto)
                .sorted(Comparator.comparing(ProductDto::id))
                .toList();

        doReturn(mockedProducts)
                .when(productRepository)
                .findAll();

        List<ProductDto> actualValues = productService.findAll();

        assertThat(actualValues).isEqualTo(expectedValues);
    }

    @Test
    @DisplayName("test findById method throw NoSuchProductException")
    void testFindByIdThrowNoSuchProductException() {
        Product mockedProduct = getMockedProduct();

        doReturn(Optional.of(mockedProduct))
                .when(productRepository)
                .findById(ID);

        assertThrows(NoSuchProductException.class, () -> productService.findById(NEW_ID));
    }

    @Test
    @DisplayName("test findById method throw NoSuchProductException with expected message")
    void testFindByIdThrowNoSuchProductExceptionWithExpectedMessage() {
        String expectedMessage = "Product with ID " + ID + " does not exist";

        Exception exception = assertThrows(NoSuchProductException.class, () -> productService.findById(ID));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("test findById method should return expected ProductDto")
    void testFindByIdShouldReturnExpectedProductDto() {
        Product mockedProduct = getMockedProduct();
        ProductDto expectedValue = productMapper.toProductDto(mockedProduct);

        doReturn(Optional.of(mockedProduct))
                .when(productRepository)
                .findById(ID);

        ProductDto actualValue = productService.findById(ID);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test save method should return expected ProductDto")
    void testSaveShouldReturnExpectedProductDto() {
        Product mockedProduct = getMockedProduct();
        ProductDto expectedValue = productMapper.toProductDto(mockedProduct);

        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(productRepository)
                .save(any(Product.class));

        ProductDto actualValue = productService.save(productMapper.toProductDto(getMockedProduct()));

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test update method should return ProductDto with new quantity")
    void testUpdateShouldReturnProductDtoWithNewQuantity() {
        Product mockedProduct = getMockedProduct();
        ProductDto expectedValue = productMapper.toProductDto(mockedProduct);

        doReturn(Optional.of(mockedProduct))
                .when(productRepository)
                .findById(ID);

        doReturn(mockedProduct)
                .when(productRepository)
                .save(mockedProduct);

        ProductDto actualValue = productService.update(ID, NEW_QUANTITY);

        assertThat(actualValue.id()).isEqualTo(expectedValue.id());
        assertThat(actualValue.quantity()).isNotEqualTo(expectedValue.quantity());
    }

    @Test
    @DisplayName("test update method should return same ProductDto without update")
    void testUpdateShouldReturnProductDtoWithoutUpdate() {
        Product mockedProduct = getMockedProduct();
        ProductDto expectedValue = productMapper.toProductDto(mockedProduct);

        doReturn(Optional.of(mockedProduct))
                .when(productRepository)
                .findById(ID);

        ProductDto actualValue = productService.update(ID, QUANTITY);

        assertThat(actualValue).isEqualTo(expectedValue);
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

    private List<Product> getMockedProducts() {
        return List.of(
                Product.builder()
                        .id(NEW_ID)
                        .quantity(NEW_QUANTITY)
                        .name(NAME)
                        .price(PRICE)
                        .total(PRICE.multiply(BigDecimal.valueOf(QUANTITY)))
                        .promotion(PROMOTION)
                        .build(),
                Product.builder()
                        .id(ID)
                        .quantity(QUANTITY)
                        .name(NAME)
                        .price(PRICE)
                        .total(PRICE.multiply(BigDecimal.valueOf(QUANTITY)))
                        .promotion(PROMOTION)
                        .build()
        );
    }

}