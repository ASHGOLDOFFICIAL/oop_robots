package ru.urfu.config;

/**
 * <p>Считывание конфигурации не удалось.</p>
 */
public final class ConfigLoadFailed extends RuntimeException {
    /**
     * <p>Конструктор.</p>
     *
     * @param message сообщение
     */
    public ConfigLoadFailed(String message) {
        super(message);
    }
}
