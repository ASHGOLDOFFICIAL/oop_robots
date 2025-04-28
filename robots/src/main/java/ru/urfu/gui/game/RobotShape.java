package ru.urfu.gui.game;

import java.awt.Graphics2D;

/**
 * <p>Форма робота для рисования.</p>
 */
public interface RobotShape {
    /**
     * <p>Рисует робота в (0, 0), направление -- вдоль оси x.</p>
     *
     * @param g на чём рисовать.
     */
    void draw(Graphics2D g);
}
