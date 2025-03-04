package ru.urfu.gui.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import ru.urfu.core.GameModel;
import ru.urfu.core.GameModelChangeListener;
import ru.urfu.core.RobotPosition;

/**
 * <p>View игры.</p>
 */
@SuppressWarnings({"MissingJavadocMethod", "MagicNumber"})
public final class GuiGameView extends JPanel implements GameModelChangeListener {
    private final GameModel model;

    /**
     * <p>Конструктор.</p>
     *
     * @param model модель игры.
     */
    public GuiGameView(GameModel model) {
        this.model = model;
        this.model.registerListener(this);
        setDoubleBuffered(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        final RobotPosition robot = model.getRobotPosition();

        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, round(robot.positionX()), round(robot.positionY()), robot.direction());
        drawTarget(g2d, model.getTargetPosition());
    }

    @Override
    public void onModelChange() {
        repaint();
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        int robotCenterX = round(x);
        int robotCenterY = round(y);

        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
    }

    private void drawTarget(Graphics2D g, Point p) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, p.x, p.y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, p.x, p.y, 5, 5);
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }
}
