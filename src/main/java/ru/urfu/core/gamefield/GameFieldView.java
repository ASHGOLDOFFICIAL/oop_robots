package ru.urfu.core.gamefield;

/**
 * <p>View для игрового поля.</p>
 */
public interface GameFieldView {
    /**
     * <p>Через этот метод оповещаем view
     * о наличии обновлений в модели.</p>
     */
    void onModelUpdate();
}
