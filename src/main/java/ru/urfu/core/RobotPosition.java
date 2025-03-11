package ru.urfu.core;

/**
 * <p>Информация о положении робота.</p>
 *
 * @param positionX позиция робота по оси x.
 * @param positionY позиция робота по оси y.
 * @param direction направление робота.
 */
public record RobotPosition(
        double positionX,
        double positionY,
        double direction) {
}
