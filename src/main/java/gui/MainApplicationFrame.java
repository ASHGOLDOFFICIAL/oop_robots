package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

import gui.menu.ExitMenuProvider;
import gui.menu.LookAndFeelMenuProvider;
import gui.menu.TestsMenuProvider;
import log.Logger;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;


public class MainApplicationFrame extends JFrame {
    private final I18n i18n = I18nFactory.getI18n(getClass());
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final MainApplicationFrameController controller = new MainApplicationFrameController();

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        final int inset = 50;
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        final LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        final GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setConfirmationBeforeExit();
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(i18n.tr("Logger is working"));
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    /**
     * <p>Создаёт меню.</p>
     *
     * @return созданное меню.
     */
    private JMenuBar generateMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(new LookAndFeelMenuProvider().provide(this, controller));
        menuBar.add(new TestsMenuProvider().provide(this, controller));
        menuBar.add(new ExitMenuProvider().provide(this, controller));
        return menuBar;
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
                controller.showExitConfirmationPopUp();
            }
        };
        addWindowListener(exitListener);
    }
}
