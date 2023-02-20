package by.grigoryev.cashreceipt.util.testbuilder;

import by.grigoryev.cashreceipt.model.DiscountCard;
import by.grigoryev.cashreceipt.util.TestBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aDiscountCard")
@With
public class DiscountCardTestBuilder implements TestBuilder<DiscountCard> {

    private Long id = 1L;

    private String discountCardNumber = "1234";

    private BigDecimal discountPercentage = BigDecimal.valueOf(3);

    @Override
    public DiscountCard build() {
        return DiscountCard.builder()
                .id(id)
                .discountCardNumber(discountCardNumber)
                .discountPercentage(discountPercentage)
                .build();
    }
}
