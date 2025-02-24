package ru.urfu.core;

/**
 * <p>Модель робота.</p>
 */
public final class RobotModel {
    private static final double MAX_VELOCITY = 0.1;
    private static final double MAX_ANGULAR_VELOCITY = 0.001;

    private volatile double positionX;
    private volatile double positionY;
    private volatile double direction;

    /**
     * <p>Конструктор.</p>
     *
     * @param initialX  изначальная координата по оси x.
     * @param initialY  изначальная координата по оси y.
     * @param direction изначальное направление.
     */
    public RobotModel(double initialX, double initialY, double direction) {
        this.positionX = initialX;
        this.positionY = initialY;
        this.direction = direction;
    }

    /**
     * <p>Геттер координаты по оси x.</p>
     *
     * @return координата по оси x.
     */
    public double getPositionX() {
        return positionX;
    }

    /**
     * <p>Сеттер координаты по оси x.</p>
     *
     * @param positionX координата по оси x.
     */
    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    /**
     * <p>Геттер координаты по оси y.</p>
     *
     * @return координата по оси y.
     */
    public double getPositionY() {
        return positionY;
    }

    /**
     * <p>Сеттер координаты по оси y.</p>
     *
     * @param positionY координата по оси y.
     */
    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    /**
     * <p>Геттер направления.</p>
     *
     * @return направление.
     */
    public double getDirection() {
        return direction;
    }

    /**
     * <p>Сеттер направления.</p>
     *
     * @param direction направление.
     */
    public void setDirection(double direction) {
        this.direction = direction;
    }

    /**
     * <p>Геттер скорости.</p>
     *
     * @return скорость.
     */
    public double getVelocity() {
        return MAX_VELOCITY;
    }

    /**
     * <p>Геттер угловой скорости.</p>
     *
     * @return угловая скорость.
     */
    public double getAngularVelocity() {
        return MAX_ANGULAR_VELOCITY;
    }
}
