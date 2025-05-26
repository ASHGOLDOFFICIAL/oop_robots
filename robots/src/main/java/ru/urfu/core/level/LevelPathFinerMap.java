package ru.urfu.core.level;

import com.mursaat.pathfinding.PathFinderMap;

/**
 * <p>Реализация {@link PathFinderMap} для {@link Level}.</p>
 */
public final class LevelPathFinerMap implements PathFinderMap {
    private final Level level;

    /**
     * <p>Конструктор.</p>
     *
     * @param level уровень для поиска путей.
     */
    public LevelPathFinerMap(Level level) {
        this.level = level;
    }

    @Override
    public boolean isTraversable(int x, int y) {
        if (x < 0 || x > level.getWidth() || y < 0 || y > level.getHeight()) {
            return false;
        }
        return !level.hasObstacle(x, y);
    }

    @Override
    public int getWidth() {
        return level.getWidth();
    }

    @Override
    public int getHeight() {
        return level.getHeight();
    }
}
