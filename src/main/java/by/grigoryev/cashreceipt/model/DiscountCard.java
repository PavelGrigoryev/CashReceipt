package by.grigoryev.cashreceipt.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "discount_card")
public class DiscountCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount_card_number")
    private String discountCardNumber;

    @Column(name = "discount_percentage")
    private BigDecimal discountPercentage;

}
