package ru.urfu.core;

import java.awt.Point;

/**
 * <p>Модель игрового поля.</p>
 */
public interface GameModel {
    /**
     * <p>Подписывает view на получение
     * уведомлений об обновлении модели.</p>
     * @param view который хотим подписать на обновления
     */
    void subscribeToUpdates(GameView view);

    /**
     * <p>Геттер информации о роботе.</p>
     * @return информация о роботе.
     */
    RobotInfo getRobot();

    /**
     * <p>Геттер положения цели.</p>
     * @return положение цели.
     */
    Point getTargetPosition();

    /**
     * <p>Сеттер положения цели.</p>
     * @param p новое положение цели.
     */
    void setTargetPosition(Point p);
}
