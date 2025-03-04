package ru.urfu.gui;

import java.awt.Dimension;
import javax.swing.JInternalFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18nManager;
import org.xnap.commons.i18n.LocaleChangeEvent;
import org.xnap.commons.i18n.LocaleChangeListener;
import ru.urfu.config.Configuration;

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
    private final Configuration config;

    /**
     * <p>Конструктор.</p>
     *
     * @param config конфигурация
     */
    protected CustomInternalFrame(Configuration config) {
        super(null, true, true, true);

        this.config = config;
        setPreferredSize(defaultSize());
        pack();
        new WindowConfigurationManager(config).configureWindow(this);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        I18nManager.getInstance().addLocaleChangeListener(this);
    }

    @Override
    public void dispose() {
        log.trace("{}[{}] is being disposed", this.getClass().getSimpleName(), hashCode());

        this.onDispose();

        new WindowConfigurationManager(config).saveState(this);
        I18nManager.getInstance().removeLocaleChangeListener(this);
        super.dispose();
    }

    @Override
    public void localeChanged(LocaleChangeEvent event) {
        setLocaleDependantProperties();
    }

    /**
     * <p>Размер по умолчанию для данного окна.</p>
     *
     * @return размер по умолчанию.
     */
    protected abstract Dimension defaultSize();

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
