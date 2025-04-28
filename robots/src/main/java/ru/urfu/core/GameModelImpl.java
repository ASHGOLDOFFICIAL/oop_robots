package ru.urfu.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.utils.MathTools;
import ru.urfu.utils.Vector2;

/**
 * <p>Реализация интерфейса {@link GameModel}.</p>
 */
public final class GameModelImpl implements GameModel {
    private final Vector2 zero = new Vector2(0, 0);

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final Logger log = LoggerFactory.getLogger(GameModelImpl.class);
    private final Vector2 initialPosition = new Vector2(100, 100);

    private final Object logicLock = new Object();
    private RobotMovement logic = new RobotMovementImpl();
    private Vector2 targetPosition = initialPosition;
    private Vector2 robotPosition = initialPosition;
    private double robotDirection = 0;

    @Override
    public Vector2 getTargetPosition() {
        return targetPosition;
    }

    @Override
    public void setTargetPosition(Vector2 p) {
        this.targetPosition = p;
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
    public RobotInfo getRobotInfo() {
        return new RobotInfo(robotPosition, robotDirection);
    }

    @Override
    public void update(int time) {
        if (!moveRobot(time)) {
            return;
        }
        this.pcs.firePropertyChange("model", null, null);
    }

    @Override
    public void changeRobotMovementLogic(RobotMovement logic) {
        synchronized (logicLock) {
            this.logic = logic;
        }
    }

    /**
     * <p>Перемещает робота на поле.</p>
     *
     * @param time время, прошедшее с последнего апдейта.
     * @return двинулся ли робот.
     */
    private boolean moveRobot(int time) {
        Vector2 velocity;
        synchronized (logicLock) {
            velocity = logic.velocity(this, time);
        }

        if (velocity.equals(zero)) {
            return false;
        }
        robotPosition = robotPosition.plus(velocity);
        robotDirection = MathTools
                .asNormalizedRadians(Math.atan2(velocity.y(), velocity.x()));
        return true;
    }
}
