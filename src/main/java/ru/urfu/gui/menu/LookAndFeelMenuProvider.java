package ru.urfu.gui.menu;

import java.awt.Component;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import ru.urfu.gui.MainFrame;


/**
 * <p>Создаёт меню для смены темы оформления.</p>
 */
public final class LookAndFeelMenuProvider implements MenuElementProvider {
    private final I18n i18n = I18nFactory.getI18n(getClass());

    @Override
    public Component provide(MainFrame frame) {
        final JMenu lookAndFeelMenu = new JMenu(i18n.tr("Look and Feel"));

        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                i18n.tr("Controls look and feel of the program."));

        lookAndFeelMenu.add(createSetSystemLookAndFeelItem(frame));
        lookAndFeelMenu.add(createSetCrossplatformLookAndFeelItem(frame));

        return lookAndFeelMenu;
    }

    /**
     * <p>Создаёт элемент меню для переключения системную темы оформления.</p>
     * @param frame окно.
     * @param controller контроллер окна.
     * @return элемент меню.
     */
    private JMenuItem createSetSystemLookAndFeelItem(MainFrame frame) {
        final JMenuItem item = new JMenuItem(i18n.tr("System Theme"), KeyEvent.VK_S);
        item.addActionListener((event) -> frame.setSystemLookAndFeel());
        return item;
    }

    /**
     * <p>Создаёт элемент меню для переключения универсальную темы оформления.</p>
     * @param frame окно.
     * @param controller контроллер окна.
     * @return элемент меню.
     */
    private JMenuItem createSetCrossplatformLookAndFeelItem(MainFrame frame) {
        final JMenuItem item = new JMenuItem(i18n.tr("Crossplatform Theme"), KeyEvent.VK_S);
        item.addActionListener((event) -> frame.setCrossPlatformLookAndFeel());
        return item;
    }
}
