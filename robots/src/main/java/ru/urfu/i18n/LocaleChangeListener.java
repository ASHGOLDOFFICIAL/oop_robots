package ru.urfu.i18n;


/**
 * <p>Слушатель событий смены локали.</p>
 */
public interface LocaleChangeListener {

    /**
     * <p>Вызывается при смене локали.</p>
     *
     * @param event событие смены локали
     */
    void onLocaleChanged(LocaleChangedEvent event);

}
