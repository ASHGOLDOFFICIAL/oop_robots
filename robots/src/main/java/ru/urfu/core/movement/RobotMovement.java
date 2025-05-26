package ru.urfu.core.movement;

import ru.urfu.core.GameModel;
import ru.urfu.utils.Vector2;

/**
 * <p>Логика передвижения робота.</p>
 */
public interface RobotMovement {
    /**
     * <p>Векторная скорость (направление, умноженное на скорость).</p>
     *
     * <p>Нулевой вектор воспринимается как отсутствие движения.</p>
     *
     * @param model модель для принятия решений.
     * @param time  время, прошедшее с последнего тика.
     * @return векторную скорость.
     */
    Vector2 velocity(GameModel model, int time);
}
