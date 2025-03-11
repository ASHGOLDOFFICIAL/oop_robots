package ru.urfu.gui;

import javax.swing.SwingUtilities;
import ru.urfu.core.GameModel;
import ru.urfu.core.GameModelImpl;
import ru.urfu.core.GameTimerController;

/**
 * <p>Класс для запуска приложения.</p>
 */
public final class Main {
    /**
     * <p>Приватный конструктор,
     * чтобы не создавали объектов.</p>
     */
    private Main() {
    }

    /**
     * <p>Метод для запуска приложения.</p>
     *
     * @param args аргументы.
     */
    public static void main(String[] args) {
        final GameModel gameModel = new GameModelImpl();
        final GameTimerController timerController = new GameTimerController(gameModel);
        timerController.start();

        final Runnable runGui = () -> {
            final MainFrame frame = new MainFrame(gameModel, timerController);
            frame.setVisible(true);
        };

        SwingUtilities.invokeLater(runGui);
    }
}
