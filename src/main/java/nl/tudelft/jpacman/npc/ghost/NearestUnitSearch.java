package nl.tudelft.jpacman.npc.ghost;

import java.util.Map;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;

/**
 * BFS specialization to find the nearest unit of a given type from a start square.
 *
 * <p>BFS explores in order of increasing distance, so the first unit found
 * is the closest one in the grid (shortest path in moves).</p>
 */
class NearestUnitSearch extends BreadthFirstSearch<Unit> {

    private final Class<? extends Unit> type;

    /**
     * Creates a new nearest unit search.
     *
     * @param type The class type of units to search for.
     */
    NearestUnitSearch(Class<? extends Unit> type) {
        this.type = type;
    }

    @Override
    protected boolean isGoal(Square current) {
        if (current == null || type == null) {
            return false;
        }
        for (Unit unit : current.getOccupants()) {
            if (type.isInstance(unit)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean canTraverse(Square current, Direction direction, Square neighbor) {
        // Preserve original behavior: findNearest does not consider accessibility.
        return true;
    }

    @Override
    protected Unit extractResult(Square goal,
                                Map<Square, Square> parentMap,
                                Map<Square, Direction> directionToReach) {
        if (goal == null || type == null) {
            return null;
        }
        for (Unit unit : goal.getOccupants()) {
            if (type.isInstance(unit)) {
                return unit;
            }
        }
        return null;
    }
}