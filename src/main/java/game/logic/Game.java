package game.logic;

import game.menus.BlockRenderer;
import game.menus.ParticleData;
import game.menus.ScoreDisplay;
import game.settings.IntSettings;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public final class Game {

    public final int SIZE_X, SIZE_Y;

    public static Game getInstance() {
        return instance;
    }

    public static void createInstance() {
        instance = new Game();
    }

    public static void createEmptyInstance() {
        instance = new Game();
        instance.running = false;
        instance.activePiece = null;
        Arrays.fill(instance.nextPieces, null);
    }

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

    private Game() {
        SIZE_X = IntSettings.BOARD_SIZE_X.value();
        SIZE_Y = IntSettings.BOARD_SIZE_Y.value();
        BOARD = new PieceType[SIZE_Y][SIZE_X];

        nextPieces[0] = Piece.random();
        for (int index = 1; index < nextPieces.length; index++) {
            nextPieces[index] = Piece.randomDifferentFrom(nextPieces[index - 1]);
            nextPieces[index].move(0, 4 * index);
        }

        activePiece = consumePiece();
        activePiece.move(SIZE_X / 2 - 1, SIZE_Y - 3);
    }

    private void stopGame() {
        running = false;
        ScoreDisplay.getInstance().addScore(clearedLines);
    }

    public void pause() {
        paused = !paused;
    }

    public void swapHeldPiece() {
        if (!running || paused || !canSwap) return;
        if (heldPiece == null) heldPiece = consumePiece();
        Piece temp = heldPiece.copy();
        heldPiece = activePiece.copy();
        activePiece = temp;
        activePiece.move(SIZE_X / 2 - 1, SIZE_Y - 3);
        canSwap = false;
    }

    public void moveLeft() {
        if (!running || paused) return;
        if (activePiece.canMoveLeft(BOARD)) activePiece.moveLeft();
    }

    public void moveRight() {
        if (!running || paused) return;
        if (activePiece.canMoveRight(BOARD)) activePiece.moveRight();
    }

    public  ArrayList<ParticleData> moveDown() {
        if (!running || paused) return new ArrayList<>();
        if (activePiece.canDrop(BOARD)) activePiece.drop();
        else return solidifyPiece(activePiece);
        return new ArrayList<>();
    }

    public ArrayList<ParticleData> moveHardDown() {
        if (!running || paused) return new ArrayList<>();
        while (activePiece.canDrop(BOARD)) activePiece.drop();
        return solidifyPiece(activePiece);
    }

    public void rotateActivePiece() {
        if (!running || paused) return;
        activePiece.rotate(BOARD);
    }

    public ArrayList<ParticleData> updateFrame(BlockRenderer gameScreen, BlockRenderer nextPieceScreen, BlockRenderer heldPieceScreen) {
        ArrayList<ParticleData> particles = new ArrayList<>();
        long time = System.nanoTime();
        if (time - lastTickTime > 200_000_000 && running && !paused) {
            lastTickTime = time;
            particles = updateTick();
        }
        gameScreen.renderBlocks(BOARD);
        if (activePiece != null) {
            Piece droppedPiece = activePiece.exactCopy();
            while (droppedPiece.canDrop(BOARD)) droppedPiece.drop();
            gameScreen.renderPiece(activePiece, SIZE_X, SIZE_Y, false);
            gameScreen.renderPiece(droppedPiece, SIZE_X, SIZE_Y, true);
        }
        if (heldPiece != null) heldPieceScreen.renderPiece(heldPiece, 4, 4, false);
        for (Piece nextPiece : nextPieces) if (nextPiece != null) nextPieceScreen.renderPiece(nextPiece, 4, 4 * 5, false);
        return particles;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return paused;
    }


    private ArrayList<ParticleData> updateTick() {
        if (activePiece.canDrop(BOARD)) activePiece.drop();
        else return solidifyPiece(activePiece);
        return new ArrayList<>();
    }

    private ArrayList<ParticleData> solidifyPiece(Piece piece) {
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

    private Piece consumePiece() {
        Piece next = nextPieces[nextPieces.length - 1].copy();
        for (int index = nextPieces.length - 1; index >= 1; index--) {
            nextPieces[index] = nextPieces[index - 1];
            nextPieces[index].move(0, 4);
        }
        nextPieces[0] = Piece.randomDifferentFrom(nextPieces[1]);
        return next;
    }

    private boolean isRowSolid(PieceType[] row) {
        for (PieceType pieceType : row) if (pieceType == null) return false;
        return true;
    }

    private boolean isRowNonEmpty(PieceType[] row) {
        for (PieceType pieceType : row) if (pieceType != null) return true;
        return false;
    }

    public int getClearedLines() {
        return clearedLines;
    }

    public boolean isEmptyGame() {
        return activePiece == null;
    }

    private final PieceType[][] BOARD;
    private boolean running = true;
    private boolean paused = false;
    private boolean canSwap = true;
    private long lastTickTime = 0;
    private int clearedLines = 0;

    private Piece heldPiece = null, activePiece;
    private final Piece[] nextPieces = new Piece[5];

    private static Game instance;

}
