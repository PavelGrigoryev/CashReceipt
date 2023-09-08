package ru.clevertec.cashreceipt.service.proxy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.clevertec.cashreceipt.cache.Cache;
import ru.clevertec.cashreceipt.cache.factory.CacheFactory;
import ru.clevertec.cashreceipt.cache.factory.CacheFactoryImpl;
import ru.clevertec.cashreceipt.dto.DiscountCardDto;
import ru.clevertec.cashreceipt.service.DiscountCardService;

import java.util.List;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class DiscountCardServiceProxy implements DiscountCardService {

    private final DiscountCardService discountCardService;
    private final CacheFactory<Long, DiscountCardDto> cacheFactory = new CacheFactoryImpl<>();
    private final Cache<Long, DiscountCardDto> cache = cacheFactory.createCache();

    @Override
    public List<DiscountCardDto> findAll() {
        return discountCardService.findAll();
    }

    @Override
    public DiscountCardDto findById(Long id) {
        DiscountCardDto cacheDiscountCartDto = cache.get(id);
        if (cacheDiscountCartDto == null) {
            DiscountCardDto discountCardDto = discountCardService.findById(id);
            cache.put(discountCardDto.id(), discountCardDto);
            return discountCardDto;
        }
        log.info("findById in cache {}", cache);
        return cacheDiscountCartDto;
    }

    @Override
    public DiscountCardDto save(DiscountCardDto discountCardDto) {
        DiscountCardDto savedDiscountCardDto = discountCardService.save(discountCardDto);
        cache.put(savedDiscountCardDto.id(), savedDiscountCardDto);
        log.info("save in cache {}", cache);
        return savedDiscountCardDto;
    }

    @Override
    public DiscountCardDto findByDiscountCardNumber(String discountCardNumber) {
        DiscountCardDto discountCardDto = discountCardService.findByDiscountCardNumber(discountCardNumber);
        DiscountCardDto cacheDiscountCardDto = cache.get(discountCardDto.id());
        if (cacheDiscountCardDto == null) {
            cache.put(discountCardDto.id(), discountCardDto);
        }
        return discountCardDto;
    }

    @Override
    public void deleteById(Long id) {
        discountCardService.deleteById(id);
        cache.removeByKey(id);
        log.info("deleteById in cache {}", cache);
    }

}
