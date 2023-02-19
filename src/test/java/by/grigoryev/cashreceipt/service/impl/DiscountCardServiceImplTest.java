package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.dto.DiscountCardDto;
import by.grigoryev.cashreceipt.exception.NoSuchDiscountCardException;
import by.grigoryev.cashreceipt.mapper.DiscountCardMapper;
import by.grigoryev.cashreceipt.model.DiscountCard;
import by.grigoryev.cashreceipt.repository.DiscountCardRepository;
import by.grigoryev.cashreceipt.service.DiscountCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DiscountCardServiceImplTest {

    private static final Long ID = 1L;
    private static final String DISCOUNT_CARD_NUMBER = "1234";
    private static final BigDecimal DISCOUNT_PERCENTAGE = BigDecimal.valueOf(3);

    private DiscountCardService discountCardService;
    private DiscountCardRepository discountCardRepository;
    private final DiscountCardMapper discountCardMapper = Mappers.getMapper(DiscountCardMapper.class);

    @BeforeEach
    void setUp() {
        discountCardRepository = mock(DiscountCardRepository.class);
        discountCardService = spy(new DiscountCardServiceImpl(discountCardRepository));
    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test should return List of size 1")
        void testFindAllShouldReturnListOfSizeOne() {
            DiscountCard mockedDiscountCard = getMockedDiscountCard();
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
            DiscountCard mockedDiscountCard = getMockedDiscountCard();
            List<DiscountCardDto> expectedValues = discountCardMapper.toDiscountCardDtoList(List.of(mockedDiscountCard));

            doReturn(List.of(mockedDiscountCard))
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
            DiscountCard mockedDiscountCard = getMockedDiscountCard();
            long wrongId = 2L;

            doReturn(Optional.of(mockedDiscountCard))
                    .when(discountCardRepository)
                    .findById(ID);

            assertThrows(NoSuchDiscountCardException.class, () -> discountCardService.findById(wrongId));
        }

        @Test
        @DisplayName("test throw NoSuchDiscountCardException with expected message")
        void testFindByIdThrowNoSuchDiscountCardExceptionWithExpectedMessage() {
            String expectedMessage = "DiscountCard with ID " + ID + " does not exist";

            Exception exception = assertThrows(NoSuchDiscountCardException.class, () -> discountCardService.findById(ID));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected DiscountCardDto")
        void testFindByIdShouldReturnExpectedDiscountCardDto() {
            DiscountCard mockedDiscountCard = getMockedDiscountCard();
            DiscountCardDto expectedValue = discountCardMapper.toDiscountCardDto(mockedDiscountCard);

            doReturn(Optional.of(mockedDiscountCard))
                    .when(discountCardRepository)
                    .findById(ID);

            DiscountCardDto actualValue = discountCardService.findById(ID);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class SaveTest {

        @Test
        @DisplayName("test should return expected DiscountCardDto")
        void testSaveShouldReturnExpectedDiscountCardDto() {
            DiscountCard mockedDiscountCard = getMockedDiscountCard();
            DiscountCardDto expectedValue = discountCardMapper.toDiscountCardDto(mockedDiscountCard);

            doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                    .when(discountCardRepository)
                    .save(any(DiscountCard.class));

            DiscountCardDto actualValue = discountCardService
                    .save(discountCardMapper.toDiscountCardDto(getMockedDiscountCard()));

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindByDiscountCardNumberTest {

        @Test
        @DisplayName("test throw NoSuchDiscountCardException")
        void testFindByDiscountCardNumberThrowNoSuchDiscountCardException() {
            DiscountCard mockedDiscountCard = getMockedDiscountCard();
            String wrongDiscountCardNumber = "2636";

            doReturn(Optional.of(mockedDiscountCard))
                    .when(discountCardRepository)
                    .findByDiscountCardNumber(DISCOUNT_CARD_NUMBER);

            assertThrows(NoSuchDiscountCardException.class,
                    () -> discountCardService.findByDiscountCardNumber(wrongDiscountCardNumber));
        }

        @Test
        @DisplayName("test throw NoSuchDiscountCardException with expected message")
        void testFindByDiscountCardNumberThrowNoSuchDiscountCardExceptionWithExpectedMessage() {
            String expectedMessage = "DiscountCard with card number " + DISCOUNT_CARD_NUMBER + " does not exist";

            Exception exception = assertThrows(NoSuchDiscountCardException.class,
                    () -> discountCardService.findByDiscountCardNumber(DISCOUNT_CARD_NUMBER));

            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected DiscountCardDto")
        void testFindByDiscountCardNumberShouldReturnExpectedDiscountCardDto() {
            DiscountCard mockedDiscountCard = getMockedDiscountCard();
            DiscountCardDto expectedValue = discountCardMapper.toDiscountCardDto(mockedDiscountCard);

            doReturn(Optional.of(mockedDiscountCard))
                    .when(discountCardRepository).findByDiscountCardNumber(DISCOUNT_CARD_NUMBER);

            DiscountCardDto actualValue = discountCardService.findByDiscountCardNumber(DISCOUNT_CARD_NUMBER);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    private DiscountCard getMockedDiscountCard() {
        return DiscountCard.builder()
                .id(ID)
                .discountCardNumber(DISCOUNT_CARD_NUMBER)
                .discountPercentage(DISCOUNT_PERCENTAGE)
                .build();
    }

}