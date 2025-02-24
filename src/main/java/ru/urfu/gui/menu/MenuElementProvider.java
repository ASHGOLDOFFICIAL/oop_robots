package ru.urfu.gui.menu;

import java.awt.Component;
import ru.urfu.gui.MainFrame;

/**
 * <p>Предоставляет элементы меню.</p>
 */
public interface MenuElementProvider {
    /**
     * <p>Предоставляет элемент для добавления в меню.</p>
     *
     * @param frame окно.
     * @return элемент для добавления.
     */
    Component provide(MainFrame frame);
}
