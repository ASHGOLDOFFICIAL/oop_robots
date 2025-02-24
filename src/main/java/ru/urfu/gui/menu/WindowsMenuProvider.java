package ru.urfu.gui.menu;

import java.awt.Component;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import ru.urfu.gui.MainFrame;

public final class WindowsMenuProvider implements MenuElementProvider {
    private final I18n i18n = I18nFactory.getI18n(getClass());

    @Override
    public Component provide(MainFrame frame) {
        final JMenu windowsMenu = new JMenu(i18n.tr("Windows"));

        windowsMenu.setMnemonic(KeyEvent.VK_W);
        windowsMenu.getAccessibleContext().setAccessibleDescription(
                i18n.tr("Controls windows."));

        windowsMenu.add(createOpenGameWindowItem(frame));
        windowsMenu.add(createOpenLogWindowItem(frame));

        return windowsMenu;
    }

    /**
     * <p>Создаёт элемент меню для открытия окна с игрой.</p>
     *
     * @param frame окно.
     * @return элемент меню.
     */
    private JMenuItem createOpenGameWindowItem(MainFrame frame) {
        final JMenuItem item = new JMenuItem(i18n.tr("Game Window"), KeyEvent.VK_G);
        item.addActionListener((event) -> frame.addGameWindow());
        return item;
    }

    /**
     * <p>Создаёт элемент меню для открытия окна с логами.</p>
     *
     * @param frame окно.
     * @return элемент меню.
     */
    private JMenuItem createOpenLogWindowItem(MainFrame frame) {
        final JMenuItem item = new JMenuItem(i18n.tr("Log Window"), KeyEvent.VK_L);
        item.addActionListener((event) -> frame.addLogWindow());
        return item;
    }
}
