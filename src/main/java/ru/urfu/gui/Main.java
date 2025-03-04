package ru.urfu.gui;

import javax.swing.SwingUtilities;
import ru.urfu.core.GameModel;
import ru.urfu.core.GameModelImpl;

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

        final Runnable runGui = () -> {
            final MainFrame frame = new MainFrame(gameModel);
            frame.setVisible(true);
        };

        SwingUtilities.invokeLater(runGui);
    }
}
