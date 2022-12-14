package by.grigoryev.cashreceipt.service.impl;

import by.grigoryev.cashreceipt.dto.ProductDto;
import by.grigoryev.cashreceipt.exception.NoSuchProductException;
import by.grigoryev.cashreceipt.mapper.ProductMapper;
import by.grigoryev.cashreceipt.model.Product;
import by.grigoryev.cashreceipt.repository.ProductRepository;
import by.grigoryev.cashreceipt.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Override
    public List<ProductDto> findAll() {
        List<ProductDto> productDtoList = productRepository.findAll()
                .stream()
                .map(productMapper::toProductDto)
                .toList();
        log.info("findAll {}", productDtoList);
        return productDtoList;
    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchProductException("Product with ID " + id + " does not exist"));
        ProductDto productDto = productMapper.toProductDto(product);
        log.info("findById {}", productDto);
        return productDto;
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        Product product = productMapper.fromProductDto(productDto);
        Product savedProduct = productRepository.save(createProduct(product));
        ProductDto savedProductDto = productMapper.toProductDto(savedProduct);
        log.info("save {}", savedProductDto);
        return savedProductDto;
    }

    @Override
    public ProductDto update(Long id, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchProductException("Product with ID " + id + " does not exist"));

        if (!quantity.equals(product.getQuantity())) {
            product.setId(id);
            product.setQuantity(quantity);
            product.setTotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

            Product updatedProduct = productRepository.save(product);
            ProductDto productDto = productMapper.toProductDto(updatedProduct);
            log.info("update {}", productDto);
            return productDto;
        } else {
            ProductDto productDto = productMapper.toProductDto(product);
            log.info("no update {}", productDto);
            return productDto;
        }

    }

    private Product createProduct(Product product) {
        return Product.builder()
                .id(product.getId())
                .quantity(product.getQuantity())
                .name(product.getName())
                .price(product.getPrice())
                .total(product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .promotion(product.getPromotion())
                .build();
    }

}
