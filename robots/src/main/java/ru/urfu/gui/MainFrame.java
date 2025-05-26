package ru.urfu.gui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Locale;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.slf4j.LoggerFactory;
import ru.urfu.config.ConfigSaveFailed;
import ru.urfu.config.ConfigurationManager;
import ru.urfu.core.GameModel;
import ru.urfu.core.GameTimerController;
import ru.urfu.core.movement.RobotMovement;
import ru.urfu.gui.game.RobotShape;
import ru.urfu.gui.menu.MainFrameMenu;
import ru.urfu.i18n.I18n;
import ru.urfu.i18n.I18nManager;
import ru.urfu.i18n.LocaleChangeListener;
import ru.urfu.i18n.LocaleChangedEvent;
import ru.urfu.log.Logger;
import ru.urfu.modding.Mod;
import ru.urfu.modding.ModLoader;
import ru.urfu.modding.ModLoaderException;
import ru.urfu.state.StateService;
import ru.urfu.state.Stateful;


/**
 * <p>Главное окно приложения.</p>
 */
public final class MainFrame extends JFrame implements LocaleChangeListener, Stateful {
    private final static int MIN_WIDTH = 300;
    private final static int MIN_HEIGHT = 800;
    private final static String DEFAULT_THEME = "javax.swing.plaf.nimbus.NimbusLookAndFeel";

    private final JDesktopPane desktopPane = new JDesktopPane();

    private final I18n i18n = I18nManager.getInstance().getI18n();
    private final org.slf4j.Logger log = LoggerFactory.getLogger(MainFrame.class);

    private final ModLoader modLoader = new ModLoader();
    private final StateService stateManager;
    private final ConfigurationManager configManager;
    private final GameTimerController controller;
    private final GameModel model;
    private WeakReference<GameWindow> gameWindow = new WeakReference<>(null);

    private final WindowListener exitListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            showExitConfirmationPopUp();
        }
    };

    /**
     * <p>Конструктор.</p>
     *
     * @param configManager менеджер конфигураций
     * @param model         модель игры
     * @param timer         контроллер-таймер
     */
    public MainFrame(ConfigurationManager configManager, GameModel model, GameTimerController timer) {
        log.debug("System locale is {}", Locale.getDefault());

        this.configManager = configManager;
        this.stateManager = new StateService(this.configManager.get());

        this.model = model;
        this.controller = timer;

        setContentPane(desktopPane);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setConfirmationBeforeExit();

        I18nManager.getInstance().addWeakLocaleChangeListener(this);

        openWindows();
        setLookAndFeel(DEFAULT_THEME);
        this.stateManager.restoreState(this, desktopPane);
        Logger.debug(i18n.tr("Logger is working"));
        setLocaleSpecificProperties();
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
            final GameWindow window = new GameWindow(this.model);
            this.gameWindow = new WeakReference<>(window);
            this.addWindow(window);
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

    /**
     * <p>Обрабатывает файл с модом.</p>
     *
     * @param modFile файл с модом.
     */
    public void handleModFile(File modFile) {
        try {
            final Mod mod = modLoader.load(modFile);
            final RobotMovement logic = mod.logic();
            if (logic != null) {
                model.changeRobotMovementLogic(logic);
            }

            final RobotShape shape = mod.shape();
            if (shape != null) {
                final GameWindow localGameWindow = this.gameWindow.get();
                if (localGameWindow != null) {
                    localGameWindow.changeRobotShape(shape);
                }
            }
        } catch (ModLoaderException e) {
            JOptionPane.showMessageDialog(
                    this,
                    i18n.tr("Couldn't load mod from given file."),
                    i18n.tr("Mod Loader Error"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public void onLocaleChanged(LocaleChangedEvent event) {
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
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

    @Override
    public String getNameForStateService() {
        return "MainFrame";
    }
}
