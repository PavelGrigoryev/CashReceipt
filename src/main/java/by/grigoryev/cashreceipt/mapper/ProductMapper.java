package by.grigoryev.cashreceipt.mapper;

import by.grigoryev.cashreceipt.dto.ProductDto;
import by.grigoryev.cashreceipt.model.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {

    ProductDto toProductDto(Product product);

    Product fromProductDto(ProductDto productDto);

}
