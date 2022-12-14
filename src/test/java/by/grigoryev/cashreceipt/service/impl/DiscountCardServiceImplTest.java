package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.dto.DiscountCardDto;
import by.grigoryev.cashreceipt.exception.NoSuchDiscountCardException;
import by.grigoryev.cashreceipt.mapper.DiscountCardMapper;
import by.grigoryev.cashreceipt.model.DiscountCard;
import by.grigoryev.cashreceipt.repository.DiscountCardRepository;
import by.grigoryev.cashreceipt.service.DiscountCardService;
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

class DiscountCardServiceImplTest {

    public static final long ID = 1L;
    public static final String DISCOUNT_CARD_NUMBER = "1234";
    public static final BigDecimal DISCOUNT_PERCENTAGE = BigDecimal.valueOf(3);

    private DiscountCardService discountCardService;
    private DiscountCardRepository discountCardRepository;
    private final DiscountCardMapper discountCardMapper = Mappers.getMapper(DiscountCardMapper.class);

    @BeforeEach
    void setUp() {
        discountCardRepository = mock(DiscountCardRepository.class);
        discountCardService = spy(new DiscountCardServiceImpl(discountCardRepository));
    }

    @Test
    @DisplayName("testing findAll method")
    void findAll() {
        DiscountCard discountCard = getMockedDiscountCard();

        doReturn(List.of(discountCard)).when(discountCardRepository).findAll();
        List<DiscountCard> discountCards = discountCardService.findAll()
                .stream()
                .map(discountCardMapper::fromDiscountCardDto)
                .toList();
        assertEquals(1, discountCards.size());
        assertEquals(discountCard, discountCards.get(0));
    }

    @Test
    @DisplayName("testing if exception throws when DiscountCard is not found by id")
    void findByIdThrowsException() {
        doThrow(new NoSuchDiscountCardException("DiscountCard with ID " + ID + " does not exist"))
                .when(discountCardRepository).findById(ID);

        Exception exception = assertThrows(NoSuchDiscountCardException.class, () -> discountCardService.findById(ID));

        String expectedMessage = "DiscountCard with ID " + ID + " does not exist";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("testing if DiscountCard returns when it is found by id")
    void findByIdReturnsDiscountCard() {
        DiscountCard mockedDiscountCard = getMockedDiscountCard();
        doReturn(Optional.of(mockedDiscountCard))
                .when(discountCardRepository).findById(ID);

        DiscountCardDto discountCardDto = discountCardService.findById(ID);
        DiscountCard discountCard = discountCardMapper.fromDiscountCardDto(discountCardDto);

        assertEquals(mockedDiscountCard, discountCard);
    }

    @Test
    @DisplayName("testing save method")
    void save() {
        DiscountCard mockedDiscountCard = getMockedDiscountCard();
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(discountCardRepository)
                .save(any(DiscountCard.class));

        DiscountCardDto discountCardDto = discountCardService
                .save(discountCardMapper.toDiscountCardDto(getMockedDiscountCard()));
        DiscountCard discountCard = discountCardMapper.fromDiscountCardDto(discountCardDto);

        assertEquals(mockedDiscountCard, discountCard);
    }

    @Test
    @DisplayName("testing if exception throws when DiscountCard is not found by card number")
    void findByDiscountCardNumberThrowException() {
        doThrow(new NoSuchDiscountCardException("DiscountCard with card number "
                + DISCOUNT_CARD_NUMBER + " does not exist"))
                .when(discountCardRepository).findByDiscountCardNumber(DISCOUNT_CARD_NUMBER);

        Exception exception = assertThrows(NoSuchDiscountCardException.class, () -> discountCardService
                .findByDiscountCardNumber(DISCOUNT_CARD_NUMBER));

        String expectedMessage = "DiscountCard with card number " + DISCOUNT_CARD_NUMBER + " does not exist";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("testing if DiscountCard returns when it is found by card number")
    void findByDiscountCardNumberReturnsDiscountCard() {
        DiscountCard mockedDiscountCard = getMockedDiscountCard();
        doReturn(Optional.of(mockedDiscountCard))
                .when(discountCardRepository).findByDiscountCardNumber(DISCOUNT_CARD_NUMBER);

        DiscountCardDto discountCardDto = discountCardService.findByDiscountCardNumber(DISCOUNT_CARD_NUMBER);
        DiscountCard discountCard = discountCardMapper.fromDiscountCardDto(discountCardDto);

        assertEquals(mockedDiscountCard, discountCard);
    }

    private DiscountCard getMockedDiscountCard() {
        return DiscountCard.builder()
                .id(ID)
                .discountCardNumber(DISCOUNT_CARD_NUMBER)
                .discountPercentage(DISCOUNT_PERCENTAGE)
                .build();
    }

}