package ru.urfu.gui.menu;

import java.awt.Component;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import ru.urfu.gui.MainApplicationFrame;
import ru.urfu.gui.MainApplicationFrameController;

/**
 * <p>Создаёт элемент меню "Выход"
 * для выхода из приложения.</p>
 */
public final class ExitMenuProvider implements MenuElementProvider {
    private final I18n i18n = I18nFactory.getI18n(getClass());

    @Override
    public Component provide(MainApplicationFrame frame, MainApplicationFrameController controller) {
        final JMenuItem item = new JMenuItem(i18n.tr("Exit"), KeyEvent.VK_Q);
        item.addActionListener((event) -> controller.showExitConfirmationPopUp());
        return item;
    }
}
