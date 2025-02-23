package ru.urfu.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.TimerTask;
import javax.swing.JPanel;
import ru.urfu.core.RobotInfo;
import ru.urfu.core.gamefield.EventGenerator;
import ru.urfu.core.gamefield.GameFieldController;
import ru.urfu.core.gamefield.GameFieldModel;
import ru.urfu.core.gamefield.GameFieldView;

/**
 * <p>Реализация интерфейса {@link GameFieldView} на Swing.</p>
 */
public final class SwingGameFieldView extends JPanel implements GameFieldView {
    private final GameFieldModel model;
    private final GameFieldController controller;

    public SwingGameFieldView(GameFieldModel model,
                              GameFieldController controller,
                              EventGenerator timer) {
        this.model = model;
        this.controller = controller;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                EventQueue.invokeLater(SwingGameFieldView.this::repaint);
            }
        }, 0, 50);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SwingGameFieldView.this.controller.setTargetPosition(e.getPoint());
            }
        });
        setDoubleBuffered(true);
    }

    @Override
    public void onModelUpdate() {
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        final RobotInfo robot = model.getRobot();

        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, round(robot.positionX()), round(robot.positionY()), robot.direction());
        drawTarget(g2d, model.getTargetPosition());
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
