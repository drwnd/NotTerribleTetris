package core.rendering_api;

import core.renderables.Renderable;

public class DefaultInput extends Input {

    public DefaultInput(Renderable renderable) {
        this.renderable = renderable;
    }

    @Override
    public void setInputMode() {
        setStandardInputMode();
    }

    @Override
    public void cursorPosCallback(long window, double xPos, double yPos) {
        standardCursorPosCallBack(xPos, yPos);
        renderable.hoverOver(cursorPos);
    }

    @Override
    public void mouseButtonCallback(long window, int button, int action, int mods) {
        renderable.clickOn(cursorPos, button, action);
    }

    @Override
    public void scrollCallback(long window, double xScroll, double yScroll) {
        renderable.scrollOn(cursorPos, xScroll, yScroll);
    }

    @Override
    public void keyCallback(long window, int key, int scancode, int action, int mods) {

    }

    @Override
    public void charCallback(long window, int codePoint) {

    }

    protected final Renderable renderable;
}
