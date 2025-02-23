package gui.menu;

import gui.MainApplicationFrame;
import gui.MainApplicationFrameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * <p>Создаёт меню для смены темы оформления.</p>
 */
public final class LookAndFeelMenuProvider implements MenuElementProvider {
    @Override
    public Component provide(MainApplicationFrame frame, MainApplicationFrameController controller) {
        final JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

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
        final JMenuItem item = new JMenuItem("Системная схема", KeyEvent.VK_S);
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
        final JMenuItem item = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
        item.addActionListener((event) -> controller.setCrossPlatformLookAndFeel(frame));
        return item;
    }
}
