package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.model.DiscountCard;
import by.grigoryev.cashreceipt.repository.DiscountCardRepository;
import by.grigoryev.cashreceipt.service.DiscountCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<DiscountCard> optionalDiscountCard = discountCardRepository.findById(id);

        if (optionalDiscountCard.isPresent()) {
            DiscountCard discountCard = optionalDiscountCard.get();
            log.info("findById {}", discountCard);
            return discountCard;
        } else {
            throw new RuntimeException("No id");
        }

    }

    @Override
    public DiscountCard save(DiscountCard discountCard) {
        DiscountCard savedDiscountCard = discountCardRepository.save(discountCard);
        log.info("save {}", savedDiscountCard);
        return savedDiscountCard;
    }

    @Override
    public DiscountCard findByDiscountCardNumber(String discountCardNumber) {
        Optional<DiscountCard> optionalDiscountCard = discountCardRepository.findByDiscountCardNumber(discountCardNumber);

        if (optionalDiscountCard.isPresent()) {
            DiscountCard discountCard = optionalDiscountCard.get();
            log.info("findByDiscountCardNumber {}", discountCard);
            return discountCard;
        } else {
            throw new RuntimeException("No card number");
        }
    }

}
