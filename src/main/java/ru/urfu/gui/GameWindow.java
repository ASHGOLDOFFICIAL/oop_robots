package ru.urfu.gui;

import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import ru.urfu.core.gamefield.EventGenerator;
import ru.urfu.core.gamefield.GameFieldController;
import ru.urfu.core.gamefield.GameFieldControllerImpl;
import ru.urfu.core.gamefield.GameFieldModel;
import ru.urfu.core.gamefield.GameFieldModelImpl;

public class GameWindow extends JInternalFrame {
    private static final I18n i18n = I18nFactory.getI18n(GameWindow.class);

    public GameWindow() {
        super(i18n.tr("Game Field"), true, true, true, true);

        final EventGenerator eventGenerator = new EventGenerator();
        final GameFieldModel model = new GameFieldModelImpl(eventGenerator);
        final GameFieldController controller = new GameFieldControllerImpl(model);

        final SwingGameFieldView view = new SwingGameFieldView(model, controller, eventGenerator);
        model.subscribeToUpdates(view);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(view, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
