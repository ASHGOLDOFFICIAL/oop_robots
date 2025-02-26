package ru.urfu.gui;

import java.awt.Frame;
import java.util.Locale;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        final Logger log = LoggerFactory.getLogger(Main.class);
        final Locale locale = Locale.getDefault();
        log.debug("System locale is {}", locale);

        SwingUtilities.invokeLater(() -> {
            final MainFrame frame = new MainFrame();
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
