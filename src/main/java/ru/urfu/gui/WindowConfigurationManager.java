package ru.urfu.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
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
     * <p>Конфигурирует окно, считывая состояние из конфига.</p>
     *
     * @param window конфигурируемое окно.
     */
    public void configureWindow(Component window) {
        final String prefix = window.getClass().getSimpleName() + ".";
        final Point location = window.getLocation();
        final int x = config.get(prefix + X_KEY, location.x);
        final int y = config.get(prefix + Y_KEY, location.y);
        final int width = config.get(prefix + WIDTH_KEY, window.getWidth());
        final int height = config.get(prefix + HEIGHT_KEY, window.getHeight());
        window.setBounds(x, y, width, height);
    }

    /**
     * <p>Сохраняет состояние окна: размер и положение.</p>
     *
     * @param window окно, чьё состояние сохраняем.
     */
    public void saveState(Component window) {
        final String prefix = window.getClass().getSimpleName() + ".";
        final Rectangle bounds = window.getBounds();
        config.put(prefix + WIDTH_KEY, bounds.width);
        config.put(prefix + HEIGHT_KEY, bounds.height);
        config.put(prefix + X_KEY, bounds.x);
        config.put(prefix + Y_KEY, bounds.y);
    }
}
