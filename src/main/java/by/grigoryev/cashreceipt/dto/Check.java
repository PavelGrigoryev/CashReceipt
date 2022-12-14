package by.grigoryev.cashreceipt.dto;

import by.grigoryev.cashreceipt.model.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Check {

    private String name;

    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime time;

    private List<Product> products;

    private BigDecimal totalSum;

    private BigDecimal discountPercentage;

    private BigDecimal discount;

    private BigDecimal totalSumWithDiscount;

}
