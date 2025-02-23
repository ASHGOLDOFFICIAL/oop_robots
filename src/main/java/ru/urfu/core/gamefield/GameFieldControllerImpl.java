package ru.urfu.core.gamefield;

import java.awt.Point;

/**
 *
 */
public final class GameFieldControllerImpl implements GameFieldController {
    private final GameFieldModel model;

    public GameFieldControllerImpl(GameFieldModel model) {
        this.model = model;
    }

    public void setTargetPosition(Point p) {
        model.setTargetPosition(p);
    }
}
