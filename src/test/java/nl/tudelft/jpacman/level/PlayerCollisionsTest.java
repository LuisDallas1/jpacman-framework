package nl.tudelft.jpacman.level;

import java.util.Arrays;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.board.UnitVisitor;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/** Tests both dispatch stages with real units, including a Ghost subclass. */
class PlayerCollisionsTest {
    private final PacManSprites sprites = new PacManSprites();
    private final Player player = new PlayerFactory(sprites).createPacMan();
    private final Ghost ghost = new GhostFactory(sprites).createBlinky();
    private final PlayerCollisions collisions = new PlayerCollisions();

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void ghostKillsPlayerInEitherOrder(boolean reversed) {
        collisions.collide(reversed ? ghost : player, reversed ? player : ghost);
        assertThat(player.isAlive()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void pelletIsConsumedInEitherOrder(boolean reversed) {
        Level level = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites)),
            new BoardFactory(sprites)).parseMap(Arrays.asList("P."));
        Pellet pellet = (Pellet) level.getBoard().squareAt(1, 0).getOccupants().get(0);
        collisions.collide(reversed ? pellet : player, reversed ? player : pellet);
        assertThat(pellet.hasSquare()).isFalse();
        assertThat(level.getBoard().squareAt(1, 0).getOccupants()).isEmpty();
        assertThat(player.getScore()).isEqualTo(pellet.getValue());
        assertThat(player.isAlive()).isTrue();
    }

    @Test
    void noninteractivePairsHaveNoEffect() {
        Pellet pellet = new Pellet(1, null);
        Unit other = new Unit() {
            @Override
            public void accept(UnitVisitor visitor) {
                visitor.visit(this);
            }

            @Override
            public Sprite getSprite() {
                return null;
            }
        };
        Unit[] units = {player, ghost, pellet, other};
        for (Unit unit : units) {
            collisions.collide(unit, unit);
            collisions.collide(unit, other);
            collisions.collide(other, unit);
        }
        collisions.collide(ghost, pellet);
        collisions.collide(pellet, ghost);
        assertThat(player.isAlive()).isTrue();
        assertThat(player.getScore()).isZero();
    }
}
