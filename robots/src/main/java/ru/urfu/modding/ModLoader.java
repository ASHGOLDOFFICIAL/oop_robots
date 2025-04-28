package ru.urfu.modding;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * <p>Загружает мод (см. {@link Mod}) из JAR-файла
 * из класса с названием <code>Mod</code>.</p>
 */
public final class ModLoader {
    private static final String CLASS_NAME = "Mod";

    /**
     * <p>Загружает мод из указанного JAR-файла.</p>
     *
     * @param jarFile JAR-файл с модом
     * @return мод
     * @throws ModLoaderException если загрузка мода не удалась
     */
    public Mod load(File jarFile) throws ModLoaderException {
        try {
            if (!jarFile.exists()) {
                throw new ModLoaderException("Given JAR file doesn't exits: " + jarFile.getAbsolutePath());
            }
            final URL[] url = new URL[]{jarFile.toURI().toURL()};

            Class<Mod> modClass;
            try (URLClassLoader classLoader = new URLClassLoader(url)) {
                modClass = (Class<Mod>) classLoader.loadClass(CLASS_NAME);
            }

            return modClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new ModLoaderException("Couldn't load mod from: " + jarFile.getAbsolutePath());
        }
    }
}
