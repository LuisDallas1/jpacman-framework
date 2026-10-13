package nl.tudelft.jpacman.game;

import java.util.function.Supplier;

import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.PlayerFactory;

/**
 * Factory that provides Game objects.
 *
 * @author Jeroen Roosen 
 */
public class GameFactory {

    /**
     * The factory providing the player objects.
     */
    private final PlayerFactory playerFactory;

    /**
     * Creates a new game factory.
     *
     * @param playerFactory
     *            The factory providing the player objects.
     */
    public GameFactory(PlayerFactory playerFactory) {
        this.playerFactory = playerFactory;
    }

    /**
     * Creates a game for a single level with one player.
     *
     * @param level
     *            The level to create a game for.
     * @return A new single player game.
     */
    public Game createSinglePlayerGame(Level level) {
        return new SinglePlayerGame(playerFactory.createPacMan(), level);
    }

    /**
     * Creates a single-player game with support for restarting after death.
     *
     * @param level The initial level.
     * @param levelSupplier Factory that reloads the level.
     * @return A restartable game.
     */
    public Game createSinglePlayerGame(Level level, Supplier<Level> levelSupplier) {
        return new SinglePlayerGame(playerFactory.createPacMan(), level, levelSupplier);
    }

    /**
     * Returns the player factory associated with this game factory.
     *
     * @return The player factory associated with this game factory.
     */
    protected PlayerFactory getPlayerFactory() {
        return playerFactory;
    }
}
