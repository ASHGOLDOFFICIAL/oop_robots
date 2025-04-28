package ru.urfu.utils;

/**
 * <p>Набор статических математических методов.</p>
 */
public final class MathTools {
    /**
     * <p>Приватный конструктор утилитарного класса.</p>
     */
    private MathTools() {
    }

    /**
     * <p>Угол между ось x и прямой,
     * проходящей через две точки.</p>
     *
     * @param v1 первый вектор
     * @param v2 второй вектор
     * @return угол.
     */
    public static double angleTo(Vector2 v1, Vector2 v2) {
        final Vector2 diff = v2.minus(v1);
        return asNormalizedRadians(Math.atan2(diff.y(), diff.x()));
    }

    /**
     * <p>Нормализация угла.</p>
     *
     * @param angle угол.
     * @return нормализация угла.
     */
    public static double asNormalizedRadians(double angle) {
        double newAngle = angle;
        while (newAngle < 0) {
            newAngle += 2 * Math.PI;
        }
        while (newAngle >= 2 * Math.PI) {
            newAngle -= 2 * Math.PI;
        }
        return newAngle;
    }
}
