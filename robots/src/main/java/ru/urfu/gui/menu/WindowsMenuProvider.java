package ru.urfu.gui.menu;

import java.awt.Component;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import ru.urfu.gui.MainFrame;
import ru.urfu.i18n.I18n;
import ru.urfu.i18n.I18nManager;

/**
 * <p>Создаёт меню для открытия окон.</p>
 */
final class WindowsMenuProvider implements MenuElementProvider {
    private final I18n i18n = I18nManager.getInstance().getI18n();

    @Override
    public Component provide(MainFrame frame) {
        final JMenu windowsMenu = new JMenu(i18n.tr("Windows"));

        windowsMenu.setMnemonic(KeyEvent.VK_W);
        windowsMenu.getAccessibleContext().setAccessibleDescription(
                i18n.tr("Controls windows."));

        windowsMenu.add(createOpenGameWindowItem(frame));
        windowsMenu.add(createOpenLogWindowItem(frame));
        windowsMenu.add(createOpenCoordinatesWindowItem(frame));

        return windowsMenu;
    }

    /**
     * <p>Создаёт элемент меню для открытия окна с игрой.</p>
     *
     * @param frame окно.
     * @return элемент меню.
     */
    private JMenuItem createOpenGameWindowItem(MainFrame frame) {
        final JMenuItem item = new JMenuItem(i18n.tr("Game Field"), KeyEvent.VK_G);
        item.addActionListener((event) -> frame.addGameWindowIfClosed());
        return item;
    }

    /**
     * <p>Создаёт элемент меню для открытия окна с логами.</p>
     *
     * @param frame окно.
     * @return элемент меню.
     */
    private JMenuItem createOpenLogWindowItem(MainFrame frame) {
        final JMenuItem item = new JMenuItem(i18n.tr("Logs"), KeyEvent.VK_L);
        item.addActionListener((event) -> frame.addLogWindowIfClosed());
        return item;
    }

    /**
     * <p>Создаёт элемент меню для открытия окна с логами.</p>
     *
     * @param frame окно.
     * @return элемент меню.
     */
    private JMenuItem createOpenCoordinatesWindowItem(MainFrame frame) {
        final JMenuItem item = new JMenuItem(i18n.tr("Coordinates"), KeyEvent.VK_L);
        item.addActionListener((event) -> frame.addCoordinatesWindowIfClosed());
        return item;
    }
}
