package ru.urfu.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Configuration {
    private final Map<String, String> config = new ConcurrentHashMap<>();

    public Configuration() {
    }

    public Configuration(Map<String, String> map) {
        config.putAll(map);
    }

    public void put(String key, String value) {
        config.put(key, value);
    }

    public String get(String key, String def) {
        return config.getOrDefault(key, def);
    }

    public void put(String key, int value) {
        config.put(key, String.valueOf(value));
    }

    public int get(String key, int def) {
        String value = config.get(key);
        if (value == null) {
            return def;
        }
        return Integer.parseInt(config.get(key));
    }

    public void remove(String key) {
        config.remove(key);
    }

    public Map<String, String> toMap() {
        return new HashMap<>(this.config);
    }
}