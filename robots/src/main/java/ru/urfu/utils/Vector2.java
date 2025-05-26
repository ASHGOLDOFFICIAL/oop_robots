package ru.urfu.utils;

/**
 * <p>Двумерный вектор.</p>
 *
 * @param x первая компонента.
 * @param y вторая компонента.
 */
public record Vector2(double x, double y) {
    /**
     * <p>Дефолтный конструктор.</p>
     */
    public Vector2() {
        this(0, 0);
    }

    /**
     * <p>Длина вектора.</p>
     *
     * @return длину
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * <p>Квадрат длины вектора.</p>
     *
     * @return квадрат длины
     */
    public double lengthSquared() {
        return x * x + y * y;
    }

    /**
     * <p>Скалярное произведение.</p>
     *
     * @param other второй вектор
     * @return скалярное произведение
     */
    public double dot(Vector2 other) {
        return x * other.x + y * other.y;
    }

    /**
     * <p>Нормализация.</p>
     *
     * @return нормализованный вектор.
     */
    public Vector2 normalize() {
        final double length = length();
        return new Vector2(x / length, y / length);
    }

    /**
     * <p>Расстояние до другой точки.</p>
     *
     * @param to другая точка
     * @return расстояние
     */
    public double distance(Vector2 to) {
        return Math.sqrt(distanceSquared(to));
    }

    /**
     * <p>Квадрат расстояния до другой точки.</p>
     *
     * @param to другая точка
     * @return квадрат расстояния
     */
    public double distanceSquared(Vector2 to) {
        final Vector2 distanceVec = new Vector2(to.x - x, to.y - y);
        return distanceVec.lengthSquared();
    }

    /**
     * <p>Умножение на скаляр.</p>
     *
     * @param t скаляр.
     * @return результат умножения
     */
    public Vector2 scalar(double t) {
        return new Vector2(x * t, y * t);
    }

    /**
     * <p>Сложение векторов.</p>
     *
     * @param other другой вектор.
     * @return результат сложения.
     */
    public Vector2 plus(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    /**
     * <p>Вычитание векторов.</p>
     *
     * @param other другой вектор.
     * @return результат вычитания.
     */
    public Vector2 minus(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    /**
     * <p>Покомпонентное умножение.</p>
     *
     * @param other другой вектор
     * @return результат умножения.
     */
    public Vector2 mul(Vector2 other) {
        return new Vector2(x * other.x, y * other.y);
    }

    /**
     * <p>Покомпонентное деление.</p>
     *
     * @param other другой вектор.
     * @return результат деления.
     */
    public Vector2 divide(Vector2 other) {
        return new Vector2(x / other.x, y / other.y);
    }

    /**
     * <p>Каждая компонента берётся по модулю.</p>
     *
     * @return результат операции.
     */
    public Vector2 abs() {
        return new Vector2(Math.abs(x), Math.abs(y));
    }
}
