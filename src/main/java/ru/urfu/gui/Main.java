package ru.urfu.gui;

import java.io.File;
import javax.swing.SwingUtilities;
import org.slf4j.LoggerFactory;
import ru.urfu.config.ConfigurationManager;
import ru.urfu.config.ConfigurationSource;
import ru.urfu.config.FileConfigurationSource;
import ru.urfu.core.GameModel;
import ru.urfu.core.GameModelImpl;
import ru.urfu.core.GameTimerController;

/**
 * <p>Класс для запуска приложения.</p>
 */
public final class Main {
    private final static String CONFIG_FILE = System.getProperty("user.home") + File.separator + "robots.properties";

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
        LoggerFactory.getLogger(Main.class).debug("Configuration file is {}", CONFIG_FILE);

        final GameModel gameModel = new GameModelImpl();
        final GameTimerController timerController = new GameTimerController(gameModel);
        final ConfigurationSource configSource = new FileConfigurationSource(CONFIG_FILE);
        final ConfigurationManager configManager = new ConfigurationManager(configSource);

        timerController.start();

        final Runnable runGui = () -> {
            final MainFrame frame = new MainFrame(configManager, gameModel, timerController);
            frame.setVisible(true);
        };

        SwingUtilities.invokeLater(runGui);
    }
}
