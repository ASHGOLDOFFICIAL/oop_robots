package ru.urfu.gui.menu;

import ru.urfu.gui.MainApplicationFrame;
import ru.urfu.gui.MainApplicationFrameController;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * <p>Создаёт меню для смены темы оформления.</p>
 */
public final class LookAndFeelMenuProvider implements MenuElementProvider {
    private final I18n i18n = I18nFactory.getI18n(getClass());

    @Override
    public Component provide(MainApplicationFrame frame, MainApplicationFrameController controller) {
        final JMenu lookAndFeelMenu = new JMenu(i18n.tr("Look and Feel"));

        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                i18n.tr("Controls look and feel of the program."));

        lookAndFeelMenu.add(createSetSystemLookAndFeelItem(frame, controller));
        lookAndFeelMenu.add(createSetCrossplatformLookAndFeelItem(frame, controller));

        return lookAndFeelMenu;
    }

    /**
     * <p>Создаёт элемент меню для переключения системную темы оформления.</p>
     * @param frame окно.
     * @param controller контроллер окна.
     * @return элемент меню.
     */
    private JMenuItem createSetSystemLookAndFeelItem(MainApplicationFrame frame,
                                                     MainApplicationFrameController controller) {
        final JMenuItem item = new JMenuItem(i18n.tr("System Theme"), KeyEvent.VK_S);
        item.addActionListener((event) -> controller.setSystemLookAndFeel(frame));
        return item;
    }

    /**
     * <p>Создаёт элемент меню для переключения универсальную темы оформления.</p>
     * @param frame окно.
     * @param controller контроллер окна.
     * @return элемент меню.
     */
    private JMenuItem createSetCrossplatformLookAndFeelItem(MainApplicationFrame frame,
                                                            MainApplicationFrameController controller) {
        final JMenuItem item = new JMenuItem(i18n.tr("Crossplatform Theme"), KeyEvent.VK_S);
        item.addActionListener((event) -> controller.setCrossPlatformLookAndFeel(frame));
        return item;
    }
}
