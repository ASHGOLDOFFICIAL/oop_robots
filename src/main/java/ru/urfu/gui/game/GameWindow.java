package ru.urfu.gui.game;

import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import org.xnap.commons.i18n.I18nManager;
import org.xnap.commons.i18n.LocaleChangeEvent;
import org.xnap.commons.i18n.LocaleChangeListener;
import ru.urfu.core.EventGenerator;
import ru.urfu.core.GameModel;
import ru.urfu.core.GameModelImpl;

/**
 * <p>Игровое окно.</p>
 */
public final class GameWindow extends JInternalFrame implements LocaleChangeListener {
    private final static int WINDOW_WIDTH = 400;
    private final static int WINDOW_HEIGHT = 400;

    private final GameModel model;
    private final GuiGameView view;
    private final GuiGameController controller;

    /**
     * <p>Конструктор.</p>
     */
    public GameWindow() {
        super(null, true, true, true, true);

        final EventGenerator eventGenerator = new EventGenerator();
        this.model = new GameModelImpl(eventGenerator);
        this.view = new GuiGameView(model, eventGenerator);
        this.controller = new GuiGameController(model, view);

        model.start();
        view.start();
        controller.start();

        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                controller.stop();
                view.stop();
                model.stop();
            }
        });

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(view, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();

        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        I18nManager.getInstance().addLocaleChangeListener(this);
        setLocaleSpecificProperties();
    }

    @Override
    public void localeChanged(LocaleChangeEvent event) {
        setLocaleSpecificProperties();
    }

    /**
     * <p>Устанавливает свойства и поля,
     * зависящие от текущей Locale.</p>
     */
    private void setLocaleSpecificProperties() {
        final I18n i18n = I18nFactory.getI18n(GameWindow.class);
        setTitle(i18n.tr("Game Field"));
    }
}
