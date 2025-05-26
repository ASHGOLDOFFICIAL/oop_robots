package ru.urfu.core.level;

import java.util.Random;
import personthecat.fastnoise.FastNoise;
import personthecat.fastnoise.generator.SimplexNoise;

/**
 * <p>Генератор случайных полей.</p>
 */
public final class LevelGenerator {
    private final static double OBSTACLE_THRESHOLD = 0.2;
    private final Random random = new Random();

    /**
     * <p>Создаёт уровень со случайными препятствиями.</p>
     *
     * @param width  ширина поля.
     * @param height высота поля.
     * @return новое поле.
     */
    public Level generate(int width, int height) {
        final FastNoise noise = new SimplexNoise(random.nextInt());
        final Level level = new Level(width, height);

        for (int x = 0; x < width; ++x) {
            level.addObstacle(x, 0);
            level.addObstacle(x, height - 1);
        }

        for (int y = 1; y < height - 1; ++y) {
            level.addObstacle(0, y);
            for (int x = 1; x < width - 1; ++x) {
                final float value = noise.getNoise(x, y);
                if (value > OBSTACLE_THRESHOLD) {
                    level.addObstacle(x, y);
                }
            }
            level.addObstacle(width - 1, y);
        }

        return level;
    }
}
