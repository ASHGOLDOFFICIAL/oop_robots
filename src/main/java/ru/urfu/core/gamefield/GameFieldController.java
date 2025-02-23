package ru.urfu.core.gamefield;

import java.awt.Point;

/**
 * <p>Контроллер игрового поля.</p>
 */
public interface GameFieldController {
    /**
     * <p>Устанавливает точку назначения модели.</p>
     * @param p точка назначения.
     */
    void setTargetPosition(Point p);
}
