package game.logic;

import org.joml.Vector2f;

import java.awt.*;

public final class Piece {

    public final Block block1, block2, block3, block4;
    private final PieceType pieceType;
    private final Vector2f rotationCenter;

    public Piece(PieceType pieceType, Vector2f rotationCenter, Block block1, Block block2, Block block3, Block block4) {
        this.pieceType = pieceType;
        this.rotationCenter = rotationCenter;
        this.block1 = block1;
        this.block2 = block2;
        this.block3 = block3;
        this.block4 = block4;
    }

    public Piece copy() {
        return pieceType.newInstance();
    }

    @Override
    public Piece clone() {
        return new Piece(pieceType, new Vector2f(rotationCenter), new Block(block1), new Block(block2), new Block(block3), new Block(block4));
    }

    public static Piece random() {
        PieceType[] values = PieceType.values();
        return values[(int) (values.length * Math.random())].newInstance();
    }

    public static Piece randomDifferentFrom(Piece piece) {
        PieceType[] values = PieceType.values();
        PieceType type = piece.pieceType;
        while (type == piece.pieceType) type = values[(int) (values.length * Math.random())];
        return type.newInstance();
    }


    public boolean canDrop(PieceType[][] board) {
        return block1.canDrop(board) && block2.canDrop(board) && block3.canDrop(board) && block4.canDrop(board);
    }

    public boolean canMoveRight(PieceType[][] board) {
        return block1.canMoveRight(board) && block2.canMoveRight(board) && block3.canMoveRight(board) && block4.canMoveRight(board);
    }

    public boolean canMoveLeft(PieceType[][] board) {
        return block1.canMoveLeft(board) && block2.canMoveLeft(board) && block3.canMoveLeft(board) && block4.canMoveLeft(board);
    }


    public void move(int x, int y) {
        block1.add(x, y);
        block2.add(x, y);
        block3.add(x, y);
        block4.add(x, y);
        rotationCenter.add(x, y);
    }

    public Color getColor() {
        return Game.getPieceColor(pieceType);
    }

    public void rotate(PieceType[][] board) {
        Block block1 = new Block(this.block1), block2 = new Block(this.block2), block3 = new Block(this.block3), block4 = new Block(this.block4);

        for (int count = 0; count < 4; count++) {
            if (block1.canRotateAround(rotationCenter, board) && block2.canRotateAround(rotationCenter, board)
                    && block3.canRotateAround(rotationCenter, board) && block4.canRotateAround(rotationCenter, board)) {
                block1.rotateAround(rotationCenter);
                block2.rotateAround(rotationCenter);
                block3.rotateAround(rotationCenter);
                block4.rotateAround(rotationCenter);
                this.block1.set(block1);
                this.block2.set(block2);
                this.block3.set(block3);
                this.block4.set(block4);
                break;
            }
            block1.rotateAround(rotationCenter);
            block2.rotateAround(rotationCenter);
            block3.rotateAround(rotationCenter);
            block4.rotateAround(rotationCenter);
        }
    }

    public void drop() {
        block1.sub(0, 1);
        block2.sub(0, 1);
        block3.sub(0, 1);
        block4.sub(0, 1);
        rotationCenter.sub(0.0F, 1.0F);
    }

    public void moveLeft() {
        block1.sub(1, 0);
        block2.sub(1, 0);
        block3.sub(1, 0);
        block4.sub(1, 0);
        rotationCenter.sub(1.0F, 0.0F);
    }

    public void moveRight() {
        block1.add(1, 0);
        block2.add(1, 0);
        block3.add(1, 0);
        block4.add(1, 0);
        rotationCenter.add(1.0F, 0.0F);
    }

    public void solidify(PieceType[][] board) {
        board[block1.y][block1.x] = pieceType;
        board[block2.y][block2.x] = pieceType;
        board[block3.y][block3.x] = pieceType;
        board[block4.y][block4.x] = pieceType;
    }
}
