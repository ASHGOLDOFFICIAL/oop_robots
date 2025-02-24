package ru.urfu.gui;

import java.awt.Frame;
import java.util.Locale;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    public static void main(String[] args) {
        final Logger log = LoggerFactory.getLogger(Main.class);
        final Locale locale = Locale.getDefault();
        log.info("System locale is {}", locale);

        SwingUtilities.invokeLater(() -> {
            final MainFrame frame = new MainFrame();
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
