package ru.urfu.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * <p>Набор методов для интернационализации в заданную локаль.</p>
 */
public final class I18n {
    private final int cacheSize = 1 << 12;
    private final MessageFormatCache cache = new MessageFormatCache(cacheSize);
    private volatile ResourceBundle bundle;
    private String baseName;

    /**
     * <p>Конструктор.</p>
     *
     * @param baseName имя основания для поиска ресурсов.
     * @param locale   первоначальная локаль
     */
    public I18n(String baseName, Locale locale) {
        setResources(baseName, locale);
    }

    /**
     * <p>Пробует загрузить ресурсы для переданной локали.</p>
     *
     * @param baseName имя основания для поиска ресурсов
     * @param locale   локаль
     */
    public synchronized void setResources(String baseName, Locale locale) {
        this.bundle = ResourceBundle.getBundle(baseName, locale);
        this.baseName = baseName;
    }

    /**
     * <p>Меняет локаль и пакеты ресурсов для данного объекта.</p>
     *
     * @param locale локаль
     */
    public synchronized void setLocale(Locale locale) {
        if (baseName != null) {
            setResources(baseName, locale);
        }
    }

    /**
     * <p>Возвращает строку с подставленными аргументами для текущей локали.</p>
     * <p>Если такой версии нет, возвращает саму строку.</p>
     *
     * @param text строка для интернационализации
     * @param args аргументы для подстановки.
     * @return интернационализированную строку.
     */
    public String tr(String text, Object... args) {
        final String stringToFormat = getStringFromBundle(text);

        if (args.length > 0) {
            final String key = getStringFromBundle(text);
            return cache.get(key).format(args);
        }

        return stringToFormat;
    }

    /**
     * <p>Возвращает интернационализированную версию строки.</p>
     * <p>Если такой версии нет, возвращает саму строку.</p>
     *
     * @param text строка для интернационализации
     * @return строка из пакета ресурсов, если есть, иначе аргумент
     */
    private String getStringFromBundle(String text) {
        try {
            return bundle.getString(text);
        } catch (MissingResourceException e) {
            return text;
        }
    }
}
