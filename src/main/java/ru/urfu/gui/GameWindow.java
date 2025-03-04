package ru.urfu.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import ru.urfu.config.Configuration;
import ru.urfu.core.GameModel;
import ru.urfu.gui.game.GuiGameController;
import ru.urfu.gui.game.GuiGameView;

/**
 * <p>Игровое окно.</p>
 */
public final class GameWindow extends CustomInternalFrame {
    private final GuiGameController controller;

    /**
     * <p>Конструктор.</p>
     *
     * @param config конфигурация
     * @param model  модель игры
     */
    public GameWindow(Configuration config, GameModel model) {
        super(config);

        final GuiGameView view = new GuiGameView(model);
        this.controller = new GuiGameController(model, view);
        this.controller.start();

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(view, BorderLayout.CENTER);
        getContentPane().add(view);

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
    }

    @Override
    protected void setLocaleDependantProperties() {
        final I18n i18n = I18nFactory.getI18n(GameWindow.class);
        setTitle(i18n.tr("Game Field"));
    }
}
