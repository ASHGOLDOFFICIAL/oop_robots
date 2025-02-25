package ru.urfu.gui.menu;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JMenuItem;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import ru.urfu.gui.MainFrame;

/**
 * <p>Создаёт элемент меню "Выход"
 * для выхода из приложения.</p>
 */
public final class ExitMenuProvider implements MenuElementProvider {
    private final I18n i18n = I18nFactory.getI18n(getClass());

    @Override
    public Component provide(MainFrame frame) {
        final JMenuItem item = new JMenuItem(i18n.tr("Exit"), KeyEvent.VK_Q);
        item.addActionListener((event) ->
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
        return item;
    }
}
