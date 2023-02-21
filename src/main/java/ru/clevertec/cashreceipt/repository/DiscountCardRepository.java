package ru.clevertec.cashreceipt.repository;

import ru.clevertec.cashreceipt.model.DiscountCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountCardRepository extends JpaRepository<DiscountCard, Long> {

    Optional<DiscountCard> findByDiscountCardNumber(String discountCardNumber);

}
