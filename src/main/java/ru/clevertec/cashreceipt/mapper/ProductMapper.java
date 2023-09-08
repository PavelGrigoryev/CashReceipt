package ru.clevertec.cashreceipt.mapper;

import ru.clevertec.cashreceipt.dto.ProductDto;
import ru.clevertec.cashreceipt.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductDto toProductDto(Product product);

    Product fromProductDto(ProductDto productDto);

    List<ProductDto> toProductDtoList(List<Product> products);

}
