package ru.urfu.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Реализация {@link ConfigurationSource},
 * в которой источником конфигурации является файл.</p>
 */
public final class FileConfigurationSource implements ConfigurationSource {
    private final Logger log = LoggerFactory.getLogger(FileConfigurationSource.class);
    private final String fileName;

    /**
     * <p>Конструктор.</p>
     *
     * @param fileName имя файла с конфигурацией.
     */
    public FileConfigurationSource(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void save(Configuration config) throws ConfigSaveFailed {
        final Properties properties = new Properties();
        properties.putAll(config.toMap());
        try (Writer writer = new FileWriter(this.fileName)) {
            properties.store(writer, "config");
        } catch (IOException e) {
            throw new ConfigSaveFailed("Configuration save failed");
        }
    }

    @Override
    public Configuration load() throws ConfigLoadFailed {
        final Properties properties = loadProperties();
        return fromProperties(properties);
    }

    /**
     * <p>Выгружает properties из файла.</p>
     *
     * @return выгруженные properties.
     */
    private Properties loadProperties() {
        final Properties properties = new Properties();

        final File file = new File(this.fileName);
        try {
            final boolean created = file.createNewFile();
            if (created) {
                log.debug("Properties was created");
            }
        } catch (IOException e) {
            throw new ConfigLoadFailed("Couldn't create file");
        }

        try (Reader reader = new FileReader(file)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new ConfigLoadFailed("Couldn't read file");
        }

        return properties;
    }

    /**
     * <p>Создаёт конфигурацию из {@link Properties}.</p>
     *
     * @param prop properties.
     * @return полученную конфигурацию.
     */
    private Configuration fromProperties(Properties prop) {
        final Map<String, String> map = new HashMap<>();
        for (final String name : prop.stringPropertyNames()) {
            map.put(name, prop.getProperty(name));
        }
        return new Configuration(map);
    }
}
