package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.exception.NoSuchDiscountCardException;
import by.grigoryev.cashreceipt.model.DiscountCard;
import by.grigoryev.cashreceipt.repository.DiscountCardRepository;
import by.grigoryev.cashreceipt.service.DiscountCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountCardServiceImpl implements DiscountCardService {

    private final DiscountCardRepository discountCardRepository;

    @Override
    public List<DiscountCard> findAll() {
        List<DiscountCard> discountCards = discountCardRepository.findAll();
        log.info("findAll {}", discountCards);
        return discountCards;
    }

    @Override
    public DiscountCard findById(Long id) {
        DiscountCard discountCard = discountCardRepository.findById(id)
                .orElseThrow(() -> new NoSuchDiscountCardException("DiscountCard with ID " + id + " does not exist"));
        log.info("findById {}", discountCard);
        return discountCard;
    }

    @Override
    public DiscountCard save(DiscountCard discountCard) {
        DiscountCard savedDiscountCard = discountCardRepository.save(discountCard);
        log.info("save {}", savedDiscountCard);
        return savedDiscountCard;
    }

    @Override
    public DiscountCard findByDiscountCardNumber(String discountCardNumber) {
        DiscountCard discountCard = discountCardRepository.findByDiscountCardNumber(discountCardNumber)
                .orElseThrow(() -> new NoSuchDiscountCardException("DiscountCard with card number " +
                        discountCardNumber + " does not exist"));
        log.info("findByDiscountCardNumber {}", discountCard);
        return discountCard;
    }

}
