package ru.urfu.core;

import java.awt.Point;

/**
 * <p>Модель игры.</p>
 */
public interface GameModel {
    /**
     * <p>Запускает игровой цикл: с некоторой периодичностью
     * модель обновляет своё состояние.</p>
     */
    void start();

    /**
     * <p>Останавливает игровой цикл: модель
     * больше не обновляет своё состояние.</p>
     */
    void stop();

    /**
     * <p>Подписывает view на получение
     * уведомлений об обновлении модели.</p>
     *
     * @param view который хотим подписать на обновления
     */
    void subscribeToUpdates(GameView view);

    /**
     * <p>Геттер информации о роботе.</p>
     *
     * @return информация о роботе.
     */
    RobotInfo getRobot();

    /**
     * <p>Геттер положения цели.</p>
     *
     * @return положение цели.
     */
    Point getTargetPosition();

    /**
     * <p>Сеттер положения цели.</p>
     *
     * @param p новое положение цели.
     */
    void setTargetPosition(Point p);
}
