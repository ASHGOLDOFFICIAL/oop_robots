package ru.urfu.core.level;

/**
 * <p>Поле: набор препятствий.</p>
 */
public final class Level {
    private final int width;
    private final int height;
    private final boolean[] obstacles;

    /**
     * <p>Конструктор.</p>
     *
     * @param width  ширина поля.
     * @param height высота поля.
     */
    public Level(int width, int height) {
        this.width = width;
        this.height = height;
        this.obstacles = new boolean[width * height];
    }

    /**
     * <p>Добавляет препятствие по данным координатам.</p>
     * <p>Индексация с нуля до ширины/высоты не включительно.</p>
     *
     * @param x x
     * @param y y
     */
    public void addObstacle(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException();
        this.obstacles[y * height + x] = true;
    }

    /**
     * <p>Проверка, есть ли препятствие по данным координатам.</p>
     * <p>Индексация с нуля до ширины/высоты не включительно.</p>
     *
     * @param x x
     * @param y y
     */
    public boolean hasObstacle(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException();
        return obstacles[y * height + x];
    }

    /**
     * <p>Ширина поля.</p>
     *
     * @return ширину поля.
     */
    public int getWidth() {
        return width;
    }

    /**
     * <p>Высота поля.</p>
     *
     * @return высоту поля.
     */
    public int getHeight() {
        return height;
    }
}
