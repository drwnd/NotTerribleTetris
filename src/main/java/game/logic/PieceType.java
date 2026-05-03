package game.logic;

import org.joml.Vector2f;

public enum PieceType {

    I(new Vector2f(1.5F, 0.5F), new Block(3, 1), new Block(0, 1), new Block(1, 1), new Block(2, 1)),
    O(new Vector2f(0.5F, 0.5F), new Block(2, 2), new Block(2, 1), new Block(1, 2), new Block(1, 1)),
    T(new Vector2f(1, 1), new Block(1, 1), new Block(0, 2), new Block(1, 2), new Block(2, 2)),
    Z(new Vector2f(1, 1), new Block(0, 1), new Block(1, 1), new Block(1, 2), new Block(2, 2)),
    S(new Vector2f(1, 1), new Block(1, 1), new Block(2, 1), new Block(0, 2), new Block(1, 2)),
    L(new Vector2f(1, 1), new Block(2, 1), new Block(0, 2), new Block(1, 2), new Block(2, 2)),
    J(new Vector2f(1, 1), new Block(0, 1), new Block(0, 2), new Block(1, 2), new Block(2, 2));

    private final Block block1, block2, block3, block4;
    private final Vector2f rotationCenter;

    PieceType(Vector2f rotationCenter, Block block1, Block block2, Block block3, Block block4) {
        this.block1 = block1;
        this.block2 = block2;
        this.block3 = block3;
        this.block4 = block4;
        this.rotationCenter = rotationCenter;
    }

    public Piece newInstance() {
        return new Piece(this, new Vector2f(rotationCenter), new Block(block1), new Block(block2), new Block(block3), new Block(block4));
    }
}
