package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.dto.DiscountCardDto;
import by.grigoryev.cashreceipt.exception.NoSuchDiscountCardException;
import by.grigoryev.cashreceipt.mapper.DiscountCardMapper;
import by.grigoryev.cashreceipt.model.DiscountCard;
import by.grigoryev.cashreceipt.repository.DiscountCardRepository;
import by.grigoryev.cashreceipt.service.DiscountCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountCardServiceImpl implements DiscountCardService {

    private final DiscountCardRepository discountCardRepository;

    private final DiscountCardMapper discountCardMapper = Mappers.getMapper(DiscountCardMapper.class);


    @Override
    public List<DiscountCardDto> findAll() {
        List<DiscountCardDto> discountCardDtoList =
                discountCardMapper.toDiscountCardDtoList(discountCardRepository.findAll());
        log.info("findAll {}", discountCardDtoList);
        return discountCardDtoList;
    }

    @Override
    public DiscountCardDto findById(Long id) {
        DiscountCard discountCard = discountCardRepository.findById(id)
                .orElseThrow(() -> new NoSuchDiscountCardException("DiscountCard with ID " + id + " does not exist"));
        DiscountCardDto discountCardDto = discountCardMapper.toDiscountCardDto(discountCard);
        log.info("findById {}", discountCardDto);
        return discountCardDto;
    }

    @Override
    public DiscountCardDto save(DiscountCardDto discountCardDto) {
        DiscountCard discountCard = discountCardMapper.fromDiscountCardDto(discountCardDto);
        DiscountCard savedDiscountCard = discountCardRepository.save(discountCard);
        DiscountCardDto savedDiscountCardDto = discountCardMapper.toDiscountCardDto(savedDiscountCard);
        log.info("save {}", savedDiscountCardDto);
        return savedDiscountCardDto;
    }

    @Override
    public DiscountCardDto findByDiscountCardNumber(String discountCardNumber) {
        DiscountCard discountCard = discountCardRepository.findByDiscountCardNumber(discountCardNumber)
                .orElseThrow(() -> new NoSuchDiscountCardException("DiscountCard with card number " +
                                                                   discountCardNumber + " does not exist"));
        DiscountCardDto discountCardDto = discountCardMapper.toDiscountCardDto(discountCard);
        log.info("findByDiscountCardNumber {}", discountCardDto);
        return discountCardDto;
    }

}
