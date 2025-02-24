package ru.urfu.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * <p>Реализация интерфейса {@link GameModel}</p>
 */
public final class GameModelImpl implements GameModel {
    private final List<GameView> listeners = new ArrayList<>();
    private final RobotModel robot = new RobotModel(100, 100, 0);
    private Point targetPosition = new Point(100, 100);

    /**
     * <p>Конструктор.</p>
     * @param timer таймер для генерации событий.
     */
    public GameModelImpl(EventGenerator timer) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                final boolean hasUpdates = updateModel();
                if (hasUpdates) {
                    notifySubscribers();
                }
            }
        }, 0, 10);
    }

    @Override
    public void subscribeToUpdates(GameView view) {
        listeners.add(view);
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
    public RobotInfo getRobot() {
        return new RobotInfo(robot);
    }

    /**
     * <p>Оповещает подписавшихся на обновления об изменении модели.</p>
     */
    private void notifySubscribers() {
        for (final GameView view : listeners) {
            view.onModelUpdate();
        }
    }

    /**
     * <p>Обновляет состояние модели.</p>
     * @return были ли какие-то изменения.
     */
    private boolean updateModel() {
        if (!hasChanges()) {
            return false;
        }
        moveRobot();
        return true;
    }

    /**
     * <p>Проверка на то, есть ли изменения.</p>
     * @return результат проверки.
     */
    private boolean hasChanges() {
        final double distance = distance(targetPosition.x, targetPosition.y,
                robot.getPositionX(), robot.getPositionY());
        return distance >= 0.5;
    }

    /**
     * <p>Перемещает робота на поле.</p>
     */
    private void moveRobot() {
        final double duration = 10;
        final double robotPositionX = robot.getPositionX();
        final double robotPositionY = robot.getPositionY();
        final double robotDirection = robot.getDirection();
        final double velocity = robot.getVelocity();

        final double angleToTarget = angleTo(robot.getPositionX(), robot.getPositionY(),
                targetPosition.x, targetPosition.y);

        double angularVelocity = 0;
        if (angleToTarget > robot.getDirection()) {
            angularVelocity = robot.getAngularVelocity();
        }
        if (angleToTarget < robot.getDirection()) {
            angularVelocity = -robot.getAngularVelocity();
        }

        double newX = robotPositionX + velocity / angularVelocity *
                (Math.sin(robotDirection + angularVelocity * duration) -
                        Math.sin(robotDirection));
        if (!Double.isFinite(newX)) {
            newX = robotPositionX + velocity * duration * Math.cos(robotDirection);
        }

        double newY = robotPositionY - velocity / angularVelocity *
                (Math.cos(robotDirection + angularVelocity * duration) -
                        Math.cos(robotDirection));
        if (!Double.isFinite(newY)) {
            newY = robotPositionY + velocity * duration * Math.sin(robotDirection);
        }

        double newDirection = asNormalizedRadians(robotDirection + angularVelocity * duration);

        robot.setPositionX(newX);
        robot.setPositionY(newY);
        robot.setDirection(newDirection);
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x2 - x1;
        double diffY = y2 - y1;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double x1, double y1, double x2, double y2) {
        double diffX = x2 - x1;
        double diffY = y2 - y1;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }
}
