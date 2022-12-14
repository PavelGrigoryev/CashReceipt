package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.model.Product;
import by.grigoryev.cashreceipt.repository.ProductRepository;
import by.grigoryev.cashreceipt.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();
        log.info("findAll {}", products);
        return products;
    }

    @Override
    public Product findById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            log.info("findById {}", product);
            return product;
        } else {
            throw new RuntimeException("No id");
        }

    }

    @Override
    public Product save(Product product) {
        Product savedProduct = Product.builder()
                .quantity(product.getQuantity())
                .name(product.getName())
                .price(product.getPrice())
                .total(product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .promotion(product.getPromotion())
                .build();

        productRepository.save(savedProduct);
        log.info("save {}", savedProduct);
        return savedProduct;
    }

    @Override
    public Product update(Long id, Integer quantity) {
        Product product = findById(id);

        if (!quantity.equals(product.getQuantity())) {
            product.setId(id);
            product.setQuantity(quantity);
            product.setTotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

            Product updatedProduct = productRepository.save(product);
            log.info("update {}", updatedProduct);
            return updatedProduct;
        } else {
            log.info("no update {}", product);
            return product;
        }

    }

}
