package game.menus;

import core.renderables.Renderable;
import core.rendering_api.DefaultInput;
import game.logic.Game;
import game.settings.KeySettings;

import static org.lwjgl.glfw.GLFW.*;

public final class MainMenuInput extends DefaultInput {

    public MainMenuInput(Renderable renderable) {
        super(renderable);
    }

    @Override
    public void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action != GLFW_PRESS) return;
        if (key == KeySettings.PAUSE.keybind()) Game.pause();
        if (key == KeySettings.SWAP_HELD_PIECE.keybind()) Game.swapHeldPiece();
        if (key == KeySettings.MOVE_LEFT.keybind()) Game.moveLeft();
        if (key == KeySettings.MOVE_RIGHT.keybind()) Game.moveRight();
        if (key == KeySettings.MOVE_DOWN.keybind()) ((MainMenu) renderable).spawnParticles(Game.moveDown());
        if (key == KeySettings.MOVE_HARD_DOWN.keybind()) ((MainMenu) renderable).spawnParticles(Game.moveHardDown());
        if (key == KeySettings.ROTATE.keybind()) Game.rotateActivePiece();
    }
}
