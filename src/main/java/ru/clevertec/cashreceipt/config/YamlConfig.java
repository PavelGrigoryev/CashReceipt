package ru.clevertec.cashreceipt.config;

import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Getter
public class YamlConfig {

    private static final String FILENAME = "application.yaml";
    private static final String ALGORITHM_KEY = "algorithm";
    private static final String CAPACITY_KEY = "capacity";

    private final String algorithm;
    private final int capacity;

    public YamlConfig() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(FILENAME);

        Map<String, Object> map = yaml.load(inputStream);
        algorithm = (String) map.get(ALGORITHM_KEY);
        capacity = (int) map.get(CAPACITY_KEY);
    }

}
