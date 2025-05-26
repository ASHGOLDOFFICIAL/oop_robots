package ru.urfu.gui.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import ru.urfu.core.GameModel;
import ru.urfu.core.RobotInfo;
import ru.urfu.core.level.Level;
import ru.urfu.utils.Vector2;

/**
 * <p>View игры.</p>
 */
public final class GuiGameView extends JPanel implements PropertyChangeListener {
    public final static int SCALE = 16;
    private final GameModel model;
    private RobotShape robotShape = new RobotShapeImpl();

    /**
     * <p>Конструктор.</p>
     *
     * @param model модель игры.
     */
    public GuiGameView(GameModel model) {
        this.model = model;
        this.model.registerListener(this);
        setDoubleBuffered(true);
        repaint();
    }

    /**
     * <p>Рисует овал с заливкой.</p>
     *
     * @param g       графика
     * @param centerX x центра овала.
     * @param centerY y центра овала.
     * @param diam1   диаметр по x.
     * @param diam2   диаметр по y.
     */
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * <p>Рисует овал без заливки.</p>
     *
     * @param g       графика
     * @param centerX x центра овала.
     * @param centerY y центра овала.
     * @param diam1   диаметр по x.
     * @param diam2   диаметр по y.
     */
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * <p>Округление числа.</p>
     *
     * @param value число.
     * @return результат.
     */
    @SuppressWarnings("MagicNumber")
    private static int round(double value) {
        return (int) (value + 0.5);
    }

    /**
     * <p>Меняет логику отображения робота.</p>
     *
     * @param shape новый отрисовщик.
     */
    public void changeRobotShape(RobotShape shape) {
        robotShape = shape;
        repaint();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        final Level level = model.getLevel();
        if (level != null) {
            drawLevel(g2d, level);
        }

        final RobotInfo robot = model.getRobotInfo();
        final Vector2 pos = robot.position();
        drawRobot(g2d, round(pos.x() * SCALE), round(pos.y() * SCALE), robot.direction());
        final Vector2 targetPosition = model.getTargetPosition().scalar(SCALE);
        drawTarget(g2d, new Point(round(targetPosition.x()), round(targetPosition.y())));
    }

    /**
     * <p>Рисует робота.</p>
     *
     * @param g         графика для рисования.
     * @param x         положение по оси x
     * @param y         положение по оси y
     * @param direction направление робота
     */
    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        final AffineTransform old = g.getTransform();

        final AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        transform.rotate(direction);

        g.transform(transform);
        robotShape.draw(g);

        g.setTransform(old);
    }

    /**
     * <p>Рисует цель.</p>
     *
     * @param g графика.
     * @param p точка цели.
     */
    @SuppressWarnings("MagicNumber")
    private void drawTarget(Graphics2D g, Point p) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, p.x, p.y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, p.x, p.y, 5, 5);
    }

    /**
     * <p>Отрисовка уровня (поля).</p>
     *
     * @param g     графика.
     * @param level уровень (поле).
     */
    private void drawLevel(Graphics2D g, Level level) {
        final int width = level.getWidth();
        final int height = level.getHeight();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (level.hasObstacle(x, y)) {
                    drawObstacle(g, x, y);
                }
            }
        }
    }

    /**
     * <p>Рисует препятствие.</p>
     *
     * @param g графика.
     * @param x x координата препятствия.
     * @param y y координата препятствия.
     */
    private void drawObstacle(Graphics2D g, int x, int y) {
        g.setColor(Color.ORANGE);
        g.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
    }
}
