package ru.urfu.core;

import java.awt.Point;
import java.beans.PropertyChangeListener;

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
     * <p>Геттер информации о положении робота.</p>
     *
     * @return информация о положении робота.
     */
    RobotPosition getRobotPosition();

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

    /**
     * <p>Регистрирует слушателя изменения модели.</p>
     *
     * @param listener слушатель
     */
    void registerListener(PropertyChangeListener listener);

    /**
     * <p>Убирает слушателя изменения модели.</p>
     *
     * @param listener слушатель
     */
    void removeListener(PropertyChangeListener listener);
}
