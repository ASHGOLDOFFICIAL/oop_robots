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
import ru.urfu.config.ConfigurationManager;
import ru.urfu.config.ConfigurationSource;
import ru.urfu.config.FileConfigurationSource;
import ru.urfu.core.GameModel;
import ru.urfu.core.GameTimerController;
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

    private final JDesktopPane desktopPane = new JDesktopPane();

    private final I18n i18n = I18nFactory.getI18n(MainFrame.class);
    private final org.slf4j.Logger log = LoggerFactory.getLogger(MainFrame.class);

    private final StateManager stateManager;
    private final ConfigurationManager configManager;
    private final GameTimerController controller;
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
    public MainFrame(GameModel gameModel, GameTimerController timer) {
        log.debug("System locale is {}", Locale.getDefault());
        log.debug("Configuration file is {}", CONFIG_FILE);

        final ConfigurationSource configurationSource = new FileConfigurationSource(CONFIG_FILE);
        this.configManager = new ConfigurationManager(configurationSource);
        this.stateManager = new StateManager(this.configManager.get());

        this.model = gameModel;
        this.controller = timer;

        setContentPane(desktopPane);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setConfirmationBeforeExit();

        I18nManager.getInstance().addLocaleChangeListener(this);

        initialState();
        setLocaleSpecificProperties();
    }

    /**
     * <p>Настройка изначального состояния приложения.</p>
     */
    private void initialState() {
        openWindows();
        setLookAndFeel(DEFAULT_THEME);
        this.stateManager.restoreState(this, desktopPane);
    }

    /**
     * <p>Открывает необходимые окна.</p>
     */
    private void openWindows() {
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
            final GameWindow gameWindow = new GameWindow(this.model);
            this.addWindow(gameWindow);
        }
    }

    /**
     * <p>Добавляет окно с логами, если то уже не открыто.</p>
     */
    public void addLogWindowIfClosed() {
        if (isWindowClosed(LogWindow.class)) {
            final LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
            this.addWindow(logWindow);
        }
    }

    /**
     * <p>Добавляет окно с координатами робота, если то уже не открыто.</p>
     */
    public void addCoordinatesWindowIfClosed() {
        if (isWindowClosed(CoordinatesWindow.class)) {
            final CoordinatesWindow coordinatesWindow = new CoordinatesWindow(this.model);
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

    @Override
    public void localeChanged(LocaleChangeEvent event) {
        setLocaleSpecificProperties();
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
    }

    /**
     * <p>Добавляет наблюдателя за событием закрытия окна теперь
     * перед выходом предлагается подтвердить намерение.</p>
     */
    private void setConfirmationBeforeExit() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this.exitListener);
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
        this.controller.stop();

        try {
            this.stateManager.saveState(this, desktopPane);
            this.configManager.flush();
        } catch (ConfigSaveFailed e) {
            log.error("Couldn't save application state");
        }
        dispose();
    }
}
