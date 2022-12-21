package by.grigoryev.cashreceipt.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDto {

    private Long id;

    private Integer quantity;

    private String name;

    private BigDecimal price;

    private BigDecimal total;

    private Boolean promotion;

}
