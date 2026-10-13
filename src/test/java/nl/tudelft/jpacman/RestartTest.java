package nl.tudelft.jpacman;

import java.util.Arrays;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies restart and pause behavior without UI or moving ghosts.
 */
class RestartTest {

    private Game makeGame() {
        return new Launcher() {
            @Override
            public Level makeLevel() {
                return getMapParser().parseMap(Arrays.asList("#####", "#P..#", "#####"));
            }
        }.makeGame();
    }

    @Test
    void startAfterDeathRestoresInitialState() {
        Game game = makeGame();
        Player player = game.getPlayers().get(0);
        Level original = game.getLevel();
        try {
            game.start();
            game.move(player, Direction.EAST);
            assertThat(player.getScore()).isPositive();
            assertThat(original.remainingPellets()).isEqualTo(1);
            player.setAlive(false);
            game.levelLost();

            game.start();

            assertThat(game.isInProgress()).isTrue();
            assertThat(game.getLevel()).isNotSameAs(original);
            assertThat(original.isInProgress()).isFalse();
            assertThat(player.isAlive()).isTrue();
            assertThat(player.getScore()).isZero();
            assertThat(game.getLevel().remainingPellets()).isEqualTo(2);
            assertThat(player.getSquare()).isSameAs(game.getLevel().getBoard().squareAt(1, 1));
            game.move(player, Direction.EAST);
            assertThat(player.getScore()).isPositive();
        } finally {
            game.stop();
        }
    }

    @Test
    void startAfterPausePreservesProgress() {
        Game game = makeGame();
        Player player = game.getPlayers().get(0);
        Level original = game.getLevel();
        try {
            game.start();
            game.move(player, Direction.EAST);
            int score = player.getScore();
            game.stop();
            game.start();
            assertThat(game.getLevel()).isSameAs(original);
            assertThat(player.getScore()).isEqualTo(score);
            assertThat(original.remainingPellets()).isEqualTo(1);
        } finally {
            game.stop();
        }
    }
}
