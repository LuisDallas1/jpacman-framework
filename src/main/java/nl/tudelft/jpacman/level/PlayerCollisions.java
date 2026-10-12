package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.board.UnitVisitor;
import nl.tudelft.jpacman.npc.Ghost;

/**
 * A simple implementation of a collision map for the JPacman player.
 * <p>
 * Uses two visitor dispatches to select a rule for both units without type
 * checks, casts or reflection. Noninteractive pairs explicitly have no effect.
 *
 * @author Arie van Deursen, 2014
 *
 */

public class PlayerCollisions implements CollisionMap {

    /** Dispatches the moving unit to an explicit collision rule. */
    private final class MoverVisitor implements UnitVisitor {
        private final Unit target;

        MoverVisitor(Unit target) {
            this.target = target;
        }

        @Override
        public void visit(Player unit) {
            target.accept(new PlayerTargetVisitor(unit));
        }

        @Override
        public void visit(Ghost unit) {
            target.accept(new GhostTargetVisitor(unit));
        }

        @Override
        public void visit(Pellet unit) {
            target.accept(new PelletTargetVisitor(unit));
        }

        @Override
        public void visit(Unit unit) {
            // This pair has no gameplay effect.
        }
    }

    /** Dispatches the target unit to an explicit collision rule. */
    private final class PlayerTargetVisitor implements UnitVisitor {
        private final Player player;

        PlayerTargetVisitor(Player player) {
            this.player = player;
        }

        @Override
        public void visit(Player unit) {
            // This pair has no gameplay effect.
        }

        @Override
        public void visit(Ghost unit) {
            playerVersusGhost(player, unit);
        }

        @Override
        public void visit(Pellet unit) {
            playerVersusPellet(player, unit);
        }

        @Override
        public void visit(Unit unit) {
            // This pair has no gameplay effect.
        }
    }

    /** Dispatches the target unit to an explicit collision rule. */
    private final class GhostTargetVisitor implements UnitVisitor {
        private final Ghost ghost;

        GhostTargetVisitor(Ghost ghost) {
            this.ghost = ghost;
        }

        @Override
        public void visit(Player unit) {
            playerVersusGhost(unit, ghost);
        }

        @Override
        public void visit(Ghost unit) {
            // This pair has no gameplay effect.
        }

        @Override
        public void visit(Pellet unit) {
            // This pair has no gameplay effect.
        }

        @Override
        public void visit(Unit unit) {
            // This pair has no gameplay effect.
        }
    }

    /** Dispatches the target unit to an explicit collision rule. */
    private final class PelletTargetVisitor implements UnitVisitor {
        private final Pellet pellet;

        PelletTargetVisitor(Pellet pellet) {
            this.pellet = pellet;
        }

        @Override
        public void visit(Player unit) {
            playerVersusPellet(unit, pellet);
        }

        @Override
        public void visit(Ghost unit) {
            // This pair has no gameplay effect.
        }

        @Override
        public void visit(Pellet unit) {
            // This pair has no gameplay effect.
        }

        @Override
        public void visit(Unit unit) {
            // This pair has no gameplay effect.
        }
    }


    @Override
    public void collide(Unit mover, Unit collidedOn) {
        mover.accept(new MoverVisitor(collidedOn));
    }



    /**
     * Actual case of player bumping into ghost or vice versa.
     *
     * @param player The player involved in the collision.
     * @param ghost The ghost involved in the collision.
     */
    public void playerVersusGhost(Player player, Ghost ghost) {
        player.setAlive(false);
    }

    /**
     * Actual case of player consuming a pellet.
     *
     * @param player The player involved in the collision.
     * @param pellet The pellet involved in the collision.
     */
    public void playerVersusPellet(Player player, Pellet pellet) {
        pellet.leaveSquare();
        player.addPoints(pellet.getValue());
    }

}
