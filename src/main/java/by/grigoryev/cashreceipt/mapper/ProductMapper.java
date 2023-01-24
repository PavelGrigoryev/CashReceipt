package by.grigoryev.cashreceipt.mapper;

import by.grigoryev.cashreceipt.dto.ProductDto;
import by.grigoryev.cashreceipt.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductDto toProductDto(Product product);

    Product fromProductDto(ProductDto productDto);

    List<ProductDto> toProductDtoList(List<Product> products);

}
