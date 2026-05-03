package game.logic;

import org.joml.Vector2f;
import org.joml.Vector2i;

public final class Block extends Vector2i {

    public Block(int x, int y) {
        super(x, y);
    }

    public Block(Block block) {
        super(block);
    }

    public Block() {
        super();
    }

    public boolean canDrop(PieceType[][] board) {
        return y > 0 && board[y - 1][x] == null;
    }

    public boolean canMoveRight(PieceType[][] board) {
        return x < Game.SIZE_X - 1 && board[y][x + 1] == null;
    }

    public boolean canMoveLeft(PieceType[][] board) {
        return x > 0 && board[y][x - 1] == null;
    }

    public boolean canRotateAround(Vector2f rotationCenter, PieceType[][] board) {
        int rotatedX = getRotatedX(rotationCenter);
        int rotatedY = getRotatedY(rotationCenter);

        if (rotatedX < 0 || rotatedX >= Game.SIZE_X) return false;
        if (rotatedY < 0 || rotatedY >= Game.SIZE_Y) return false;
        return board[rotatedY][rotatedX] == null;
    }

    public void rotateAround(Vector2f rotationCenter) {
        int rotatedX = getRotatedX(rotationCenter);
        int rotatedY = getRotatedY(rotationCenter);
        set(rotatedX, rotatedY);
    }

    private int getRotatedY(Vector2f rotationCenter) {
        return (int) (rotationCenter.y + rotationCenter.x - x);
    }

    private int getRotatedX(Vector2f rotationCenter) {
        return (int) (rotationCenter.x - rotationCenter.y + y);
    }
}
