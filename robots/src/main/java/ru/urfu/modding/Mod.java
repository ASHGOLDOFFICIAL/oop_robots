package ru.urfu.modding;

import ru.urfu.core.movement.RobotMovement;
import ru.urfu.gui.game.RobotShape;

/**
 * <p>Модификация для игры.</p>
 *
 * <p>Смотрите {@link ModLoader} для требований,
 * предъявляемых реализация интерфейса для того,
 * чтобы быть их можно было загрузить.</p>
 */
public interface Mod {
    /**
     * <p>Форма робота для отображения.</p>
     *
     * <p>Если модификация не предполагает
     * изменения отображения, следует вернуть <code>null</code>.</p>
     *
     * @return класс, рисующий робота.
     */
    RobotShape shape();

    /**
     * <p>Логика передвижения робота.</p>
     *
     * <p>Если модификация не предполагает изменения
     * логики передвижения, следует вернуть <code>null</code>.</p>
     *
     * @return класс, определяющий движение.
     */
    RobotMovement logic();
}
