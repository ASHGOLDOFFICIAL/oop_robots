package ru.urfu.core;

/**
 * <p>View для игрового поля.</p>
 */
public interface GameView {
    /**
     * <p>Через этот метод оповещаем view
     * о наличии обновлений в модели.</p>
     */
    void onModelUpdate();
}
