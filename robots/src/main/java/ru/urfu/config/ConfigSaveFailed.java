package ru.urfu.config;

/**
 * <p>Сохранение конфигурации провалилось.</p>
 */
public final class ConfigSaveFailed extends Exception {
    /**
     * <p>Конструктор.</p>
     *
     * @param message сообщение
     */
    public ConfigSaveFailed(String message) {
        super(message);
    }
}
