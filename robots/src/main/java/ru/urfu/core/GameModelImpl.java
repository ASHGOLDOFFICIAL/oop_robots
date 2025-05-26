package ru.urfu.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.core.level.Level;
import ru.urfu.core.level.LevelGenerator;
import ru.urfu.core.movement.ObstaclesModeMovement;
import ru.urfu.core.movement.RobotMovement;
import ru.urfu.core.movement.StandardModeMovement;
import ru.urfu.utils.MathTools;
import ru.urfu.utils.Vector2;

/**
 * <p>Реализация интерфейса {@link GameModel}.</p>
 */
public final class GameModelImpl implements GameModel {
    private final static String MODEL_STRING = "model";
    private final Vector2 zero = new Vector2();

    private final static int WIDTH = 50;
    private final static int HEIGHT = 50;
    private Level level = null;
    private final LevelGenerator levelGenerator = new LevelGenerator();

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final Logger log = LoggerFactory.getLogger(GameModelImpl.class);
    private final Vector2 initialPosition = new Vector2(2, 2);

    private final Object logicLock = new Object();
    private RobotMovement logic = new StandardModeMovement();
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
        this.pcs.firePropertyChange(MODEL_STRING, null, null);
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
    public Level getLevel() {
        return level;
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
        this.pcs.firePropertyChange(MODEL_STRING, null, null);
    }

    @Override
    public void reset() {
        resetSilently();
        this.pcs.firePropertyChange(MODEL_STRING, null, null);
    }

    @Override
    public void setObstaclesMode() {
        resetSilently();
        synchronized (logicLock) {
            logic = new ObstaclesModeMovement();
            level = levelGenerator.generate(WIDTH, HEIGHT);
            level.removeObstacle((int) initialPosition.x(), (int) initialPosition.y());
        }
        this.pcs.firePropertyChange(MODEL_STRING, null, null);
    }

    @Override
    public void changeRobotMovementLogic(RobotMovement logic) {
        resetSilently();
        synchronized (logicLock) {
            this.logic = logic;
        }
    }

    /**
     * <p>Сбрасывает модель до первоначального состояния без оповещения слушателей.</p>
     */
    private void resetSilently() {
        synchronized (logicLock) {
            logic = new StandardModeMovement();
            robotPosition = initialPosition;
            targetPosition = initialPosition;
            level = null;
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
