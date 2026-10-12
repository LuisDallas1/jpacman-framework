package nl.tudelft.jpacman.npc.ghost;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;

/**
 * Abstract base class implementing the Breadth-First Search (BFS) algorithm
 * over the Pac-Man grid.
 *
 * <p>This class defines the invariant skeleton of BFS: initialization of the
 * queue and visited set, node expansion in order of increasing distance,
 * and goal testing. The variable parts are isolated as template hooks:</p>
 * <ul>
 *   <li>{@link #isGoal(Square)}: defines the stopping condition.</li>
 *   <li>{@link #canTraverse(Square, Direction, Square)}: defines whether a
 *       neighbor can be traversed from the current square.</li>
 *   <li>{@link #extractResult(Square, Map, Map)}: constructs the result to return
 *       when a goal is reached.</li>
 * </ul>
 *
 * @param <T> The type of result returned by this search.
 */
abstract class BreadthFirstSearch<T> {

    /**
     * Executes BFS starting from the given square.
     *
     * @param start The square to start the search from.
     * @return The result extracted when the goal is reached, or {@code null}
     *         if no goal is found.
     */
    public final T execute(Square start) {
        if (start == null) {
            return null;
        }

        Deque<Square> queue = new ArrayDeque<>();
        Set<Square> visited = new HashSet<>();
        Map<Square, Square> parentMap = new HashMap<>();
        Map<Square, Direction> directionToReach = new HashMap<>();

        // Initialize BFS
        queue.addLast(start);
        visited.add(start);
        parentMap.put(start, null);
        directionToReach.put(start, null);

        while (!queue.isEmpty()) {
            Square current = queue.pollFirst();

            if (isGoal(current)) {
                return extractResult(current, parentMap, directionToReach);
            }

            for (Direction direction : Direction.values()) {
                Square neighbor = current.getSquareAt(direction);
                if (neighbor == null) {
                    continue;
                }

                if (visited.contains(neighbor)) {
                    continue;
                }

                if (!canTraverse(current, direction, neighbor)) {
                    continue;
                }

                visited.add(neighbor);
                parentMap.put(neighbor, current);
                directionToReach.put(neighbor, direction);
                queue.addLast(neighbor);
            }
        }

        return null;
    }

    /**
     * Checks whether the current square satisfies the goal condition.
     *
     * @param current The square being examined.
     * @return {@code true} if the goal is reached, {@code false} otherwise.
     */
    protected abstract boolean isGoal(Square current);

    /**
     * Determines whether the neighbor square can be traversed from the current
     * position.
     *
     * @param current   The current square.
     * @param direction The direction to the neighbor.
     * @param neighbor  The neighbor square.
     * @return {@code true} if traversal is allowed, {@code false} otherwise.
     */
    protected abstract boolean canTraverse(Square current, Direction direction, Square neighbor);

    /**
     * Extracts the search result when the goal is reached.
     *
     * @param goal             The goal square.
     * @param parentMap        A map mapping each visited square to its parent square
     *                         in the BFS tree ({@code null} for the start square).
     * @param directionToReach A map mapping each visited square to the direction
     *                         that led to it from its parent.
     * @return The result of type {@code T}.
     */
    protected abstract T extractResult(Square goal,
                                      Map<Square, Square> parentMap,
                                      Map<Square, Direction> directionToReach);
}
