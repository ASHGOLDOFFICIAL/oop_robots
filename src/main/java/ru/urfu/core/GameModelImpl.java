package ru.urfu.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Реализация интерфейса {@link GameModel}.</p>
 */
public final class GameModelImpl implements GameModel {
    private final static int GAME_CLOCK_PERIOD = 10;
    private final static double HALF_A_PIXEL = 0.5;
    private final static double EPSILON = 0.00001;

    private final Logger log = LoggerFactory.getLogger(GameModelImpl.class);
    private final Point initialPosition = new Point(100, 100);

    private final Timer timer = new Timer("Model Events Generator", true);
    private final RobotModel robot = new RobotModel(initialPosition.x, initialPosition.y, 0);
    private final List<GameModelChangeListener> listeners = new ArrayList<>();

    private final TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            update();
        }
    };

    private boolean isRunning = false;
    private Point targetPosition = new Point(initialPosition.x, initialPosition.y);

    /**
     * <p>Квадрат расстояния между двумя точками.</p>
     *
     * @param x1 x первой точки.
     * @param y1 y первой точки.
     * @param x2 x второй точки.
     * @param y2 y второй точки.
     * @return квадрат расстояния.
     */
    private static double distanceSquared(double x1, double y1, double x2, double y2) {
        double diffX = x2 - x1;
        double diffY = y2 - y1;
        return diffX * diffX + diffY * diffY;
    }

    /**
     * <p>Угол между ось x и прямой,
     * проходящей через две точки.</p>
     *
     * @param x1 x первой точки.
     * @param y1 y первой точки.
     * @param x2 x второй точки.
     * @param y2 y второй точки.
     * @return угол.
     */
    private static double angleTo(double x1, double y1, double x2, double y2) {
        double diffX = x2 - x1;
        double diffY = y2 - y1;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    /**
     * <p>Нормализация угла.</p>
     *
     * @param angle угол.
     * @return нормализация угла.
     */
    private static double asNormalizedRadians(double angle) {
        double newAngle = angle;
        while (newAngle < 0) {
            newAngle += 2 * Math.PI;
        }
        while (newAngle >= 2 * Math.PI) {
            newAngle -= 2 * Math.PI;
        }
        return newAngle;
    }

    @Override
    public void start() {
        this.isRunning = true;
        this.timer.schedule(updateTask, 0, GAME_CLOCK_PERIOD);
        log.debug("Model has started.");
    }

    @Override
    public void stop() {
        this.isRunning = false;
        updateTask.cancel();
        log.debug("Model has stopped.");
    }

    @Override
    public Point getTargetPosition() {
        return new Point(targetPosition.x, targetPosition.y);
    }

    @Override
    public void setTargetPosition(Point p) {
        this.targetPosition = new Point(p.x, p.y);
    }

    @Override
    public void registerListener(GameModelChangeListener listener) {
        this.listeners.add(listener);
        log.debug("Registered listener. Current listeners are {}", this.listeners);
    }

    @Override
    public void removeListener(GameModelChangeListener listener) {
        this.listeners.remove(listener);
        log.debug("Unregistered listener. Current listeners are {}", this.listeners);
    }

    @Override
    public RobotPosition getRobotPosition() {
        return new RobotPosition(robot);
    }

    /**
     * <p>Обновляет состояние модели.</p>
     */
    private void update() {
        if (!hasChanges()) {
            return;
        }
        moveRobot();
        notifyListeners();
    }

    /**
     * <p>Оповещает слушателей об изменении модели.</p>
     */
    private void notifyListeners() {
        for (final GameModelChangeListener listener : listeners) {
            listener.onModelChange();
        }
    }

    /**
     * <p>Проверка на то, есть ли изменения.</p>
     *
     * <p>Определяем через квадрат расстояния между
     * точкой назначения и текущим положением.</p>
     *
     * <p>Квадрат берём, чтобы не вычислять корень.</p>
     *
     * @return результат проверки.
     */
    private boolean hasChanges() {
        final double distance = distanceSquared(targetPosition.x, targetPosition.y,
                robot.getPositionX(), robot.getPositionY());
        return distance >= HALF_A_PIXEL;
    }

    /**
     * <p>Перемещает робота на поле.</p>
     */
    private void moveRobot() {
        double newDirection = calcNewDirection();
        robot.setDirection(newDirection);

        final double velocity = robot.getVelocity();

        final double oldX = robot.getPositionX();
        final double newX = oldX + velocity * GAME_CLOCK_PERIOD * Math.cos(newDirection);
        robot.setPositionX(newX);

        final double oldY = robot.getPositionY();
        final double newY = oldY + velocity * GAME_CLOCK_PERIOD * Math.sin(newDirection);
        robot.setPositionY(newY);
    }

    /**
     * <p>Вычисляет новое направления робота
     * на основании положения цели.</p>
     *
     * @return новое направление для робота.
     */
    private double calcNewDirection() {
        final double angleToTarget = angleTo(robot.getPositionX(), robot.getPositionY(),
                targetPosition.x, targetPosition.y);

        final double direction = robot.getDirection();
        double newDirection = direction;
        if (Math.abs(angleToTarget - direction) > EPSILON) {
            double angularVelocity = robot.getAngularVelocity();
            if (angleToTarget < direction) {
                angularVelocity = -robot.getAngularVelocity();
            }

            final double angleDelta = angularVelocity * GAME_CLOCK_PERIOD;
            newDirection = asNormalizedRadians(direction + angleDelta);
        }

        return newDirection;
    }
}
