package ru.clevertec.cashreceipt.service.impl;

import ru.clevertec.cashreceipt.dto.ProductDto;
import ru.clevertec.cashreceipt.exception.NoSuchProductException;
import ru.clevertec.cashreceipt.mapper.ProductMapper;
import ru.clevertec.cashreceipt.model.Product;
import ru.clevertec.cashreceipt.repository.ProductRepository;
import ru.clevertec.cashreceipt.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Override
    public List<ProductDto> findAll() {
        List<ProductDto> productDtoList = productMapper.toProductDtoList(productRepository.findAll());
        productDtoList.sort(Comparator.comparing(ProductDto::id));
        log.info("findAll {}", productDtoList);
        return productDtoList;
    }

    @Override
    public ProductDto findById(Long id) {
        ProductDto productDto = productRepository.findById(id)
                .map(productMapper::toProductDto)
                .orElseThrow(() -> new NoSuchProductException("Product with ID " + id + " does not exist"));
        log.info("findById {}", productDto);
        return productDto;
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        ProductDto savedProductDto = productMapper.toProductDto(productRepository
                .save(createProduct(productMapper.fromProductDto(productDto))));
        log.info("save {}", savedProductDto);
        return savedProductDto;
    }

    @Override
    public ProductDto update(Long id, Integer quantity) {
        return productRepository.findById(id)
                .map(product -> {
                    if (!quantity.equals(product.getQuantity())) {
                        product.setQuantity(quantity);
                        product.setTotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
                        Product updatedProduct = productRepository.save(product);
                        log.info("update {}", updatedProduct);
                        return productMapper.toProductDto(updatedProduct);
                    } else {
                        ProductDto productDto = productMapper.toProductDto(product);
                        log.info("no update {}", productDto);
                        return productDto;
                    }
                })
                .orElseThrow(() -> new NoSuchProductException("Product with ID " + id + " does not exist"));
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchProductException("No product with ID " + id + " to delete"));
        log.info("deleteById {}", product);
        productRepository.deleteById(id);
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
