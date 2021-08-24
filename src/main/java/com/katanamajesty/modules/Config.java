package com.katanamajesty.modules;

import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class Config {

    ClassLoader classLoader = getClass().getClassLoader();
    static Map<String, Object> data = null;

    @SneakyThrows
    public void init() {
        Yaml yaml = new Yaml();
        data = yaml.load(classLoader.getResourceAsStream("config.yml"));
    }

    public static String getString(String key) {
        if (data.containsKey(key)) return data.get(key).toString();
        return null;
    }

}
