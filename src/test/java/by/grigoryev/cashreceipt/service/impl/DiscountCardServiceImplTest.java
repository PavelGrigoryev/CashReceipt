package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.dto.DiscountCardDto;
import by.grigoryev.cashreceipt.exception.NoSuchDiscountCardException;
import by.grigoryev.cashreceipt.mapper.DiscountCardMapper;
import by.grigoryev.cashreceipt.model.DiscountCard;
import by.grigoryev.cashreceipt.repository.DiscountCardRepository;
import by.grigoryev.cashreceipt.service.DiscountCardService;
import by.grigoryev.cashreceipt.util.testbuilder.DiscountCardTestBuilder;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountCardServiceImplTest {

    @Spy
    private DiscountCardService discountCardService;
    @Mock
    private DiscountCardRepository discountCardRepository;
    private final DiscountCardMapper discountCardMapper = Mappers.getMapper(DiscountCardMapper.class);
    private static final DiscountCardTestBuilder testBuilder = DiscountCardTestBuilder.aDiscountCard();
    @Captor
    private ArgumentCaptor<DiscountCard> captor;

    @BeforeEach
    void setUp() {
        discountCardService = new DiscountCardServiceImpl(discountCardRepository);
    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test should return List of size 1")
        void testFindAllShouldReturnListOfSizeOne() {
            DiscountCard mockedDiscountCard = testBuilder.build();
            int expectedSize = 1;

            doReturn(List.of(mockedDiscountCard))
                    .when(discountCardRepository)
                    .findAll();

            List<DiscountCardDto> actualValues = discountCardService.findAll();

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return expected List of DiscountCardDto")
        void testFindAllShouldReturnListOfDiscountCardDto() {
            DiscountCard mockedDiscountCard = testBuilder.build();
            List<DiscountCardDto> expectedValues = discountCardMapper.toDiscountCardDtoList(List.of(mockedDiscountCard));

            doReturn(List.of(mockedDiscountCard))
                    .when(discountCardRepository)
                    .findAll();

            List<DiscountCardDto> actualValues = discountCardService.findAll();

            assertThat(actualValues).isEqualTo(expectedValues);
        }

        @Test
        @DisplayName("test should return empty List")
        void testFindAllShouldReturnEmptyList() {
            List<DiscountCardDto> expectedValues = List.of();

            doReturn(List.of())
                    .when(discountCardRepository)
                    .findAll();

            List<DiscountCardDto> actualValues = discountCardService.findAll();

            assertThat(actualValues).isEqualTo(expectedValues);
        }

    }

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test throw NoSuchDiscountCardException")
        void testFindByIdThrowNoSuchDiscountCardException() {
            long id = 1;

            doThrow(new NoSuchDiscountCardException(""))
                    .when(discountCardRepository)
                    .findById(id);

            assertThrows(NoSuchDiscountCardException.class, () -> discountCardService.findById(id));
        }

        @Test
        @DisplayName("test throw NoSuchDiscountCardException with expected message")
        void testFindByIdThrowNoSuchDiscountCardExceptionWithExpectedMessage() {
            long id = 1;
            String expectedMessage = "DiscountCard with ID " + id + " does not exist";

            Exception exception = assertThrows(NoSuchDiscountCardException.class, () -> discountCardService.findById(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected DiscountCardDto")
        void testFindByIdShouldReturnExpectedDiscountCardDto() {
            DiscountCard mockedDiscountCard = testBuilder.build();
            long id = mockedDiscountCard.getId();
            DiscountCardDto expectedValue = discountCardMapper.toDiscountCardDto(mockedDiscountCard);

            doReturn(Optional.of(mockedDiscountCard))
                    .when(discountCardRepository)
                    .findById(id);

            DiscountCardDto actualValue = discountCardService.findById(id);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class SaveTest {

        @ParameterizedTest(name = "{arguments} test")
        @DisplayName("test should capture save value")
        @MethodSource("by.grigoryev.cashreceipt.service.impl.DiscountCardServiceImplTest#getArgumentsForSaveTest")
        void testSaveShouldCaptureValue(DiscountCard expectedValue) {
            doReturn(expectedValue)
                    .when(discountCardRepository)
                    .save(expectedValue);

            discountCardService.save(discountCardMapper.toDiscountCardDto(expectedValue));

            verify(discountCardRepository)
                    .save(captor.capture());

            DiscountCard captorValue = captor.getValue();

            assertThat(captorValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindByDiscountCardNumberTest {

        @Test
        @DisplayName("test throw NoSuchDiscountCardException")
        void testFindByDiscountCardNumberThrowNoSuchDiscountCardException() {
            String discountCardNumber = "1234";

            doThrow(new NoSuchDiscountCardException(""))
                    .when(discountCardRepository)
                    .findByDiscountCardNumber(discountCardNumber);

            assertThrows(NoSuchDiscountCardException.class,
                    () -> discountCardService.findByDiscountCardNumber(discountCardNumber));
        }

        @Test
        @DisplayName("test throw NoSuchDiscountCardException with expected message")
        void testFindByDiscountCardNumberThrowNoSuchDiscountCardExceptionWithExpectedMessage() {
            String discountCardNumber = "1234";
            String expectedMessage = "DiscountCard with card number " + discountCardNumber + " does not exist";

            Exception exception = assertThrows(NoSuchDiscountCardException.class,
                    () -> discountCardService.findByDiscountCardNumber(discountCardNumber));

            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected DiscountCardDto")
        void testFindByDiscountCardNumberShouldReturnExpectedDiscountCardDto() {
            DiscountCard mockedDiscountCard = testBuilder.build();
            String discountCardNumber = mockedDiscountCard.getDiscountCardNumber();
            DiscountCardDto expectedValue = discountCardMapper.toDiscountCardDto(mockedDiscountCard);

            doReturn(Optional.of(mockedDiscountCard))
                    .when(discountCardRepository).findByDiscountCardNumber(discountCardNumber);

            DiscountCardDto actualValue = discountCardService.findByDiscountCardNumber(discountCardNumber);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class DeleteTest {

        @Test
        @DisplayName("test should invoke method 1 time")
        void testDeleteById() {
            DiscountCard mockedDiscountCard = testBuilder.build();
            long id = mockedDiscountCard.getId();

            doReturn(Optional.of(mockedDiscountCard))
                    .when(discountCardRepository)
                    .findById(id);

            doNothing()
                    .when(discountCardRepository)
                    .deleteById(id);

            discountCardService.deleteById(id);

            verify(discountCardRepository, times(1))
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
                                .withDiscountCardNumber("9876")
                                .withDiscountPercentage(BigDecimal.TEN)
                                .build()
                ),
                Arguments.of(
                        testBuilder.withId(3L)
                                .withDiscountCardNumber("5566")
                                .withDiscountPercentage(BigDecimal.valueOf(7.5))
                                .build()
                )
        );
    }

}