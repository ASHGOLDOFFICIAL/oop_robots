package ru.urfu.gui.menu;

import ru.urfu.gui.MainApplicationFrame;
import ru.urfu.gui.MainApplicationFrameController;
import ru.urfu.log.Logger;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * <p>Создаёт меню "Тесты"</p>
 */
public final class TestsMenuProvider implements MenuElementProvider {
    private final I18n i18n = I18nFactory.getI18n(getClass());

    @Override
    public Component provide(MainApplicationFrame frame, MainApplicationFrameController controller) {
        final JMenu menu = new JMenu(i18n.tr("Tests"));
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription(i18n.tr("Test commands.p"));
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
        final JMenuItem menuItem = new JMenuItem(i18n.tr("Log a message"), KeyEvent.VK_S);
        menuItem.addActionListener((event) -> Logger.debug(i18n.tr("New string")));
        return menuItem;
    }
}
