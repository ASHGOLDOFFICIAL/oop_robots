package ru.urfu.gui.menu;

import ru.urfu.gui.MainApplicationFrame;
import ru.urfu.gui.MainApplicationFrameController;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

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
