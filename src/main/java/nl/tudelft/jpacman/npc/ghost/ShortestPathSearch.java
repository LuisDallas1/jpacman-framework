package nl.tudelft.jpacman.npc.ghost;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;

/**
 * BFS specialization to find the shortest path (in number of moves) from a
 * start square to a target square.
 *
 * <p>The path is returned as a list of directions from start to target.
 * Since BFS guarantees shortest path in the grid for unit move costs,
 * this yields the shortest route in moves.</p>
 */
class ShortestPathSearch extends BreadthFirstSearch<List<Direction>> {

    private final Square target;
    private final Unit traveller;

    /**
     * Creates a new shortest path search.
     *
     * @param target    The target square to reach.
     * @param traveller The unit attempting to traverse the path, or {@code null}
     *                  if accessibility should not be considered.
     */
    ShortestPathSearch(Square target, Unit traveller) {
        this.target = target;
        this.traveller = traveller;
    }

    @Override
    protected boolean isGoal(Square current) {
        return current.equals(target);
    }

    @Override
    protected boolean canTraverse(Square current, Direction direction, Square neighbor) {
        if (traveller != null) {
            return neighbor.isAccessibleTo(traveller);
        }
        return true;
    }

    @Override
    protected List<Direction> extractResult(Square goal,
                                          Map<Square, Square> parentMap,
                                          Map<Square, Direction> directionToReach) {
        LinkedList<Direction> directions = new LinkedList<>();
        Square current = goal;
        while (parentMap.get(current) != null) {
            directions.addFirst(directionToReach.get(current));
            current = parentMap.get(current);
        }
        return directions;
    }
}
