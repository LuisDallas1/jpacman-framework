package nl.tudelft.jpacman.npc.ghost;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;

/**
 * Navigation provides utility to navigate on {@link Square}s.
 *
 * @author Jeroen Roosen 
 */
public final class Navigation {

    private Navigation() {
    }

    /**
     * Calculates the shortest path. This is done by BFS. This search ensures
     * the traveller is allowed to occupy the squares on the way, or returns the
     * shortest path to the square regardless of terrain if no traveller is
     * specified.
     *
     * @param from
     *            The starting square.
     * @param to
     *            The destination.
     * @param traveller
     *            The traveller attempting to reach the destination. If
     *            traveller is set to <code>null</code>, this method will ignore
     *            terrain and find the shortest path whether it can actually be
     *            reached or not.
     * @return The shortest path to the destination or <code>null</code> if no
     *         such path could be found. When the destination is the current
     *         square, an empty list is returned.
     */
    public static List<Direction> shortestPath(Square from, Square to,
                                                          Unit traveller) {
        if (from == null || to == null) {
            return null;
        }
        if (from.equals(to)) {
            return new ArrayList<>();
        }
        return new ShortestPathSearch(to, traveller).execute(from);
    }

    /**
     * Finds the nearest unit of the given type and returns its location. This
     * method will perform a breadth first search starting from the given
     * square.
     *
     * @param type
     *            The type of unit to search for.
     * @param currentLocation
     *            The starting location for the search.
     * @return The nearest unit of the given type, or <code>null</code> if no
     *         such unit could be found.
     */
    public static Unit findNearest(Class<? extends Unit> type,
                                              Square currentLocation) {
        if (type == null || currentLocation == null) {
            return null;
        }
        return new NearestUnitSearch(type).execute(currentLocation);
    }

    /**
     *  Finds a subtype of Unit in a level.
     *  This method is very useful for finding the ghosts in the parsed map.
     *
     * @param clazz the type to search for.
     * @param board the board to find the unit in.
     * @param <T> the return type, same as the type in clazz.
     *
     * @return the first unit found of type clazz, or null.
     */
    public static <T extends Unit> T findUnitInBoard(Class<T> clazz, Board board) {
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                final T ghost = Navigation.findUnit(clazz, board.squareAt(x, y));
                if (ghost != null) {
                    return ghost;
                }
            }
        }

        return null;
    }

    /**
     * Determines whether a square has an occupant of a certain type.
     *
     * @param type
     *            The type to search for.
     * @param square
     *            The square to search.
     * @param <T>
     *           the type of unit we searched for.
     *
     * @return A unit of type T, iff such a unit occupies this square, or
     *         <code>null</code> of none does.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Unit> T findUnit(Class<T> type, Square square) {
        for (Unit unit : square.getOccupants()) {
            if (type.isInstance(unit)) {
                assert unit.hasSquare();
                return (T) unit;
            }
        }
        return null;
    }


}
