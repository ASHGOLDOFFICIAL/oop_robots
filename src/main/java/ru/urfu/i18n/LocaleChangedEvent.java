package ru.urfu.i18n;

import java.util.EventObject;
import java.util.Locale;

/**
 * <p>Событие смены локали.</p>
 */
public final class LocaleChangedEvent extends EventObject {
    private final Locale newLocale;

    /**
     * <p>Конструктор.</p>
     *
     * @param source    источник события
     * @param newLocale новая локаль
     */
    public LocaleChangedEvent(Object source, Locale newLocale) {
        super(source);

        this.newLocale = newLocale;
    }

    /**
     * <p>Возвращает новую локаль.</p>
     *
     * @return новую локаль
     */
    public Locale getNewLocale() {
        return newLocale;
    }
}
