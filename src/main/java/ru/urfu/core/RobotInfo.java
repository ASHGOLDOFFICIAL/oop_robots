package ru.urfu.core;

import ru.urfu.core.gamefield.GameFieldModel;

/**
 * <p>Информация о роботе. Класс нужен для того,
 * чтобы скрыть {@link RobotModel} от всех классов,
 * кроме {@link GameFieldModel}</p>
 *
 * @param positionX позиция робота по оси x.
 * @param positionY позиция робота по оси y.
 * @param direction направление робота.
 */
public record RobotInfo(
        double positionX,
        double positionY,
        double direction) {

    public RobotInfo(RobotModel model) {
        this(model.getPositionX(), model.getPositionY(), model.getDirection());
    }
}
