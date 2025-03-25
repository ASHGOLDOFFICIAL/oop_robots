package ru.urfu.state;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.Locale;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18nManager;
import ru.urfu.config.Configuration;

/**
 * <p>Менеджер конфигураций окон.</p>
 *
 * <p>Сохраняет и загружает общее состояние окна: положение и размер.</p>
 */
public final class StateManager {
    private final static String LANGUAGE_KEY = "language";
    private final static String WIDTH_KEY = "width";
    private final static String HEIGHT_KEY = "height";
    private final static String X_KEY = "x";
    private final static String Y_KEY = "y";
    private final static String MINIMIZED_KEY = "minimized";

    private final Logger log = LoggerFactory.getLogger(StateManager.class);
    private final Configuration config;

    /**
     * <p>Конструктор.</p>
     *
     * @param config конфиг, в который сохранять информацию
     */
    public StateManager(Configuration config) {
        this.config = config;
    }

    /**
     * <p>Восстанавливает состояние {@link JFrame}
     * и его дочерних компонентов, реализующих {@link Stateful}.</p>
     *
     * @param frame корневой фрейм
     * @param pane  панель с окнами
     */
    public void restoreState(JFrame frame, JDesktopPane pane) {
        restoreLocale();
        configureComponent(frame);
        for (JInternalFrame child : pane.getAllFrames()) {
            if (child instanceof Stateful) {
                configureFrame(child);
            }
        }
    }

    /**
     * <p>Сохраняет состояние {@link JFrame}
     * и его дочерних компонентов, реализующих {@link Stateful}.</p>
     *
     * @param frame корневой фрейм
     * @param pane  панель с окнами
     */
    public void saveState(JFrame frame, JDesktopPane pane) {
        for (JInternalFrame child : pane.getAllFrames()) {
            if (child instanceof Stateful) {
                saveFrameState(child);
            }
        }
        saveComponentState(frame);
        config.put(LANGUAGE_KEY, Locale.getDefault().toString());
    }

    /**
     * <p>Восстанавливает локаль.</p>
     */
    private void restoreLocale() {
        final String language = config.get(LANGUAGE_KEY, Locale.getDefault().toString());
        final Locale locale = Locale.forLanguageTag(language);
        Locale.setDefault(locale);
        I18nManager.getInstance().setDefaultLocale(locale);
        log.debug("Initial locale is {}", locale);
    }

    /**
     * <p>Конфигурирует компонент, считывая состояние из конфига.</p>
     *
     * @param component конфигурируемый компонент.
     */
    private void configureComponent(Component component) {
        final String prefix = component.getClass().getSimpleName() + ".";
        final Point location = component.getLocation();
        final int x = config.get(prefix + X_KEY, location.x);
        final int y = config.get(prefix + Y_KEY, location.y);
        final int width = config.get(prefix + WIDTH_KEY, component.getWidth());
        final int height = config.get(prefix + HEIGHT_KEY, component.getHeight());
        component.setBounds(x, y, width, height);
    }

    /**
     * <p>Конфигурирует {@link JInternalFrame}.</p>
     *
     * @param frame конфигурируемый фрейм.
     */
    private void configureFrame(JInternalFrame frame) {
        configureComponent(frame);
        final String prefix = frame.getClass().getSimpleName() + ".";
        final boolean isMinimized = config.get(prefix + MINIMIZED_KEY, false);

        try {
            frame.setIcon(isMinimized);
        } catch (PropertyVetoException e) {
            log.warn("Couldn't set minimized state of frame", e);
        }
    }

    /**
     * <p>Сохраняет состояние компонента: размер и положение.</p>
     *
     * @param component компонент, чьё состояние сохраняем.
     */
    private void saveComponentState(Component component) {
        final String prefix = component.getClass().getSimpleName() + ".";
        final Rectangle bounds = component.getBounds();
        config.put(prefix + WIDTH_KEY, bounds.width);
        config.put(prefix + HEIGHT_KEY, bounds.height);
        config.put(prefix + X_KEY, bounds.x);
        config.put(prefix + Y_KEY, bounds.y);
    }

    /**
     * <p>Сохраняет состояние {@link JInternalFrame}:
     * размер, положение, состояние минимизации.</p>
     *
     * @param frame фрейм, чьё состояние сохраняем.
     */
    private void saveFrameState(JInternalFrame frame) {
        saveComponentState(frame);
        final String prefix = frame.getClass().getSimpleName() + ".";
        config.put(prefix + MINIMIZED_KEY, frame.isIcon());
    }
}
