package ru.urfu.gui;

import javax.swing.SwingUtilities;

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
        final Runnable runGui = () -> {
            final MainFrame frame = new MainFrame();
            frame.setVisible(true);
        };

        SwingUtilities.invokeLater(runGui);
    }
}
