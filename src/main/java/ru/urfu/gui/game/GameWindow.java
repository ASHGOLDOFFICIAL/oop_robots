package ru.urfu.gui.game;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import ru.urfu.core.EventGenerator;
import ru.urfu.core.GameModel;
import ru.urfu.core.GameModelImpl;
import ru.urfu.gui.CustomInternalFrame;

/**
 * <p>Игровое окно.</p>
 */
public final class GameWindow extends CustomInternalFrame {
    private final static int WINDOW_WIDTH = 400;
    private final static int WINDOW_HEIGHT = 400;

    private final GameModel model;
    private final GuiGameView view;
    private final GuiGameController controller;

    /**
     * <p>Конструктор.</p>
     */
    public GameWindow() {
        final EventGenerator eventGenerator = new EventGenerator();
        this.model = new GameModelImpl(eventGenerator);
        this.view = new GuiGameView(model, eventGenerator);
        this.controller = new GuiGameController(model, view);

        model.start();
        view.start();
        controller.start();

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(view, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();

        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocaleDependantProperties();
    }

    @Override
    public void onDispose() {
        controller.stop();
        view.stop();
        model.stop();
    }

    @Override
    protected void setLocaleDependantProperties() {
        final I18n i18n = I18nFactory.getI18n(GameWindow.class);
        setTitle(i18n.tr("Game Field"));
    }
}
