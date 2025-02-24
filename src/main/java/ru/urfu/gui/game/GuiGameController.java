package ru.urfu.gui.game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.core.GameModel;


/**
 * <p>Контроллер для {@link GuiGameView}.</p>
 */
public final class GuiGameController {
    private final Logger log = LoggerFactory.getLogger(GuiGameController.class);
    private final GuiGameView view;
    private final MouseListener mouseListener;

    /**
     * <p>Конструктор.</p>
     *
     * @param model модель, с которой контроллер будет работать.
     * @param view  GUI-view, от которого контроллер будет получать события.
     */
    public GuiGameController(GameModel model, GuiGameView view) {
        this.view = view;
        this.mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setTargetPosition(e.getPoint());
            }
        };
    }

    /**
     * <p>Запускает контроллер:
     * начинает просушивать события мыши.</p>
     */
    public void start() {
        view.addMouseListener(mouseListener);
        log.debug("Controller has started.");
    }

    /**
     * <p>Останавливает контроллер:
     * перестаёт прослушивать события мыши.</p>
     */
    public void stop() {
        view.removeMouseListener(mouseListener);
        log.debug("Controller has stopped.");
    }
}
