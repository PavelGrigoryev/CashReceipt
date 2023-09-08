package ru.clevertec.cashreceipt.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.cashreceipt.model.DiscountCard;
import ru.clevertec.cashreceipt.util.testbuilder.DiscountCardTestBuilder;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class LFUCacheTest {

    private Cache<Integer, DiscountCard> cache;
    private static final DiscountCardTestBuilder testBuilder = DiscountCardTestBuilder.aDiscountCard();

    @BeforeEach
    void setUp() {
        cache = new LFUCache<>(3);
        cache.put(1, testBuilder.build());
        cache.put(2, testBuilder.withId(2L)
                .withDiscountCardNumber("5678")
                .withDiscountPercentage(BigDecimal.valueOf(7.5))
                .build());
        cache.put(3, testBuilder.withId(3L)
                .withDiscountCardNumber("9876")
                .withDiscountPercentage(BigDecimal.TEN).
                build());
    }

    @Test
    @DisplayName("check get method should return value by key")
    void checkGetMethodShouldExpectedReturnValueByKey() {
        DiscountCard expectedValue = testBuilder.build();

        DiscountCard actualValue = cache.get(1);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("check get method should return null when cache does not contain a value by key")
    void checkGetMethodShouldReturnNull() {
        DiscountCard actualValue = cache.get(4);

        assertThat(actualValue).isNull();
    }

    @Test
    @DisplayName("check expected key should be removed from all maps")
    void checkExpectedKeyShouldBeRemovedFromAllMaps() {
        DiscountCard expectedValue = testBuilder.withId(4L).build();
        cache.put(4, expectedValue);

        assertThat(cache.get(1)).isNull();
        assertThat(cache.get(4)).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("check cache should remove value with less frequency")
    void checkCacheShouldRemoveValueWithLessFrequency() {
        cache.get(1);
        cache.get(2);
        cache.put(4, testBuilder.withId(4L).build());

        assertThat(cache.get(3)).isNull();

        cache.put(5, testBuilder.withId(5L).build());

        assertThat(cache.get(4)).isNull();
    }

    @Test
    @DisplayName("check put method should return deleted value if cache contains expected key")
    void checkPutMethodShouldReturnDeletedValueIfCacheContainsExpectedKey() {
        DiscountCard expectedValue = testBuilder.build();

        DiscountCard actualValue = cache.put(1, testBuilder.withId(5L).build());

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("check put method should return null if capacity of cache <= 0")
    void checkPutMethodShouldReturnNullIfCapacityIsLessOrEqualZero() {
        cache = new LFUCache<>(0);
        DiscountCard actualValue = cache.put(1, testBuilder.build());

        assertThat(actualValue).isNull();
    }

    @Test
    @DisplayName("check that minFrequency in get method is incremented by 1")
    void checkMinFrequencyShouldIncrementInGetMethod() {
        cache.get(1);
        cache.get(2);
        cache.get(3);

        assertThat(cache.toString()).endsWith("=2)");
    }

    @Test
    @DisplayName("check toString method starts with expected value")
    void checkToStringMethodStartsWithExpectedValue() {
        String actualValue = cache.toString();

        assertThat(actualValue).startsWith("LFUCache(capacity=3");
    }

    @Test
    @DisplayName("check removeByKey method should return removed value")
    void checkRemoveByKeyShouldReturnRemovedValue() {
        DiscountCard expectedValue = testBuilder.build();

        DiscountCard actualValue = cache.removeByKey(1);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("check removeByKey method should return null when cache does not contain a value by key")
    void checkRemoveByKeyMethodShouldReturnNull() {
        DiscountCard actualValue = cache.removeByKey(4);

        assertThat(actualValue).isNull();
    }

    @Test
    @DisplayName("check that minFrequency in removeByKey method is incremented by 1")
    void checkMinFrequencyShouldIncrementInRemoveByKeyMethod() {
        cache.removeByKey(1);
        cache.removeByKey(2);
        cache.removeByKey(3);

        assertThat(cache.toString()).endsWith("=2)");
    }

}
