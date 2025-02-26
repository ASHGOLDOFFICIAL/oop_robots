package ru.urfu.config;

public class ConfigurationManager {
    private final ConfigurationSource source;
    private Configuration config;

    public ConfigurationManager(ConfigurationSource source) {
        this.source = source;
    }

    public Configuration get() {
        if (config == null) {
            this.config = this.source.load();
        }
        return this.config;
    }

    public void flush() throws ConfigSaveFailed {
        this.source.save(this.config);
    }
}
