package ru.urfu.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Timer;
import javax.swing.JPanel;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import ru.urfu.config.Configuration;
import ru.urfu.core.GameModel;
import ru.urfu.core.GameModelImpl;
import ru.urfu.gui.game.GuiGameController;
import ru.urfu.gui.game.GuiGameView;

/**
 * <p>Игровое окно.</p>
 */
public final class GameWindow extends CustomInternalFrame {
    private final Timer timer = new Timer("Game Events Generator", true);
    private final GameModel model;
    private final GuiGameView view;
    private final GuiGameController controller;

    /**
     * <p>Конструктор.</p>
     *
     * @param config конфигурация
     */
    public GameWindow(Configuration config) {
        super(config);

        this.model = new GameModelImpl(timer);
        this.view = new GuiGameView(model, timer);
        this.controller = new GuiGameController(model, view);

        model.start();
        view.start();
        controller.start();

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(view, BorderLayout.CENTER);
        getContentPane().add(panel);

        setLocaleDependantProperties();
    }

    @Override
    protected Dimension defaultSize() {
        final int windowWidth = 400;
        final int windowHeight = 400;
        return new Dimension(windowWidth, windowHeight);
    }

    @Override
    public void onDispose() {
        controller.stop();
        view.stop();
        model.stop();
        timer.cancel();
    }

    @Override
    protected void setLocaleDependantProperties() {
        final I18n i18n = I18nFactory.getI18n(GameWindow.class);
        setTitle(i18n.tr("Game Field"));
    }
}
