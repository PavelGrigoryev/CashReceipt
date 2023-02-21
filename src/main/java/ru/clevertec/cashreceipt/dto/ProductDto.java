package ru.clevertec.cashreceipt.dto;

import java.math.BigDecimal;

public record ProductDto(
        Long id,
        Integer quantity,
        String name,
        BigDecimal price,
        BigDecimal total,
        Boolean promotion
) {
}
