package ru.urfu.config;

/**
 * <p>Источник для получения/сохранения конфигурации.</p>
 */
public interface ConfigurationSource {
    /**
     * <p>Сохраняет конфигурацию в источник.</p>
     *
     * @param config конфигурация.
     */
    void save(Configuration config) throws ConfigSaveFailed;

    /**
     * <p>Загружает конфигурацию.</p>
     *
     * @return конфигурация.
     */
    Configuration load() throws ConfigLoadFailed;
}
