package ru.clevertec.cashreceipt.service.proxy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.clevertec.cashreceipt.cache.Cache;
import ru.clevertec.cashreceipt.cache.factory.CacheFactory;
import ru.clevertec.cashreceipt.cache.factory.CacheFactoryImpl;
import ru.clevertec.cashreceipt.dto.ProductDto;
import ru.clevertec.cashreceipt.service.ProductService;

import java.util.List;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class ProductServiceProxy implements ProductService {

    private final ProductService productService;
    private final CacheFactory<Long, ProductDto> cacheFactory = new CacheFactoryImpl<>();
    private final Cache<Long, ProductDto> cache = cacheFactory.createCache();

    @Override
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    @Override
    public ProductDto findById(Long id) {
        ProductDto cacheProductDto = cache.get(id);
        if (cacheProductDto == null) {
            ProductDto productDto = productService.findById(id);
            cache.put(productDto.id(), productDto);
            return productDto;
        }
        log.info("findById in cache {}", cache);
        return cacheProductDto;
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        ProductDto savedProductDto = productService.save(productDto);
        cache.put(savedProductDto.id(), savedProductDto);
        log.info("save in cache {}", cache);
        return savedProductDto;
    }

    @Override
    public ProductDto update(Long id, Integer quantity) {
        ProductDto productDto = productService.update(id, quantity);
        cache.put(productDto.id(), productDto);
        log.info("update in cache {}", cache);
        return productDto;
    }

    @Override
    public void deleteById(Long id) {
        productService.deleteById(id);
        cache.removeByKey(id);
        log.info("deleteById in cache {}", cache);
    }

}
