package ru.clevertec.cashreceipt.util.testbuilder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.cashreceipt.model.Product;
import ru.clevertec.cashreceipt.util.TestBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aProduct")
@With
public class ProductTestBuilder implements TestBuilder<Product> {

    private Long id = 1L;
    private Integer quantity = 3;
    private String name = "Samovar";
    private BigDecimal price = BigDecimal.valueOf(256.24);
    private Boolean promotion = true;

    @Override
    public Product build() {
        return Product.builder()
                .id(id)
                .quantity(quantity)
                .name(name)
                .price(price)
                .total(price.multiply(BigDecimal.valueOf(quantity)))
                .promotion(promotion)
                .build();
    }

}
