// CHECKSTYLE:OFF: PackageDeclaration

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import ru.urfu.core.RobotMovement;
import ru.urfu.gui.game.RobotShape;
import ru.urfu.utils.Vector2;

/**
 * <p>Пример мода для игры.</p>
 *
 * <p>Движется напрямую к цели, отображается как зелёный квадрат.</p>
 */
public class Mod implements ru.urfu.modding.Mod {
    private static final int DIMENSION = 30;

    @Override
    public RobotShape shape() {
        return g -> {
            final Shape shape = new Rectangle(DIMENSION, DIMENSION);
            g.setColor(Color.GREEN);
            g.fill(shape);
            g.setColor(Color.BLACK);
            g.draw(shape);
        };
    }

    @Override
    public RobotMovement logic() {
        return (model, time) -> {
            final Vector2 position = model.getRobotInfo().position();
            final Vector2 target = model.getTargetPosition();

            if (position.distanceSquared(target) < 0.5)
                return new Vector2(0, 0);
            return target.minus(position).normalize();
        };
    }
}
