package ru.urfu.i18n;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * <p>Содержит единый {@link I18n} и работает со слушателями.</p>
 */
public final class I18nManager {
    private static final String BASE_NAME = "ru.urfu.i18n.Messages";
    private static final I18nManager INSTANCE = new I18nManager();

    private final I18n i18n = new I18n(BASE_NAME, Locale.getDefault());
    private final Set<LocaleChangeListener> listeners = Collections.newSetFromMap(new WeakHashMap<>());

    /**
     * <p>Приватный конструктор синглтона.</p>
     */
    private I18nManager() {
    }

    /**
     * <p>Экземпляр менеджера.</p>
     *
     * @return экземпляр менеджера
     */
    public static I18nManager getInstance() {
        return INSTANCE;
    }

    /**
     * <p>Возвращает объект {@link I18n}.</p>
     *
     * @return объект {@link I18n}.
     */
    public I18n getI18n() {
        return i18n;
    }

    /**
     * <p>Меняет локаль по умолчанию у всех {@link I18n}
     * и оповещает слушателей о смене локали.</p>
     *
     * @param locale новая локаль
     */
    public void setDefaultLocale(Locale locale) {
        synchronized (i18n) {
            Locale.setDefault(locale);
            i18n.setLocale(locale);
        }
        fireLocaleChangedEvent(locale);
    }

    /**
     * <p>Добавляет слушателя о смене события по слабой ссылке.</p>
     *
     * @param listener слушатель
     */
    public void addWeakLocaleChangeListener(LocaleChangeListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * <p>Оповещает слушателей о смене локали.</p>
     *
     * @param newLocale новая локаль
     */
    private void fireLocaleChangedEvent(Locale newLocale) {
        LocaleChangeListener[] listenersCopy;
        synchronized (listeners) {
            listenersCopy = listeners.toArray(new LocaleChangeListener[0]);
        }

        if (listenersCopy.length > 0) {
            final LocaleChangedEvent event = new LocaleChangedEvent(I18nManager.class, newLocale);

            for (final LocaleChangeListener listener : listenersCopy) {
                listener.onLocaleChanged(event);
            }
        }
    }
}
