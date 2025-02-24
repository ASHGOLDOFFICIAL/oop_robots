package ru.urfu.core;

import java.awt.Point;

/**
 * <p>Контроллер игрового поля.</p>
 */
public interface GameController {
    /**
     * <p>Устанавливает точку назначения модели.</p>
     * @param p точка назначения.
     */
    void setTargetPosition(Point p);
}
