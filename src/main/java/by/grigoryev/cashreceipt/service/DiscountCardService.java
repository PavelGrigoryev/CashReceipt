package by.grigoryev.cashreceipt.service;

import by.grigoryev.cashreceipt.dto.DiscountCardDto;

import java.util.List;

public interface DiscountCardService {

    List<DiscountCardDto> findAll();

    DiscountCardDto findById(Long id);

    DiscountCardDto save(DiscountCardDto discountCardDto);

    DiscountCardDto findByDiscountCardNumber(String discountCardNumber);

    void deleteById(Long id);

}
