package nl.tudelft.jpacman.board;

import nl.tudelft.jpacman.sprite.Sprite;

/** Minimal unit used to test square occupancy. */
class BasicUnit extends Unit {
    @Override
    public void accept(UnitVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Sprite getSprite() {
        return null;
    }
}
