package ru.clevertec.cashreceipt.cache.factory;

import ru.clevertec.cashreceipt.cache.Cache;
import ru.clevertec.cashreceipt.cache.LFUCache;
import ru.clevertec.cashreceipt.cache.LRUCache;
import ru.clevertec.cashreceipt.config.YamlConfig;

public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {

    @Override
    public Cache<K, V> createCache() {
        YamlConfig yamlConfig = new YamlConfig();
        return "LRU".equals(yamlConfig.getAlgorithm())
                ? new LRUCache<>(yamlConfig.getCapacity())
                : new LFUCache<>(yamlConfig.getCapacity());
    }

}
