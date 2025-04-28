package ru.urfu.gui.menu;

import java.awt.Component;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import ru.urfu.gui.MainFrame;
import ru.urfu.i18n.I18n;
import ru.urfu.i18n.I18nManager;
import ru.urfu.log.Logger;


/**
 * <p>Создаёт меню "Тесты".</p>
 */
final class TestsMenuProvider implements MenuElementProvider {
    private final I18n i18n = I18nManager.getInstance().getI18n();

    @Override
    public Component provide(MainFrame frame) {
        final JMenu menu = new JMenu(i18n.tr("Tests"));
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription(i18n.tr("Test commands."));
        menu.add(createAddLogMessageItem(i18n.tr("Log message A"), i18n.tr("New string A")));
        menu.add(createAddLogMessageItem(i18n.tr("Log message B"), i18n.tr("New string B")));
        return menu;
    }

    /**
     * <p>Создаёт элемент меню "Сообщение в лог",
     * при нажатии на которую происходит логирование
     * сообщения "Новая строка".</p>
     *
     * @param menuName отображаемое имя
     * @param message  логируемое сообщение
     * @return созданный элемент.
     */
    private JMenuItem createAddLogMessageItem(String menuName, String message) {
        final JMenuItem menuItem = new JMenuItem(menuName, KeyEvent.VK_S);
        menuItem.addActionListener((event) -> Logger.debug(message));
        return menuItem;
    }
}
