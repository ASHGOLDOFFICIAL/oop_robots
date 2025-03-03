package ru.urfu.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import javax.swing.JInternalFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.config.Configuration;

/**
 * <p>Менеджер конфигураций окон.</p>
 *
 * <p>Сохраняет и загружает общее состояние окна: положение и размер.</p>
 */
public final class WindowConfigurationManager {
    private final static String WIDTH_KEY = "width";
    private final static String HEIGHT_KEY = "height";
    private final static String X_KEY = "x";
    private final static String Y_KEY = "y";
    private final static String MINIMIZED_KEY = "minimized";

    private final Logger log = LoggerFactory.getLogger(WindowConfigurationManager.class);

    private final Configuration config;

    /**
     * <p>Конструктор.</p>
     *
     * @param config конфиг, в который сохранять информацию
     */
    public WindowConfigurationManager(Configuration config) {
        this.config = config;
    }

    /**
     * <p>Конфигурирует компонент, считывая состояние из конфига.</p>
     *
     * @param component конфигурируемый компонент.
     */
    public void configureComponent(Component component) {
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
    public void configureFrame(JInternalFrame frame) {
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
    public void saveComponentState(Component component) {
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
    public void saveFrameState(JInternalFrame frame) {
        saveComponentState(frame);
        final String prefix = frame.getClass().getSimpleName() + ".";
        config.put(prefix + MINIMIZED_KEY, frame.isIcon());
    }
}
