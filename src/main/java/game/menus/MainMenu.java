package game.menus;

import core.language.CoreUiMessages;
import core.renderables.*;
import core.rendering_api.DefaultInput;
import core.rendering_api.Window;

import org.joml.Vector2f;
import org.joml.Vector2i;

import static org.lwjgl.glfw.GLFW.*;

public final class MainMenu extends UiBackgroundElement {

    public MainMenu() {
        super(new Vector2f(1.0F, 1.0F), new Vector2f(0.0F, 0.0F));

        Vector2f sizeToParent = new Vector2f(0.25F, 0.1F);
        UiButton closeApplicationButton = new UiButton(sizeToParent, new Vector2f(0.05F, 0.85F), Window::popRenderable);
        TextElement text = new TextElement(new Vector2f(0.05F, 0.5F), CoreUiMessages.QUIT_GAME);
        closeApplicationButton.addRenderable(text);

        UiButton settingsButton = new UiButton(sizeToParent, new Vector2f(0.05F, 0.725F), getSettingsAction());
        settingsButton.addRenderable(new TextElement(new Vector2f(0.05F, 0.5F), CoreUiMessages.SETTINGS));

        addRenderable(settingsButton);
        addRenderable(closeApplicationButton);
    }

    private static Clickable getSettingsAction() {
        return (Vector2i _, int _, int action) -> {
            if (action != GLFW_PRESS) return ButtonResult.IGNORE;
            Window.pushRenderable(new SettingsMenu());
            return ButtonResult.SUCCESS;
        };
    }

    @Override
    public void setOnTop() {
        Window.setInput(new DefaultInput(this));
    }
}
