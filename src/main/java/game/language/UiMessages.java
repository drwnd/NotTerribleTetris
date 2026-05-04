package game.language;

import core.language.Translatable;

public enum UiMessages implements Translatable {
    ROTATE,
    MOVE_DOWN,
    MOVE_LEFT,
    MOVE_RIGHT,
    MOVE_HARD_DOWN,
    SWAP_HELD_PIECE,
    PAUSE,
    START_GAME,
    BEST_SCORE,
    RECENT_SCORES,
    CURRENT_SCORE,
    BOARD_SIZE_X,
    BOARD_SIZE_Y;

    @Override
    public String translationFileName() {
        return "uiMessages";
    }
}
