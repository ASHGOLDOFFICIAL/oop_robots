package gui.menu;

import gui.MainApplicationFrame;
import gui.MainApplicationFrameController;
import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * <p>Создаёт меню "Тесты"</p>
 */
public final class TestsMenuProvider implements MenuElementProvider {
    @Override
    public Component provide(MainApplicationFrame frame, MainApplicationFrameController controller) {
        final JMenu menu = new JMenu("Тесты");
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription("Тестовые команды");
        menu.add(createAddLogMessageItem());
        return menu;
    }

    /**
     * <p>Создаёт элемент меню "Сообщение в лог",
     * при нажатии на которую происходит логирование
     * сообщения "Новая строка".</p>
     *
     * @return созданный элемент.
     */
    private JMenuItem createAddLogMessageItem() {
        final JMenuItem menuItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        menuItem.addActionListener((event) -> Logger.debug("Новая строка"));
        return menuItem;
    }
}
