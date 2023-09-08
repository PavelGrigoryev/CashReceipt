package ru.clevertec.cashreceipt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "discount_card")
public class DiscountCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "\\d{4}", message = "Discount card number must be 4 digits")
    @Column(name = "discount_card_number")
    private String discountCardNumber;

    @DecimalMin(value = "0.01", message = "Discount percentage must be greater than or equal to 0.01")
    @DecimalMax(value = "100.00", message = "Discount percentage must be less than or equal to 100.00")
    @Column(name = "discount_percentage")
    private BigDecimal discountPercentage;

}
