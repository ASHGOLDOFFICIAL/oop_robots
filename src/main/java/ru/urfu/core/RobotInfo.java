package ru.urfu.core;

/**
 * <p>Информация о роботе. Класс нужен для того,
 * чтобы скрыть {@link RobotModel} от всех классов,
 * кроме {@link GameModel}</p>
 *
 * @param positionX позиция робота по оси x.
 * @param positionY позиция робота по оси y.
 * @param direction направление робота.
 */
public record RobotInfo(
        double positionX,
        double positionY,
        double direction) {

    /**
     * <p>Конструктор на основе модели.</p>
     * @param model модель.
     */
    public RobotInfo(RobotModel model) {
        this(model.getPositionX(), model.getPositionY(), model.getDirection());
    }
}
