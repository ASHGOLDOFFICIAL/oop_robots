package ru.urfu.core.movement;

import java.util.List;
import ru.urfu.core.GameModel;
import ru.urfu.core.level.AStarPathFinder;
import ru.urfu.core.level.Level;
import ru.urfu.utils.Vector2;

/**
 * <p>Логика движение робота с поддержкой объезда препятствий.</p>
 */
public final class ObstaclesModeMovement implements RobotMovement {
    private final static double EPSILON = 0.05;
    private final static double SPEED = 0.01;
    private final Vector2 zero = new Vector2();

    private int pathIndex = 0;
    private AStarPathFinder pathFinder;
    private List<Vector2> path;
    private Vector2 currentTarget;

    @Override
    public Vector2 velocity(GameModel model, int time) {
        final Level level = model.getLevel();
        if (level == null) {
            return linearMovement(model, time);
        }

        if (pathFinder == null) {
            pathFinder = new AStarPathFinder(level);
        }
        return pathBasedMovement(model, time);
    }

    /**
     * <p>Движение по пути.</p>
     *
     * @param model модель.
     * @param time  дельта времени.
     * @return вектор движения.
     */
    private Vector2 pathBasedMovement(GameModel model, int time) {
        final Vector2 target = model.getTargetPosition();
        final Vector2 targetTile = floorVector(target);

        final Vector2 from = model.getRobotInfo().position();
        final Vector2 fromTile = floorVector(from);

        if (currentTarget == null || !currentTarget.equals(targetTile)) {
            currentTarget = targetTile;
            path = pathFinder.findPath(fromTile, targetTile);
            pathIndex = 0;
        }

        if (path == null || path.isEmpty() || pathIndex >= path.size()) {
            return zero;
        }

        final Vector2 next = path.get(pathIndex);
        if (next.equals(targetTile)) {
            return linearMovement(model, time);
        } else if (next.equals(fromTile)) {
            ++pathIndex;
            return zero;
        }

        return next.minus(fromTile).normalize().scalar(time * SPEED);
    }

    /**
     * <p>Движение по прямой.</p>
     *
     * @param model модель.
     * @param time  дельта времени.
     * @return вектор движения.
     */
    private Vector2 linearMovement(GameModel model, int time) {
        final Vector2 target = model.getTargetPosition();
        final Vector2 from = model.getRobotInfo().position();
        if (from.distanceSquared(target) < EPSILON) {
            return zero;
        }
        return target.minus(from).normalize().scalar(time * SPEED);
    }

    /**
     * Покомпонентное взятие целое части от вектора.
     *
     * @param vec вектор.
     * @return результат.
     */
    private Vector2 floorVector(Vector2 vec) {
        return new Vector2(Math.floor(vec.x()), Math.floor(vec.y()));
    }
}
