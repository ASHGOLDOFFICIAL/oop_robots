package ru.urfu.i18n;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>Кэш для паттернов {@link MessageFormat}.</p>
 *
 * <p>Ограничен по размеру. При превышении очищается.</p>
 */
public class MessageFormatCache {
    private final int capacity;
    private final ConcurrentMap<String, MessageFormat> patternCache =
            new ConcurrentHashMap<>();

    /**
     * <p>Конструктор.</p>
     *
     * @param size размер кэша.
     */
    public MessageFormatCache(int size) {
        this.capacity = size;
    }

    /**
     * <p>Возвращает паттерн по строке-ключу.</p>
     *
     * <p>Если паттерна не было, создаёт новый, сохраняет и возвращает.</p>
     *
     * @param key строка-ключ
     * @return паттерн
     */
    public synchronized MessageFormat get(String key) {
        final MessageFormat pattern = patternCache.get(key);
        if (pattern != null) {
            return pattern;
        }

        if (patternCache.size() >= capacity) {
            patternCache.clear();
        }
        final MessageFormat newPattern = new MessageFormat(key);
        patternCache.put(key, newPattern);
        return newPattern;
    }
}
