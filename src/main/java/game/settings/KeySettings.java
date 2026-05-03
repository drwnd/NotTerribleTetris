package game.settings;

import core.settings.KeySetting;

import static org.lwjgl.glfw.GLFW.*;


public enum KeySettings implements KeySetting {
    ROTATE(GLFW_KEY_W),
    MOVE_DOWN(GLFW_KEY_S),
    MOVE_LEFT(GLFW_KEY_A),
    MOVE_RIGHT(GLFW_KEY_D),
    MOVE_HARD_DOWN(GLFW_KEY_LEFT_SHIFT),
    SWAP_HELD_PIECE(GLFW_KEY_SPACE),
    PAUSE(GLFW_KEY_P);

    KeySettings(int defaultValue) {
        this.defaultKeybind = defaultValue;
        this.keybind = defaultValue;
    }


    @Override
    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    @Override
    public int keybind() {
        return keybind;
    }

    @Override
    public int defaultKeybind() {
        return defaultKeybind;
    }

    private final int defaultKeybind;
    private int keybind;
}
