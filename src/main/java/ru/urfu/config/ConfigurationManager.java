package ru.urfu.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigurationManager {
    private final Logger log = LoggerFactory.getLogger(ConfigurationManager.class);
    private final ConfigurationSource source;
    private final Configuration config;

    public ConfigurationManager(ConfigurationSource source) {
        this.source = source;
        this.config = this.source.load();
    }

    public Configuration get() {
        return this.config;
    }

    public void flush() throws ConfigSaveFailed {
        this.source.save(this.config);
        log.debug("Configuration flushed");
    }
}
