package ru.urfu.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import org.xnap.commons.i18n.I18nManager;
import org.xnap.commons.i18n.LocaleChangeEvent;
import org.xnap.commons.i18n.LocaleChangeListener;
import ru.urfu.core.GameModel;
import ru.urfu.gui.game.GuiGameController;
import ru.urfu.gui.game.GuiGameView;
import ru.urfu.state.Stateful;

/**
 * <p>Игровое окно.</p>
 */
public final class GameWindow extends JInternalFrame implements LocaleChangeListener, Stateful {
    private final GuiGameController controller;

    /**
     * <p>Конструктор.</p>
     *
     * @param model модель игры
     */
    public GameWindow(GameModel model) {
        super(null, true, true, true, true);

        final GuiGameView view = new GuiGameView(model);
        this.controller = new GuiGameController(model, view);
        this.controller.start();

        I18nManager.getInstance().addLocaleChangeListener(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(view, BorderLayout.CENTER);
        getContentPane().add(view);

        setLocaleDependantProperties();
        setControllerStopOnExit();

        final int width = 400;
        final int height = 400;
        setPreferredSize(new Dimension(width, height));

        final int x = 400;
        final int y = 10;
        setLocation(x, y);
        pack();
    }

    @Override
    public void localeChanged(LocaleChangeEvent event) {
        setLocaleDependantProperties();
    }

    /**
     * <p>Устанавливает свойства и поля,
     * зависящие от текущей локали.</p>
     */
    void setLocaleDependantProperties() {
        final I18n i18n = I18nFactory.getI18n(GameWindow.class);
        setTitle(i18n.tr("Game Field"));
    }

    /**
     * <p>Выключает перед выходом контроллер.</p>
     */
    private void setControllerStopOnExit() {
        final InternalFrameListener exitListener = new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                controller.stop();
                dispose();
            }
        };
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addInternalFrameListener(exitListener);
    }

    @Override
    public String getNameForStateService() {
        return "GameWindow";
    }
}
