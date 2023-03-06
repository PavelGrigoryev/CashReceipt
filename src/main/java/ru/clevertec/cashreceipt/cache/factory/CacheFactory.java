package ru.clevertec.cashreceipt.cache.factory;

import ru.clevertec.cashreceipt.cache.Cache;

public interface CacheFactory<K, V> {

    Cache<K, V> createCache();

}
