package ru.urfu.core;

import java.util.Timer;
import java.util.TimerTask;

public class EventGenerator {
    private final Timer timer = initTimer();
    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    public void schedule(TimerTask task, long delay, long period) {
        timer.schedule(task, delay, period);
    }
}
