package ru.urfu.gui;

import java.awt.Frame;
import java.util.Locale;

import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RobotsProgram {
    public static void main(String[] args) {
        final Logger log = LoggerFactory.getLogger(RobotsProgram.class);

        final Locale locale = Locale.getDefault();
        log.debug("System locale is {}", locale);

        JOptionPane.setDefaultLocale(locale);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            log.error("Error during setting application theme", e);
        }

        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
