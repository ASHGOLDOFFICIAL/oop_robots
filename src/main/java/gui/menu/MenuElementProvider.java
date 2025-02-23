package gui.menu;

import gui.MainApplicationFrame;
import gui.MainApplicationFrameController;

import java.awt.*;

/**
 * <p>Предоставляет элементы меню.</p>
 */
public interface MenuElementProvider {
    /**
     * <p>Предоставляет элемент для добавления в меню.</p>
     *
     * @param frame      окно.
     * @param controller контроллер окна.
     * @return элемент для добавления.
     */
    Component provide(MainApplicationFrame frame, MainApplicationFrameController controller);
}
