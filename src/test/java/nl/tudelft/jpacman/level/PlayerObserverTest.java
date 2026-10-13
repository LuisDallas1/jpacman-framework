package nl.tudelft.jpacman.level;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.HashMap;

import nl.tudelft.jpacman.sprite.AnimatedSprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link PlayerObserver} notifications emitted by a {@link Player},
 * covering both the score-change and death events.
 */
@SuppressWarnings("PMD.TooManyStaticImports")
class PlayerObserverTest {

    /** The player acting as the subject under test. */
    private Player player;

    /** A mock observer registered on the player. */
    private PlayerObserver observer;

    /**
     * Creates a player and registers a mock observer on it.
     */
    @BeforeEach
    void setUp() {
        player = new Player(new HashMap<>(), mock(AnimatedSprite.class));
        observer = mock(PlayerObserver.class);
        player.addObserver(observer);
    }

    /**
     * Adding points notifies observers with the new total score.
     */
    @Test
    void addPointsNotifiesScoreChanged() {
        player.addPoints(10);
        verify(observer).scoreChanged(10);
        assertThat(player.getScore()).isEqualTo(10);
    }

    /**
     * Killing the player notifies observers via {@code playerDied}.
     */
    @Test
    void setAliveFalseNotifiesPlayerDied() {
        player.setAlive(false);
        verify(observer).playerDied();
        assertThat(player.isAlive()).isFalse();
    }

    /**
     * When notified of a death, an observer must already see the player as dead,
     * i.e. the subject updates its state before notifying.
     */
    @Test
    void observerObservesDeadStateWhenNotified() {
        boolean[] sawDead = { false };
        player.addObserver(new PlayerObserver() {
            @Override
            public void scoreChanged(int newScore) {
                // Not relevant for this test.
            }

            @Override
            public void playerDied() {
                sawDead[0] = !player.isAlive();
            }
        });
        player.setAlive(false);
        assertThat(sawDead[0]).isTrue();
    }

    /**
     * A removed observer is no longer notified.
     */
    @Test
    void removeObserverStopsNotifications() {
        player.removeObserver(observer);
        player.addPoints(5);
        verifyNoInteractions(observer);
    }
}
