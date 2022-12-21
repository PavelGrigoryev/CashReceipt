package by.grigoryev.cashreceipt.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DiscountCardDto {

    private Long id;

    private String discountCardNumber;

    private BigDecimal discountPercentage;

}
