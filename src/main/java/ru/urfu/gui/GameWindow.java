package ru.urfu.gui;

import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import ru.urfu.core.EventGenerator;
import ru.urfu.core.GameController;
import ru.urfu.core.GameControllerImpl;
import ru.urfu.core.GameModel;
import ru.urfu.core.GameModelImpl;

public class GameWindow extends JInternalFrame {
    private static final I18n i18n = I18nFactory.getI18n(GameWindow.class);

    public GameWindow() {
        super(i18n.tr("Game Field"), true, true, true, true);

        final EventGenerator eventGenerator = new EventGenerator();
        final GameModel model = new GameModelImpl(eventGenerator);
        final GameController controller = new GameControllerImpl(model);

        final SwingGameView view = new SwingGameView(model, controller, eventGenerator);
        model.subscribeToUpdates(view);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(view, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
