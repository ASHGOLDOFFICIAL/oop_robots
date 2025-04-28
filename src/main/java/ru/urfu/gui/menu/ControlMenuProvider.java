package ru.urfu.gui.menu;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import ru.urfu.gui.MainFrame;
import ru.urfu.i18n.I18n;
import ru.urfu.i18n.I18nManager;

/**
 * <p>Создаёт меню для управления приложением.</p>
 */
public final class ControlMenuProvider implements MenuElementProvider {
    private final I18n i18n = I18nManager.getInstance().getI18n();

    @Override
    public Component provide(MainFrame frame) {
        final JMenu menu = new JMenu(i18n.tr("Control"));

        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
                i18n.tr("Controls the application."));

        menu.add(new LookAndFeelMenuProvider().provide(frame));
        menu.add(new LanguageMenuProvider().provide(frame));
        menu.add(new JSeparator());
        menu.add(createExitItem(frame));

        return menu;
    }

    /**
     * <p>Создаёт кнопку для выхода из приложения.</p>
     *
     * @param frame главное окно, которое надо закрыть.
     * @return кнопку.
     */
    public Component createExitItem(MainFrame frame) {
        final JMenuItem item = new JMenuItem(i18n.tr("Exit"), KeyEvent.VK_Q);
        item.addActionListener((event) ->
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
        return item;
    }
}
