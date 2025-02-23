package ru.urfu.core.gamefield;

import java.awt.Point;
import ru.urfu.core.RobotInfo;

/**
 * <p>Модель игрового поля.</p>
 */
public interface GameFieldModel {
    /**
     * <p>Подписывает view на получение
     * уведомлений об обновлении модели.</p>
     * @param view который хотим подписать на обновления
     */
    void subscribeToUpdates(GameFieldView view);

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
