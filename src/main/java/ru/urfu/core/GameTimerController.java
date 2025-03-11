package ru.urfu.core;

import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Контроллер, запускающий игровой цикл.</p>
 */
public final class GameTimerController {
    private final static int GAME_CLOCK_PERIOD = 10;

    private final Logger log = LoggerFactory.getLogger(GameTimerController.class);
    private final Timer timer = new Timer("Model Events Generator", true);
    private final TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            model.update(GAME_CLOCK_PERIOD);
        }
    };

    private final GameModel model;

    /**
     * <p>Конструктор.</p>
     *
     * @param model изменяемая модель.
     */
    public GameTimerController(GameModel model) {
        this.model = model;
    }

    /**
     * <p>Запускает игровой цикл: с некоторой периодичностью
     * модель обновляет своё состояние.</p>
     */
    public void start() {
        this.timer.schedule(updateTask, 0, GAME_CLOCK_PERIOD);
        log.debug("Game timer controller has started.");
    }

    /**
     * <p>Останавливает игровой цикл: модель
     * больше не обновляет своё состояние.</p>
     */
    public void stop() {
        updateTask.cancel();
        log.debug("Game timer controller has stopped.");
    }
}
