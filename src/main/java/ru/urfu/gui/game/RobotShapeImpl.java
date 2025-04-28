package ru.urfu.gui.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * <p>Стандартное отображение робота.</p>
 */
public final class RobotShapeImpl implements RobotShape {
    @Override
    public void draw(Graphics2D g) {
        final Shape body = new Ellipse2D.Double(-15, -5, 30, 10);
        final Shape head = new Ellipse2D.Double(10, -2.5, 5, 5);

        g.setColor(Color.MAGENTA);
        g.fill(body);
        g.setColor(Color.BLACK);
        g.draw(body);

        g.setColor(Color.WHITE);
        g.fill(head);
        g.setColor(Color.BLACK);
        g.draw(head);
    }
}
