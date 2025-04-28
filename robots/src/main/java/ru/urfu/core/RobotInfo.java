package ru.urfu.core;

import ru.urfu.utils.Vector2;

/**
 * <p>Информация о положении робота.</p>
 *
 * @param position  позиция робота.
 * @param direction направление робота.
 */
public record RobotInfo(
        Vector2 position,
        double direction) {
}
