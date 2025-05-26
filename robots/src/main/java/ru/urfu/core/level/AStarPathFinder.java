package ru.urfu.core.level;

import com.mursaat.pathfinding.AStar;
import com.mursaat.pathfinding.AStarParams;
import com.mursaat.pathfinding.DistanceCalculator;
import com.mursaat.pathfinding.NeighborsEnumerator;
import com.mursaat.pathfinding.PathFinderMap;
import com.mursaat.pathfinding.PathNodePosition;
import java.util.List;
import ru.urfu.utils.Vector2;

/**
 * <p>Поиск пути с помощью алгоритма A*.</p>
 */
public final class AStarPathFinder {
    private final PathFinderMap map;

    /**
     * <p>Конструктор.</p>
     *
     * @param level поля, на котором будут искаться пути.
     */
    public AStarPathFinder(Level level) {
        this.map = new LevelPathFinerMap(level);
    }

    /**
     * <p>Ищет путь.</p>
     *
     * @param from клетка откуда.
     * @param to   клетка куда.
     * @return список клеток, которые надо пройти.
     */
    public List<Vector2> findPath(Vector2 from, Vector2 to) {
        final PathNodePosition startPos = new PathNodePosition((int) from.x(), (int) from.y());
        final PathNodePosition endPos = new PathNodePosition((int) to.x(), (int) to.y());

        final AStarParams params = new AStarParams(map, startPos, endPos);
        params.setNeighborsEnumerator(NeighborsEnumerator.ORTHO_NEIGHBORS);
        params.setHeuristic(DistanceCalculator.MANHATTAN_DISTANCE);

        return AStar.findPath(params).stream().map(this::fromPathNode).toList();
    }

    /**
     * <p>Конвертирует внутренний объект {@link PathNodePosition} в {@link Vector2}.</p>
     *
     * @param node узел
     * @return координаты клетки в виде вектора.
     */
    private Vector2 fromPathNode(PathNodePosition node) {
        return new Vector2(node.x, node.y);
    }
}