package nl.tudelft.jpacman.game;

import java.util.List;
import java.util.function.Supplier;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;

import com.google.common.collect.ImmutableList;

/**
 * A game with one player and a single level.
 *
 * @author Jeroen Roosen 
 */
public class SinglePlayerGame extends Game {

    /**
     * The player of this game.
     */
    private final Player player;

    /**
     * The level of this game.
     */
    private Level level;

    private final Supplier<Level> levelSupplier;

    /**
     * Create a new single player game for the provided level and player.
     *
     * @param player
     *            The player.
     * @param level
     *            The level.
     */
    protected SinglePlayerGame(Player player, Level level) {
        this(player, level, null);
    }

    /**
     * Creates a game that can reload its level after the player dies.
     *
     * @param player The player.
     * @param level The initial level.
     * @param levelSupplier Factory for fresh levels, or null to disable restart.
     */
    protected SinglePlayerGame(Player player, Level level, Supplier<Level> levelSupplier) {
        assert player != null;
        assert level != null;

        this.player = player;
        this.level = level;
        this.levelSupplier = levelSupplier;
        this.level.registerPlayer(player);
    }

    @Override
    public synchronized void start() {
        if (!player.isAlive() && levelSupplier != null) {
            Level freshLevel = levelSupplier.get();
            stop();
            level.stop();
            level.removeObserver(this);
            level = freshLevel;
            player.setAlive(true);
            player.setDirection(Direction.EAST);
            level.registerPlayer(player);
            player.addPoints(-player.getScore());
        }
        super.start();
    }

    @Override
    public List<Player> getPlayers() {
        return ImmutableList.of(player);
    }

    @Override
    public Level getLevel() {
        return level;
    }
}
