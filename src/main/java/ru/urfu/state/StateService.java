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
import ru.urfu.config.Configuration;
import ru.urfu.i18n.I18nManager;

/**
 * <p>Менеджер конфигураций окон.</p>
 *
 * <p>Сохраняет и загружает общее состояние окна: положение и размер.</p>
 */
public final class StateService {
    private final static String LANGUAGE_KEY = "language";
    private final static String WIDTH_KEY = "width";
    private final static String HEIGHT_KEY = "height";
    private final static String X_KEY = "x";
    private final static String Y_KEY = "y";
    private final static String MINIMIZED_KEY = "minimized";

    private final Logger log = LoggerFactory.getLogger(StateService.class);
    private final Configuration config;

    /**
     * <p>Конструктор.</p>
     *
     * @param config конфиг, в который сохранять информацию
     */
    public StateService(Configuration config) {
        this.config = config;
    }

    /**
     * <p>Восстанавливает состояние {@link JFrame}
     * и его дочерних компонентов, реализующих {@link Stateful}.</p>
     *
     * @param root корневой фрейм
     * @param pane панель с окнами
     */
    public void restoreState(Stateful root, JDesktopPane pane) {
        restoreLocale();
        configureComponent(root);

        for (JInternalFrame child : pane.getAllFrames()) {
            if (child instanceof Stateful stateful) {
                configureComponent(stateful);
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
    public void saveState(Stateful frame, JDesktopPane pane) {
        for (JInternalFrame child : pane.getAllFrames()) {
            if (child instanceof Stateful stateful) {
                saveComponentState(stateful);
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
     * @param comp конфигурируемый компонент.
     */
    private void configureComponent(Stateful comp) {
        log.debug("Configuring {}", comp.getClass().getSimpleName());
        final String prefix = comp.getNameForStateService() + ".";

        if (comp instanceof Component component) {
            log.debug("Configuring {} as Component", comp.getClass().getSimpleName());
            final Point location = component.getLocation();
            final int x = config.get(prefix + X_KEY, location.x);
            final int y = config.get(prefix + Y_KEY, location.y);
            final int width = config.get(prefix + WIDTH_KEY, component.getWidth());
            final int height = config.get(prefix + HEIGHT_KEY, component.getHeight());
            component.setBounds(x, y, width, height);
        }

        if (comp instanceof JInternalFrame frame) {
            log.debug("Configuring {} as JInternalFrame", comp.getClass().getSimpleName());
            final boolean isMinimized = config.get(prefix + MINIMIZED_KEY, false);

            try {
                frame.setIcon(isMinimized);
            } catch (PropertyVetoException e) {
                log.warn("Couldn't set minimized state of frame", e);
            }
        }
    }

    /**
     * <p>Сохраняет состояние компонента: размер и положение.</p>
     *
     * @param comp компонент, чьё состояние сохраняем.
     */
    private void saveComponentState(Stateful comp) {
        log.debug("Saving {}", comp.getClass().getSimpleName());
        final String prefix = comp.getNameForStateService() + ".";

        if (comp instanceof Component component) {
            log.debug("Saving {} as Component", comp.getClass().getSimpleName());
            final Rectangle bounds = component.getBounds();
            config.put(prefix + WIDTH_KEY, bounds.width);
            config.put(prefix + HEIGHT_KEY, bounds.height);
            config.put(prefix + X_KEY, bounds.x);
            config.put(prefix + Y_KEY, bounds.y);
        }

        if (comp instanceof JInternalFrame frame) {
            log.debug("Saving {} as JInternalFrame", comp.getClass().getSimpleName());
            config.put(prefix + MINIMIZED_KEY, frame.isIcon());
        }
    }
}
