package ru.urfu.core;

import ru.urfu.utils.MathTools;
import ru.urfu.utils.Vector2;

/**
 * <p>Реализация логики передвижения робота по умолчанию.</p>
 */
public final class RobotMovementImpl implements RobotMovement {
    private final static double HALF_A_PIXEL = 0.5;
    private final static double EPSILON = 0.00001;
    private static final double MAX_VELOCITY = 0.1;
    private static final double MAX_ANGULAR_VELOCITY = 0.001;
    private static final double RADIUS = MAX_VELOCITY / MAX_ANGULAR_VELOCITY;

    private final Vector2 zero = new Vector2(0, 0);

    private GameModel model;

    @Override
    public Vector2 velocity(GameModel model, int time) {
        this.model = model;
        if (!hasChanges()) {
            return zero;
        }
        final double direction = calcNewDirection(time);
        final double x = MAX_VELOCITY * time * Math.cos(direction);
        final double y = MAX_VELOCITY * time * Math.sin(direction);
        return new Vector2(x, y);
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
        final double distance = model
                .getRobotInfo()
                .position()
                .distanceSquared(model.getTargetPosition());
        return distance >= HALF_A_PIXEL;
    }

    /**
     * <p>Вычисляет новое направления робота
     * на основании положения цели.</p>
     *
     * @param time время, прошедшее с последнего апдейта.
     * @return новое направление для робота.
     */
    private double calcNewDirection(int time) {
        final RobotInfo p = model.getRobotInfo();
        final Vector2 target = model.getTargetPosition();

        final double angleToTarget = MathTools.angleTo(p.position(), target);
        final double angleDifference = MathTools.asNormalizedRadians(angleToTarget - p.direction());

        if (angleDifference < EPSILON) {
            return p.direction();
        }

        double angularVelocity = MAX_ANGULAR_VELOCITY;
        angularVelocity *= (angleDifference > Math.PI) ? -1 : 1;
        angularVelocity *= (isInsideBlindZone(p, target)) ? -1 : 1;

        final double angleDelta = angularVelocity * time;
        return MathTools.asNormalizedRadians(p.direction() + angleDelta);
    }

    /**
     * <p>Проверяет, находится ли точка в слепой зоне робота.</p>
     * <p>Слепой зоной назовём внутренности окружности,
     * по которым робот может совершить круговое движение.</p>
     *
     * @param pos   информация о положении робота.
     * @param point проверяемая точка.
     * @return результат проверки.
     */
    private boolean isInsideBlindZone(RobotInfo pos, Vector2 point) {
        final double robotX = pos.position().x();
        final double robotY = pos.position().y();

        final double radiusSquared = RADIUS * RADIUS;
        final double directionSin = Math.sin(pos.direction());
        final double directionCos = Math.cos(pos.direction());

        final double blindZone1CenterX = robotX - RADIUS * directionSin;
        final double blindZone1CenterY = robotY + RADIUS * directionCos;
        final Vector2 blindZone1 = new Vector2(blindZone1CenterX, blindZone1CenterY);
        final double distance1 = point.distanceSquared(blindZone1);
        if (distance1 < radiusSquared) {
            return true;
        }

        final double blindZone2CenterX = robotX + RADIUS * directionSin;
        final double blindZone2CenterY = robotY - RADIUS * directionCos;
        final Vector2 blindZone2 = new Vector2(blindZone2CenterX, blindZone2CenterY);
        final double distance2 = point.distanceSquared(blindZone2);
        return distance2 < radiusSquared;
    }
}
