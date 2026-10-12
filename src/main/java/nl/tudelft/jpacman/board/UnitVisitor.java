package nl.tudelft.jpacman.board;

import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.npc.Ghost;

/**
 * Operations on the supported unit types. New visit methods must be implemented
 * by every visitor; units with a new category must override accept accordingly.
 */
public interface UnitVisitor {
    /** @param player The player being visited. */
    void visit(Player player);

    /** @param ghost The ghost being visited. */
    void visit(Ghost ghost);

    /** @param pellet The pellet being visited. */
    void visit(Pellet pellet);

    /** @param unit A unit outside the interactive categories. */
    void visit(Unit unit);
}
