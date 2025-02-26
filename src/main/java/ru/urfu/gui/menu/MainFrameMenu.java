package ru.urfu.gui.menu;

import javax.swing.JMenuBar;
import ru.urfu.gui.MainFrame;

/**
 * <p>Полоска меню для главного окна.</p>
 */
public final class MainFrameMenu extends JMenuBar {
    /**
     * <p>Конструктор.</p>
     *
     * @param frame главное окно.
     */
    public MainFrameMenu(MainFrame frame) {
        add(new ControlMenuProvider().provide(frame));
        add(new WindowsMenuProvider().provide(frame));
        add(new TestsMenuProvider().provide(frame));
    }
}
