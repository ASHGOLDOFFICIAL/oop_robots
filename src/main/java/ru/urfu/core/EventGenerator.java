package ru.urfu.core;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>Обёртка над таймером для генерации событий.</p>
 */
public final class EventGenerator {
    private final Timer timer = initTimer();

    /**
     * <p>Инициализирует таймер.</p>
     *
     * @return таймер.
     */
    private Timer initTimer() {
        return new Timer("events generator", true);
    }

    /**
     * <p>Запланировать задачу.</p>
     *
     * @param task   задача.
     * @param delay  отложить на.
     * @param period периодичность.
     */
    public void schedule(TimerTask task, long delay, long period) {
        timer.schedule(task, delay, period);
    }
}
