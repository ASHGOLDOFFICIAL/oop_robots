package gui.menu;

import gui.MainApplicationFrame;
import gui.MainApplicationFrameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * <p>Создаёт элемент меню "Выход"
 * для выхода из приложения.</p>
 */
public final class ExitMenuProvider implements MenuElementProvider {
    @Override
    public Component provide(MainApplicationFrame frame, MainApplicationFrameController controller) {
        final JMenuItem item = new JMenuItem("Выход", KeyEvent.VK_Q);
        item.addActionListener((event) -> controller.showExitConfirmationPopUp(frame));
        return item;
    }
}
