package ru.urfu.core;

import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
    private final RobotModel robot = new RobotModel(initialPosition.x, initialPosition.y, 0);

    private final Timer timer = new Timer("Model Events Generator", true);
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            update();
        }
    };

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
        this.timer.schedule(updateTask, 0, GAME_CLOCK_PERIOD);
        log.debug("Model has started.");
    }

    @Override
    public void stop() {
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
    public void registerListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
        log.debug("Registered listener {}", listener.getClass().getSimpleName());
    }

    @Override
    public void removeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
        log.debug("Unregistered listener {}", listener.getClass().getSimpleName());
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
        this.pcs.firePropertyChange("model", null, null);
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
        final double angleDifference = asNormalizedRadians(angleToTarget - direction);

        if (angleDifference < EPSILON) {
            return direction;
        }

        double angularVelocity = robot.getAngularVelocity();
        angularVelocity *= (angleDifference < Math.PI) ? 1 : -1;
        angularVelocity *= (isInsideBlindZone(targetPosition)) ? -1 : 1;

        final double angleDelta = angularVelocity * GAME_CLOCK_PERIOD;
        return asNormalizedRadians(direction + angleDelta);
    }

    /**
     * <p>Проверяет, находится ли точка в слепой зоне робота.</p>
     * <p>Слепой зоной назовём внутренности окружности,
     * по которым робот может совершить круговое движение.</p>
     *
     * @param point проверяемая точка.
     * @return результат проверки.
     */
    private boolean isInsideBlindZone(Point point) {
        final double targetX = point.x;
        final double targetY = point.y;

        final double robotX = robot.getPositionX();
        final double robotY = robot.getPositionY();
        final double direction = robot.getDirection();
        final double radius = robot.getMovementCircumferenceRadius();

        final double radiusSquared = radius * radius;
        final double directionSin = Math.sin(direction);
        final double directionCos = Math.cos(direction);

        final double blindZone1CenterX = robotX - radius * directionSin;
        final double blindZone1CenterY = robotY + radius * directionCos;
        final double distance1 = distanceSquared(targetX, targetY, blindZone1CenterX, blindZone1CenterY);
        if (distance1 < radiusSquared) {
            return true;
        }

        final double blindZone2CenterX = robotX + radius * directionSin;
        final double blindZone2CenterY = robotY - radius * directionCos;
        final double distance2 = distanceSquared(targetX, targetY, blindZone2CenterX, blindZone2CenterY);
        return distance2 < radiusSquared;
    }
}
