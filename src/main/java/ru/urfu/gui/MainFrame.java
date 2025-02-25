package ru.urfu.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import ru.urfu.gui.game.GameWindow;
import ru.urfu.gui.menu.ExitMenuProvider;
import ru.urfu.gui.menu.LookAndFeelMenuProvider;
import ru.urfu.gui.menu.TestsMenuProvider;
import ru.urfu.gui.menu.WindowsMenuProvider;
import ru.urfu.log.Logger;


/**
 * <p>Главное окно приложения.</p>
 */
public final class MainFrame extends JFrame {
    private final I18n i18n = I18nFactory.getI18n(MainFrame.class);
    private final org.slf4j.Logger log = LoggerFactory.getLogger(MainFrame.class);
    private final JDesktopPane desktopPane = new JDesktopPane();

    /**
     * <p>Конструктор.</p>
     */
    public MainFrame() {
        // Make the big window be indented 50 pixels
        // from each edge of the screen.
        final int inset = 50;
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);
        setJMenuBar(generateMenuBar());
        setConfirmationBeforeExit();

        initialState();
    }

    /**
     * <p>Создаёт меню.</p>
     *
     * @return созданное меню.
     */
    private JMenuBar generateMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(new WindowsMenuProvider().provide(this));
        menuBar.add(new LookAndFeelMenuProvider().provide(this));
        menuBar.add(new TestsMenuProvider().provide(this));
        menuBar.add(new ExitMenuProvider().provide(this));
        return menuBar;
    }

    /**
     * <p>Настройка изначального состояния приложения.</p>
     */
    private void initialState() {
        setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        addLogWindow();
        addGameWindow();
    }

    /**
     * <p>Добавляет окно с игрой.</p>
     */
    public void addGameWindow() {
        final GameWindow gameWindow = new GameWindow();
        this.addWindow(gameWindow);
    }

    /**
     * <p>Добавляет окно с логами.</p>
     */
    public void addLogWindow() {
        final LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        this.setMinimumSize(logWindow.getSize());
        Logger.debug(i18n.tr("Logger is working"));
        this.addWindow(logWindow);
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

        Logger.debug(i18n.tr("Exit confirmation"));

        switch (result) {
            case JOptionPane.YES_OPTION -> {
                Logger.debug(i18n.tr("Exit is confirmed"));
                closeMainWindow();
            }
            case JOptionPane.NO_OPTION -> Logger.debug(i18n.tr("Exit is cancelled"));
            default -> Logger.debug(i18n.tr("Unknown option"));
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
    }

    /**
     * <p>Закрывает главное окно.</p>
     */
    private void closeMainWindow() {
        setVisible(false);
        dispose();
        System.exit(0);
    }

    /**
     * <p>Добавляет наблюдателя за событием закрытия окна теперь
     * перед выходом предлагается подтвердить намерение.</p>
     */
    private void setConfirmationBeforeExit() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        final WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showExitConfirmationPopUp();
            }
        };
        addWindowListener(exitListener);
    }
}
