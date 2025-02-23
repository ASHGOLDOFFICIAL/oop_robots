package ru.urfu.core;

public final class RobotModel {
    private static final double MAX_VELOCITY = 0.1;
    private static final double MAX_ANGULAR_VELOCITY = 0.001;

    private volatile double positionX;
    private volatile double positionY;
    private volatile double direction;
    private volatile double velocity = 0;
    private volatile double angularVelocity = 0;

    public RobotModel(double initialX, double initialY, double direction) {
        this.positionX = initialX;
        this.positionY = initialY;
        this.direction = direction;
    }

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double setVelocity(double velocity) {
        final double newVelocity = applyLimits(velocity, 0, MAX_VELOCITY);
        this.velocity = newVelocity;
        return newVelocity;
    }

    public double setAngularVelocity(double angularVelocity) {
        final double newAngularVelocity = applyLimits(angularVelocity, -MAX_ANGULAR_VELOCITY, MAX_ANGULAR_VELOCITY);
        this.angularVelocity = newAngularVelocity;
        return newAngularVelocity;
    }

    public double getMaxVelocity() {
        return MAX_VELOCITY;
    }

    public double getMaxAngularVelocity() {
        return MAX_ANGULAR_VELOCITY;
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }
}
