package ru.urfu.gui;

import javax.swing.JInternalFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18nManager;
import org.xnap.commons.i18n.LocaleChangeEvent;
import org.xnap.commons.i18n.LocaleChangeListener;

/**
 * <p>{@link JInternalFrame} с предустановленными настройками:</p>
 * <ol>
 *     <li>При закрытии происходит вызов {@link JInternalFrame#dispose}.</li>
 *     <li>Класс следит за сменами дефолтной локали.</li>
 * </ol>
 */
public abstract class CustomInternalFrame
        extends JInternalFrame
        implements LocaleChangeListener {
    private final Logger log = LoggerFactory.getLogger(CustomInternalFrame.class);

    /**
     * <p>Конструктор.</p>
     */
    protected CustomInternalFrame() {
        super(null, true, true, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        I18nManager.getInstance().addLocaleChangeListener(this);
    }

    @Override
    public void dispose() {
        log.trace("{}[{}] is being disposed", this.getClass().getSimpleName(), hashCode());
        this.onDispose();
        I18nManager.getInstance().removeLocaleChangeListener(this);
        super.dispose();
    }

    @Override
    public void localeChanged(LocaleChangeEvent event) {
        setLocaleDependantProperties();
    }

    /**
     * <p>Действия, которые нужно совершить
     * перед вызовом {@link JInternalFrame#dispose}.</p>
     */
    protected abstract void onDispose();

    /**
     * <p>Устанавливает свойства и поля,
     * зависящие от текущей локали.</p>
     */
    protected abstract void setLocaleDependantProperties();
}
