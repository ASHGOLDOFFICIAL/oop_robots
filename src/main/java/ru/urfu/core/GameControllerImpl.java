package ru.urfu.core;

import java.awt.Point;

/**
 *
 */
public final class GameControllerImpl implements GameController {
    private final GameModel model;

    public GameControllerImpl(GameModel model) {
        this.model = model;
    }

    public void setTargetPosition(Point p) {
        model.setTargetPosition(p);
    }
}
