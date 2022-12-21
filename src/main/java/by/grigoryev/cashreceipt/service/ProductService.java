package by.grigoryev.cashreceipt.service;

import by.grigoryev.cashreceipt.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> findAll();

    ProductDto findById(Long id);

    ProductDto save(ProductDto productDto);

    ProductDto update(Long id, Integer quantity);

}
