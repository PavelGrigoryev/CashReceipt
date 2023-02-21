package ru.clevertec.cashreceipt.service.impl;

import ru.clevertec.cashreceipt.dto.ProductDto;
import ru.clevertec.cashreceipt.exception.NoSuchProductException;
import ru.clevertec.cashreceipt.mapper.ProductMapper;
import ru.clevertec.cashreceipt.model.Product;
import ru.clevertec.cashreceipt.repository.ProductRepository;
import ru.clevertec.cashreceipt.service.ProductService;
import ru.clevertec.cashreceipt.util.testbuilder.ProductTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Spy
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
    private static final ProductTestBuilder testBuilder = ProductTestBuilder.aProduct();
    @Captor
    private ArgumentCaptor<Product> captor;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository);
    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test should return List of size 1")
        void testFindAllShouldReturnListOfSizeOne() {
            Product mockedProduct = testBuilder.build();
            int expectedSize = 1;

            doReturn(List.of(mockedProduct))
                    .when(productRepository)
                    .findAll();

            List<ProductDto> actualValues = productService.findAll();

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return sorted by id List of ProductDto")
        void testFindAllShouldReturnSortedByIdListOfProductDto() {
            List<Product> mockedProducts = List.of(
                    testBuilder.withId(2L).build(),
                    testBuilder.withId(3L).build(),
                    testBuilder.build()
            );
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
        @DisplayName("test should return empty List")
        void testFindAllShouldReturnEmptyList() {
            List<ProductDto> expectedValues = List.of();

            doReturn(List.of())
                    .when(productRepository)
                    .findAll();

            List<ProductDto> actualValues = productService.findAll();

            assertThat(actualValues).isEqualTo(expectedValues);
        }

    }

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test throw NoSuchProductException")
        void testFindByIdThrowNoSuchProductException() {
            long id = 2L;

            doThrow(new NoSuchProductException(""))
                    .when(productRepository)
                    .findById(id);

            assertThrows(NoSuchProductException.class, () -> productService.findById(id));
        }

        @Test
        @DisplayName("test throw NoSuchProductException with expected message")
        void testFindByIdThrowNoSuchProductExceptionWithExpectedMessage() {
            long id = 1L;
            String expectedMessage = "Product with ID " + id + " does not exist";

            Exception exception = assertThrows(NoSuchProductException.class, () -> productService.findById(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected ProductDto")
        void testFindByIdShouldReturnExpectedProductDto() {
            Product mockedProduct = testBuilder.build();
            long id = mockedProduct.getId();
            ProductDto expectedValue = productMapper.toProductDto(mockedProduct);

            doReturn(Optional.of(mockedProduct))
                    .when(productRepository)
                    .findById(id);

            ProductDto actualValue = productService.findById(id);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class SaveTest {

        @ParameterizedTest(name = "{arguments} test")
        @DisplayName("test should capture save value")
        @MethodSource("ru.clevertec.cashreceipt.service.impl.ProductServiceImplTest#getArgumentsForSaveTest")
        void testSaveShouldCaptureValue(Product expectedValue) {
            doReturn(expectedValue)
                    .when(productRepository)
                    .save(expectedValue);

            productService.save(productMapper.toProductDto(expectedValue));

            verify(productRepository)
                    .save(captor.capture());

            Product captorValue = captor.getValue();

            assertThat(captorValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class UpdateTest {

        @Test
        @DisplayName("test should return ProductDto with new quantity")
        void testUpdateShouldReturnProductDtoWithNewQuantity() {
            Product mockedProduct = testBuilder.build();
            ProductDto expectedValue = productMapper.toProductDto(mockedProduct);
            long id = mockedProduct.getId();
            int newQuantity = 2;

            doReturn(Optional.of(mockedProduct))
                    .when(productRepository)
                    .findById(id);

            doReturn(mockedProduct)
                    .when(productRepository)
                    .save(mockedProduct);

            ProductDto actualValue = productService.update(id, newQuantity);

            assertThat(actualValue.id()).isEqualTo(expectedValue.id());
            assertThat(actualValue.quantity()).isNotEqualTo(expectedValue.quantity());
        }

        @Test
        @DisplayName("test should return same ProductDto without update")
        void testUpdateShouldReturnProductDtoWithoutUpdate() {
            Product mockedProduct = testBuilder.build();
            ProductDto expectedValue = productMapper.toProductDto(mockedProduct);
            long id = mockedProduct.getId();
            int oldQuantity = mockedProduct.getQuantity();

            doReturn(Optional.of(mockedProduct))
                    .when(productRepository)
                    .findById(id);

            ProductDto actualValue = productService.update(id, oldQuantity);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class DeleteTest {

        @Test
        @DisplayName("test should invoke method 1 time")
        void testDeleteById() {
            Product mockedProduct = testBuilder.build();
            long id = mockedProduct.getId();

            doReturn(Optional.of(mockedProduct))
                    .when(productRepository)
                    .findById(id);

            doNothing()
                    .when(productRepository)
                    .deleteById(id);

            productService.deleteById(id);

            verify(productRepository, times(1))
                    .deleteById(id);
        }

    }

    private static Stream<Arguments> getArgumentsForSaveTest() {
        return Stream.of(
                Arguments.of(
                        testBuilder.build()
                ),
                Arguments.of(
                        testBuilder.withId(2L)
                        .withName("Banana")
                        .withQuantity(5)
                        .withPrice(BigDecimal.TEN)
                        .build()
                ),
                Arguments.of(
                        testBuilder.withId(3L)
                        .withName("Jingles")
                        .withQuantity(41)
                        .withPromotion(false).
                        build()
                )
        );
    }

}