package game.logic;

import game.menus.BlockRenderer;
import game.menus.ParticleData;
import game.menus.ScoreDisplay;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public final class Game {

    public static final int SIZE_X = 10;
    public static final int SIZE_Y = 30;

    public static Color getPieceColor(PieceType piece) {
        return switch (piece) {
            case PieceType.I -> new Color(0, 253, 253);
            case PieceType.O -> new Color(253, 253, 0);
            case PieceType.T -> new Color(152, 0, 253);
            case PieceType.S -> new Color(0, 253, 0);
            case PieceType.Z -> new Color(253, 0, 0);
            case PieceType.L -> new Color(253, 169, 0);
            case PieceType.J -> new Color(0, 0, 253);
        };
    }

    public static void startGame() {
        for (PieceType[] row : BOARD) Arrays.fill(row, null);
        paused = false;
        running = true;
        canSwap = true;
        clearedLines = 0;

        nextPieces[0] = Piece.random();
        for (int index = 1; index < nextPieces.length; index++) {
            nextPieces[index] = Piece.randomDifferentFrom(nextPieces[index - 1]);
            nextPieces[index].move(0, 4 * index);
        }

        heldPiece = null;
        activePiece = consumePiece();
        activePiece.move(SIZE_X / 2 - 1, SIZE_Y - 3);
    }

    private static void stopGame() {
        running = false;
        ScoreDisplay.getInstance().addScore(clearedLines);
    }

    public static void pause() {
        paused = !paused;
    }

    public static void swapHeldPiece() {
        if (!running || paused || !canSwap) return;
        if (heldPiece == null) heldPiece = consumePiece();
        Piece temp = heldPiece.copy();
        heldPiece = activePiece.copy();
        activePiece = temp;
        activePiece.move(SIZE_X / 2 - 1, SIZE_Y - 3);
        canSwap = false;
    }

    public static void moveLeft() {
        if (!running || paused) return;
        if (activePiece.canMoveLeft(BOARD)) activePiece.moveLeft();
    }

    public static void moveRight() {
        if (!running || paused) return;
        if (activePiece.canMoveRight(BOARD)) activePiece.moveRight();
    }

    public static ArrayList<ParticleData> moveDown() {
        if (!running || paused) return new ArrayList<>();
        if (activePiece.canDrop(BOARD)) activePiece.drop();
        else return solidifyPiece(activePiece);
        return new ArrayList<>();
    }

    public static ArrayList<ParticleData> moveHardDown() {
        if (!running || paused) return new ArrayList<>();
        while (activePiece.canDrop(BOARD)) activePiece.drop();
        return solidifyPiece(activePiece);
    }

    public static void rotateActivePiece() {
        if (!running || paused) return;
        activePiece.rotate(BOARD);
    }

    public static ArrayList<ParticleData> updateFrame(BlockRenderer gameScreen, BlockRenderer nextPieceScreen, BlockRenderer heldPieceScreen) {
        ArrayList<ParticleData> particles = new ArrayList<>();
        long time = System.nanoTime();
        if (time - lastTickTime > 200_000_000 && running && !paused) {
            lastTickTime = time;
            particles = updateTick();
        }
        gameScreen.renderBlocks(BOARD);
        if (activePiece != null) {
            Piece droppedPiece = activePiece.clone();
            while (droppedPiece.canDrop(BOARD)) droppedPiece.drop();
            gameScreen.renderPiece(activePiece, SIZE_X, SIZE_Y, false);
            gameScreen.renderPiece(droppedPiece, SIZE_X, SIZE_Y, true);
        }
        if (heldPiece != null) heldPieceScreen.renderPiece(heldPiece, 4, 4, false);
        for (Piece nextPiece : nextPieces) if (nextPiece != null) nextPieceScreen.renderPiece(nextPiece, 4, 4 * 5, false);
        return particles;
    }

    public static boolean isRunning() {
        return running;
    }

    public static boolean isPaused() {
        return paused;
    }


    private static ArrayList<ParticleData> updateTick() {
        if (activePiece.canDrop(BOARD)) activePiece.drop();
        else return solidifyPiece(activePiece);
        return new ArrayList<>();
    }

    private static ArrayList<ParticleData> solidifyPiece(Piece piece) {
        canSwap = true;
        piece.solidify(BOARD);

        ArrayList<ParticleData> particles = new ArrayList<>();
        int clearCount = 0;

        for (int row = SIZE_Y - 1; row >= 0; row--)
            if (isRowSolid(BOARD[row])) {
                for (int index = 0; index < SIZE_X; index++) particles.add(new ParticleData(index, row, BOARD[row][index]));
                for (int index = row; index < SIZE_Y - 1; index++) BOARD[index] = BOARD[index + 1];
                BOARD[SIZE_Y - 1] = new PieceType[SIZE_X];
                clearCount++;
            }

        if (isRowNonEmpty(BOARD[SIZE_Y - 1])) stopGame();
        else {
            activePiece = consumePiece();
            activePiece.move(SIZE_X / 2 - 1, SIZE_Y - 3);
        }

        clearedLines += clearCount;
        return particles;
    }

    private static Piece consumePiece() {
        Piece next = nextPieces[nextPieces.length - 1].copy();
        for (int index = nextPieces.length - 1; index >= 1; index--) {
            nextPieces[index] = nextPieces[index - 1];
            nextPieces[index].move(0, 4);
        }
        nextPieces[0] = Piece.randomDifferentFrom(nextPieces[1]);
        return next;
    }

    private static boolean isRowSolid(PieceType[] row) {
        for (PieceType pieceType : row) if (pieceType == null) return false;
        return true;
    }

    private static boolean isRowNonEmpty(PieceType[] row) {
        for (PieceType pieceType : row) if (pieceType != null) return true;
        return false;
    }

    public static int getClearedLines() {
        return clearedLines;
    }

    private static final PieceType[][] BOARD = new PieceType[SIZE_Y][SIZE_X];
    private static boolean running = false;
    private static boolean paused = false;
    private static boolean canSwap = false;
    private static long lastTickTime = 0;
    private static int clearedLines;

    private static Piece heldPiece, activePiece;
    private static final Piece[] nextPieces = new Piece[5];
}
