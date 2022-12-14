package by.grigoryev.cashreceipt.repository;

import by.grigoryev.cashreceipt.model.DiscountCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountCardRepository extends JpaRepository<DiscountCard, Long> {

    Optional<DiscountCard> findByDiscountCardNumber(String discountCardNumber);

}
