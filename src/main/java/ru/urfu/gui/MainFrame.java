package ru.urfu.gui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import org.xnap.commons.i18n.I18nManager;
import org.xnap.commons.i18n.LocaleChangeEvent;
import org.xnap.commons.i18n.LocaleChangeListener;
import ru.urfu.config.ConfigSaveFailed;
import ru.urfu.config.Configuration;
import ru.urfu.config.ConfigurationManager;
import ru.urfu.config.ConfigurationSource;
import ru.urfu.config.FileConfigurationSource;
import ru.urfu.core.GameModel;
import ru.urfu.gui.menu.MainFrameMenu;
import ru.urfu.log.Logger;


/**
 * <p>Главное окно приложения.</p>
 */
public final class MainFrame extends JFrame implements LocaleChangeListener {
    private final static int MIN_WIDTH = 300;
    private final static int MIN_HEIGHT = 800;

    private final static String DEFAULT_THEME = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
    private final static String CONFIG_FILE = System.getProperty("user.home") + "/robots.properties";
    private final static String THEME_KEY = "themeClass";

    private final JDesktopPane desktopPane = new JDesktopPane();

    private final I18n i18n = I18nFactory.getI18n(MainFrame.class);
    private final org.slf4j.Logger log = LoggerFactory.getLogger(MainFrame.class);

    private final ConfigurationManager configManager;
    private final Configuration config;
    private final GameModel model;

    private final WindowListener exitListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            showExitConfirmationPopUp();
        }
    };

    /**
     * <p>Конструктор.</p>
     *
     * @param gameModel модель игры
     */
    public MainFrame(GameModel gameModel) {
        log.debug("System locale is {}", Locale.getDefault());
        log.debug("Configuration file is {}", CONFIG_FILE);

        final ConfigurationSource configurationSource = new FileConfigurationSource(CONFIG_FILE);
        this.configManager = new ConfigurationManager(configurationSource);
        this.config = this.configManager.get();

        this.model = gameModel;
        this.model.start();

        I18nManager.getInstance().addLocaleChangeListener(this);

        setConfirmationBeforeExit();
        setLocaleSpecificProperties();

        setContentPane(desktopPane);

        initialState();
    }

    /**
     * <p>Настройка изначального состояния приложения.</p>
     */
    private void initialState() {
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        final String themeClass = config.get(THEME_KEY, DEFAULT_THEME);
        setLookAndFeel(themeClass);
        new WindowConfigurationManager(config).configureComponent(this);
        addLogWindowIfClosed();
        addGameWindowIfClosed();
        addCoordinatesWindowIfClosed();
    }

    /**
     * <p>Проверяет, закрыто ли окно.</p>
     *
     * @param clazz класс окна
     * @return результат проверки
     */
    private boolean isWindowClosed(Class<?> clazz) {
        for (final JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame.getClass() == clazz) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Добавляет окно с игрой, если то уже не открыто.</p>
     */
    public void addGameWindowIfClosed() {
        if (isWindowClosed(GameWindow.class)) {
            final GameWindow gameWindow = new GameWindow(this.config, this.model);
            this.addWindow(gameWindow);
        }
    }

    /**
     * <p>Добавляет окно с логами, если то уже не открыто.</p>
     */
    public void addLogWindowIfClosed() {
        if (isWindowClosed(LogWindow.class)) {
            final LogWindow logWindow = new LogWindow(this.config, Logger.getDefaultLogSource());
            this.addWindow(logWindow);
        }
    }

    /**
     * <p>Добавляет окно с координатами робота, если то уже не открыто.</p>
     */
    public void addCoordinatesWindowIfClosed() {
        if (isWindowClosed(CoordinatesWindow.class)) {
            final CoordinatesWindow coordinatesWindow = new CoordinatesWindow(this.config, this.model);
            this.addWindow(coordinatesWindow);
        }
    }

    /**
     * <p>Открывает всплывающее окно
     * с запросом подтверждения выхода.</p>
     */
    private void showExitConfirmationPopUp() {
        final Object[] options = {i18n.tr("Yes"), i18n.tr("No")};
        final int result = JOptionPane.showOptionDialog(
                null,
                i18n.tr("Are you sure you want to close application?"),
                i18n.tr("Exit Confirmation"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (result == JOptionPane.YES_OPTION) {
            onClose();
        }
    }

    /**
     * <p>Устанавливает системную тему оформления.</p>
     */
    public void setSystemLookAndFeel() {
        setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        this.invalidate();
    }

    /**
     * <p>Устанавливает универсальную тему оформления.</p>
     */
    public void setCrossPlatformLookAndFeel() {
        setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        this.invalidate();
    }

    /**
     * <p>Добавляет внутреннее окно.</p>
     *
     * @param frame добавляемое окно.
     */
    private void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }


    /**
     * <p>Меняет тему оформления.</p>
     *
     * @param className имя класс новой темы.
     */
    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            log.error("Error during setting application theme", e);
        }
        config.put(THEME_KEY, className);
    }

    /**
     * <p>Добавляет наблюдателя за событием закрытия окна теперь
     * перед выходом предлагается подтвердить намерение.</p>
     */
    private void setConfirmationBeforeExit() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this.exitListener);
    }

    @Override
    public void localeChanged(LocaleChangeEvent event) {
        setLocaleSpecificProperties();
    }

    /**
     * <p>Устанавливает свойства и поля,
     * зависящие от текущей Locale.</p>
     */
    private void setLocaleSpecificProperties() {
        setJMenuBar(new MainFrameMenu(this));
    }

    /**
     * <p>Действия при выходе.</p>
     */
    private void onClose() {
        this.model.stop();

        for (final JInternalFrame frame : this.desktopPane.getAllFrames()) {
            frame.dispose();
        }
        dispose();

        try {
            new WindowConfigurationManager(config).saveComponentState(this);
            this.configManager.flush();
        } catch (ConfigSaveFailed e) {
            log.error("Couldn't save application state");
        }
    }
}
