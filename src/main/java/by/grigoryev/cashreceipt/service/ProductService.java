package by.grigoryev.cashreceipt.service;

import by.grigoryev.cashreceipt.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product findById(Long id);

    Product save(Product product);

    Product update(Long id, Integer quantity);

}
