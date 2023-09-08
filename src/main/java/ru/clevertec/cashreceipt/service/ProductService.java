package ru.clevertec.cashreceipt.service;

import ru.clevertec.cashreceipt.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> findAll();

    ProductDto findById(Long id);

    ProductDto save(ProductDto productDto);

    ProductDto update(Long id, Integer quantity);

    void deleteById(Long id);

}
