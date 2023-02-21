package ru.clevertec.cashreceipt.service.impl;

import ru.clevertec.cashreceipt.dto.DiscountCardDto;
import ru.clevertec.cashreceipt.exception.NoSuchDiscountCardException;
import ru.clevertec.cashreceipt.mapper.DiscountCardMapper;
import ru.clevertec.cashreceipt.model.DiscountCard;
import ru.clevertec.cashreceipt.repository.DiscountCardRepository;
import ru.clevertec.cashreceipt.service.DiscountCardService;
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
        DiscountCardDto discountCardDto = discountCardMapper.toDiscountCardDto(discountCardRepository.findById(id)
                .orElseThrow(() -> new NoSuchDiscountCardException("DiscountCard with ID " + id + " does not exist")));
        log.info("findById {}", discountCardDto);
        return discountCardDto;
    }

    @Override
    public DiscountCardDto save(DiscountCardDto discountCardDto) {
        DiscountCardDto savedDiscountCardDto = discountCardMapper
                .toDiscountCardDto(discountCardRepository.save(discountCardMapper.fromDiscountCardDto(discountCardDto)));
        log.info("save {}", savedDiscountCardDto);
        return savedDiscountCardDto;
    }

    @Override
    public DiscountCardDto findByDiscountCardNumber(String discountCardNumber) {
        DiscountCardDto discountCardDto = discountCardMapper.toDiscountCardDto(
                discountCardRepository.findByDiscountCardNumber(discountCardNumber)
                        .orElseThrow(() -> new NoSuchDiscountCardException("DiscountCard with card number " +
                                                                           discountCardNumber + " does not exist"))
        );
        log.info("findByDiscountCardNumber {}", discountCardDto);
        return discountCardDto;
    }

    @Override
    public void deleteById(Long id) {
        DiscountCard discountCard = discountCardRepository.findById(id)
                .orElseThrow(() -> new NoSuchDiscountCardException("No discount card with ID " + id + " to delete"));
        log.info("deleteById {}", discountCard);
        discountCardRepository.deleteById(id);
    }

}
