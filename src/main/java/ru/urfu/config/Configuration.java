package ru.urfu.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Конфигурация.</p>
 */
public final class Configuration {
    private final Map<String, String> config = new ConcurrentHashMap<>();

    /**
     * <p>Конструктор, создающий пустую конфигурацию.</p>
     */
    public Configuration() {
    }

    /**
     * <p>Конструктор, заполняющий конфигурацию
     * содержимым переданного отображения.</p>
     *
     * @param map отображение, из которого будут считываться пары ключ-значение
     */
    public Configuration(Map<String, String> map) {
        config.putAll(map);
    }

    /**
     * <p>Помещает пару ключ-значение в конфигурацию.</p>
     *
     * @param key   ключ
     * @param value строковое значение
     */
    public void put(String key, String value) {
        config.put(key, value);
    }

    /**
     * <p>Достаёт значение по ключу. Если не нашлось,
     * возвращает значение по умолчанию.</p>
     *
     * @param key ключ
     * @param def значение по умолчанию
     * @return полученное значение или значение по умолчанию
     */
    public String get(String key, String def) {
        return config.getOrDefault(key, def);
    }

    /**
     * <p>Помещает пару ключ-значение в конфигурацию.</p>
     *
     * @param key   ключ
     * @param value целочисленное значение
     */
    public void put(String key, int value) {
        config.put(key, String.valueOf(value));
    }

    /**
     * <p>Достаёт значение по ключу. Если не нашлось,
     * возвращает значение по умолчанию.</p>
     *
     * @param key ключ
     * @param def значение по умолчанию
     * @return полученное значение или значение по умолчанию
     */
    public int get(String key, int def) {
        String value = config.get(key);
        if (value == null) {
            return def;
        }
        return Integer.parseInt(config.get(key));
    }

    /**
     * <p>Создаёт отображение с парами
     * ключ-значение из конфигурации.</p>
     *
     * @return полученное отображение
     */
    public Map<String, String> toMap() {
        return new HashMap<>(this.config);
    }
}
