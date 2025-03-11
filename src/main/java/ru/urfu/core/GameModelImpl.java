package ru.urfu.core;

import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Реализация интерфейса {@link GameModel}.</p>
 */
public final class GameModelImpl implements GameModel {
    private final static double HALF_A_PIXEL = 0.5;
    private final static double EPSILON = 0.00001;
    private static final double MAX_VELOCITY = 0.1;
    private static final double MAX_ANGULAR_VELOCITY = 0.001;
    private static final double RADIUS = MAX_VELOCITY / MAX_ANGULAR_VELOCITY;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final Logger log = LoggerFactory.getLogger(GameModelImpl.class);
    private final Point initialPosition = new Point(100, 100);

    private Point targetPosition = new Point(initialPosition.x, initialPosition.y);
    private double robotX = initialPosition.x;
    private double robotY = initialPosition.y;
    private double robotDirection = 0;

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
        return new RobotPosition(robotX, robotY, robotDirection);
    }

    @Override
    public void update(int time) {
        if (!hasChanges()) {
            return;
        }
        moveRobot(time);
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
        final double distance = distanceSquared(targetPosition.x, targetPosition.y, robotX, robotY);
        return distance >= HALF_A_PIXEL;
    }

    /**
     * <p>Перемещает робота на поле.</p>
     *
     * @param time время, прошедшее с последнего апдейта.
     */
    private void moveRobot(int time) {
        robotDirection = calcNewDirection(time);
        robotX = robotX + MAX_VELOCITY * time * Math.cos(robotDirection);
        robotY = robotY + MAX_VELOCITY * time * Math.sin(robotDirection);
    }

    /**
     * <p>Вычисляет новое направления робота
     * на основании положения цели.</p>
     *
     * @param time время, прошедшее с последнего апдейта.
     * @return новое направление для робота.
     */
    private double calcNewDirection(int time) {
        final double angleToTarget = angleTo(robotX, robotY, targetPosition.x, targetPosition.y);
        final double angleDifference = asNormalizedRadians(angleToTarget - robotDirection);

        if (angleDifference < EPSILON) {
            return robotDirection;
        }

        double angularVelocity = MAX_ANGULAR_VELOCITY;
        angularVelocity *= (angleDifference < Math.PI) ? 1 : -1;
        angularVelocity *= (isInsideBlindZone(targetPosition)) ? -1 : 1;

        final double angleDelta = angularVelocity * time;
        return asNormalizedRadians(robotDirection + angleDelta);
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

        final double radiusSquared = RADIUS * RADIUS;
        final double directionSin = Math.sin(robotDirection);
        final double directionCos = Math.cos(robotDirection);

        final double blindZone1CenterX = robotX - RADIUS * directionSin;
        final double blindZone1CenterY = robotY + RADIUS * directionCos;
        final double distance1 = distanceSquared(targetX, targetY, blindZone1CenterX, blindZone1CenterY);
        if (distance1 < radiusSquared) {
            return true;
        }

        final double blindZone2CenterX = robotX + RADIUS * directionSin;
        final double blindZone2CenterY = robotY - RADIUS * directionCos;
        final double distance2 = distanceSquared(targetX, targetY, blindZone2CenterX, blindZone2CenterY);
        return distance2 < radiusSquared;
    }
}
