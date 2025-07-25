package ru.urfu.core;

import java.beans.PropertyChangeListener;
import ru.urfu.core.level.Level;
import ru.urfu.core.movement.RobotMovement;
import ru.urfu.utils.Vector2;

/**
 * <p>Модель игры.</p>
 */
public interface GameModel {
    /**
     * <p>Обновляет состояние модели.</p>
     *
     * @param time время, на которое продвигаем модель.
     */
    void update(int time);

    /**
     * <p>Меняет логику передвижения робота.</p>
     *
     * @param logic новая логика передвижения
     */
    void changeRobotMovementLogic(RobotMovement logic);

    /**
     * <p>Геттер информации о положении робота.</p>
     *
     * @return информация о положении робота.
     */
    RobotInfo getRobotInfo();

    /**
     * <p>Геттер положения цели.</p>
     *
     * @return положение цели.
     */
    Vector2 getTargetPosition();

    /**
     * <p>Сеттер положения цели.</p>
     *
     * @param p новое положение цели.
     */
    void setTargetPosition(Vector2 p);

    /**
     * <p>Регистрирует слушателя изменения модели.</p>
     *
     * @param listener слушатель
     */
    void registerListener(PropertyChangeListener listener);

    /**
     * <p>Убирает слушателя изменения модели.</p>
     *
     * @param listener слушатель
     */
    void removeListener(PropertyChangeListener listener);

    /**
     * <p>Возвращает модель в первоначальное состояние.</p>
     */
    void reset();

    /**
     * <p>Включает режим с препятствиями. Робот движется линейно.</p>
     */
    void setObstaclesMode();

    /**
     * <p>Возвращает текущее поле, если есть.</p>
     *
     * @return поле игры с препятствиями.
     */
    Level getLevel();
}
