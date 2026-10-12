package nl.tudelft.jpacman.level;

import java.util.List;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.npc.ghost.RandomTargetStrategy;
import nl.tudelft.jpacman.sprite.PacManSprites;

/**
 * Factory that creates levels and units.
 *
 * @author Jeroen Roosen 
 */
public class LevelFactory {

    private static final int GHOSTS = 4;
    private static final int BLINKY = 0;
    private static final int INKY = 1;
    private static final int PINKY = 2;
    private static final int CLYDE = 3;

    /**
     * The default value of a pellet.
     */
    private static final int PELLET_VALUE = 10;

    /**
     * The sprite store that provides sprites for units.
     */
    private final PacManSprites sprites;

    /**
     * Used to cycle through the various ghost types.
     */
    private int ghostIndex;

    /**
     * The factory providing ghosts.
     */
    private final GhostFactory ghostFact;

    /**
     * Creates a new level factory.
     *
     * @param spriteStore
     *            The sprite store providing the sprites for units.
     * @param ghostFactory
     *            The factory providing ghosts.
     */
    public LevelFactory(PacManSprites spriteStore, GhostFactory ghostFactory) {
        this.sprites = spriteStore;
        this.ghostIndex = -1;
        this.ghostFact = ghostFactory;
    }

    /**
     * Creates a new level from the provided data.
     *
     * @param board
     *            The board with all ghosts and pellets occupying their squares.
     * @param ghosts
     *            A list of all ghosts on the board.
     * @param startPositions
     *            A list of squares from which players may start the game.
     * @return A new level for the board.
     */
    public Level createLevel(Board board, List<Ghost> ghosts,
                             List<Square> startPositions) {

        // We'll adopt the simple collision map for now.
        CollisionMap collisionMap = new PlayerCollisions();

        return new Level(board, ghosts, startPositions, collisionMap);
    }

    /**
     * Creates a new ghost, cycling through the four classic ghost types in
     * order (Blinky → Inky → Pinky → Clyde). If the switch falls through to
     * the default branch (which cannot happen in normal gameplay given
     * {@code GHOSTS == 4}), a wandering ghost backed by
     * {@link RandomTargetStrategy} is returned instead of an anonymous subclass.
     *
     * <p>
     * <strong>Strategy pattern:</strong> the previous {@code default} branch
     * instantiated a private {@code RandomGhost} inner class whose sole
     * purpose was to return {@link java.util.Optional#empty()} from
     * {@code nextAiMove()}. That subclass has been removed; the same effect is
     * now achieved by configuring a standard {@link nl.tudelft.jpacman.npc.ghost.Blinky}
     * (or any Ghost subclass) with a {@link RandomTargetStrategy}.
     * </p>
     *
     * @return The new ghost.
     */
    Ghost createGhost() {
        ghostIndex++;
        ghostIndex %= GHOSTS;
        switch (ghostIndex) {
            case BLINKY:
                return ghostFact.createBlinky();
            case INKY:
                return ghostFact.createInky();
            case PINKY:
                return ghostFact.createPinky();
            case CLYDE:
                return ghostFact.createClyde();
            default:
                // Fallback: a Blinky-shaped ghost configured to wander randomly.
                // RandomTargetStrategy replaces the former RandomGhost inner class.
                Ghost wanderer = ghostFact.createBlinky();
                wanderer.setTargetStrategy(new RandomTargetStrategy());
                return wanderer;
        }
    }

    /**
     * Creates a new pellet.
     *
     * @return The new pellet.
     */
    public Pellet createPellet() {
        return new Pellet(PELLET_VALUE, sprites.getPelletSprite());
    }
}
