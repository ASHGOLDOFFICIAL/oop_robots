package ru.urfu.gui.menu;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import ru.urfu.gui.MainFrame;

public final class ControlMenuProvider implements MenuElementProvider {
    private final I18n i18n = I18nFactory.getI18n(getClass());

    @Override
    public Component provide(MainFrame frame) {
        final JMenu menu = new JMenu(i18n.tr("Control"));

        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
                i18n.tr("Controls the application."));

        menu.add(new LookAndFeelMenuProvider().provide(frame));
        menu.add(new LanguagesMenuProvider().provide(frame));
        menu.add(new JSeparator());
        menu.add(createExitItem(frame));

        return menu;
    }

    public Component createExitItem(MainFrame frame) {
        final JMenuItem item = new JMenuItem(i18n.tr("Exit"), KeyEvent.VK_Q);
        item.addActionListener((event) ->
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
        return item;
    }
}
