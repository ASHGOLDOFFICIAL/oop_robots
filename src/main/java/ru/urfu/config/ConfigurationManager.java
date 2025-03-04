package ru.urfu.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Менеджер конфигураций: считывает её
 * из некоторого источника, сохраняет в него же.</p>
 */
public final class ConfigurationManager {
    private final Logger log = LoggerFactory.getLogger(ConfigurationManager.class);
    private final ConfigurationSource source;
    private final Configuration config;

    /**
     * <p>Конструктор.</p>
     *
     * @param source источник конфигурации
     */
    public ConfigurationManager(ConfigurationSource source) {
        this.source = source;
        this.config = this.source.load();
    }

    /**
     * <p>Получить конфигурацию.</p>
     *
     * @return конфигурацию
     */
    public Configuration get() {
        return this.config;
    }

    /**
     * <p>Сохранить изменения в источник.</p>
     *
     * @throws ConfigSaveFailed если сохранение не удалось
     */
    public void flush() throws ConfigSaveFailed {
        this.source.save(this.config);
        log.debug("Configuration flushed");
    }
}
