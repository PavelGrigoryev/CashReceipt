package by.grigoryev.cashreceipt.service;

import by.grigoryev.cashreceipt.model.DiscountCard;

import java.util.List;

public interface DiscountCardService {

    List<DiscountCard> findAll();

    DiscountCard findById(Long id);

    DiscountCard save(DiscountCard discountCard);

    DiscountCard findByDiscountCardNumber(String discountCardNumber);

}
