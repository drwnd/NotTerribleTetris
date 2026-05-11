package game.menus;

import core.language.CoreUiMessages;
import core.renderables.*;
import core.rendering_api.Window;
import core.settings.CoreFloatSettings;

import game.language.UiMessages;
import game.logic.Game;

import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public final class MainMenu extends UiBackgroundElement {

    public MainMenu() {
        super(new Vector2f(1.0F, 1.0F), new Vector2f(0.0F, 0.0F));
        Vector2f sizeToParent = new Vector2f(0.25F, 0.1F);

        UiButton closeApplicationButton = new UiButton(sizeToParent, new Vector2f(0.05F, 0.85F), Window::popRenderable);
        closeApplicationButton.addRenderable(new TextElement(new Vector2f(0.05F, 0.5F), CoreUiMessages.QUIT_GAME));

        UiButton settingsButton = new UiButton(sizeToParent, new Vector2f(0.05F, 0.725F), getSettingsAction());
        settingsButton.addRenderable(new TextElement(new Vector2f(0.05F, 0.5F), CoreUiMessages.SETTINGS));

        UiButton newGameButton = new UiButton(sizeToParent, new Vector2f(0.05F, 0.6F), getNewGameAction());
        newGameButton.addRenderable(new TextElement(new Vector2f(0.05F, 0.5F), UiMessages.START_GAME));

        Game.createEmptyInstance();
        addRenderable(gameScreen);
        addRenderable(nextPiecesScreen);
        addRenderable(heldPieceScreen);
        addRenderable(scoreBoard);
        setUpScreens();

        addRenderable(settingsButton);
        addRenderable(closeApplicationButton);
        addRenderable(newGameButton);
    }

    public void spawnParticles(ArrayList<ParticleData> particles) {
        float guiSize = gameScreen.scalesWithGuiSize() ? CoreFloatSettings.GUI_SIZE.value() : 1.0F;
        float rimThickness = CoreFloatSettings.RIM_THICKNESS.value() * guiSize;

        Vector2f sizeToParent = new Vector2f(1.0F, 1.0F)
                .sub(2 * rimThickness / Window.getAspectRatio(), 2 * rimThickness)
                .div(Game.getInstance().SIZE_X, Game.getInstance().SIZE_Y);

        for (ParticleData particle : particles) {
            Vector2f offsetToParent = new Vector2f(sizeToParent).mul(particle.blockX(), particle.blockY());
            gameScreen.addRenderable(new Particle(sizeToParent, offsetToParent, particle.pieceType()));
        }
    }

    private Clickable getSettingsAction() {
        return (Vector2i _, int _, int action) -> {
            if (action != GLFW_PRESS) return ButtonResult.IGNORE;
            if (Game.getInstance().isRunning() && !Game.getInstance().isPaused()) return ButtonResult.FAILURE;
            Window.pushRenderable(new SettingsMenu());
            return ButtonResult.SUCCESS;
        };
    }

    private Clickable getNewGameAction() {
        return (Vector2i _, int _, int action) -> {
            if (action != GLFW_PRESS) return ButtonResult.IGNORE;
            if (Game.getInstance().isRunning()) return ButtonResult.FAILURE;
            Game.createInstance();
            setUpScreens();
            return ButtonResult.SUCCESS;
        };
    }

    @Override
    public void renderSelf(Vector2f position, Vector2f size) {
        super.renderSelf(position, size);
        ArrayList<ParticleData> particles = Game.getInstance().updateFrame(gameScreen, nextPiecesScreen, heldPieceScreen);
        spawnParticles(particles);

        long time = System.nanoTime();
        gameScreen.getChildren().removeIf(renderable -> renderable instanceof Particle particle && time - particle.getSpawnTime() > 2_000_000_000);
    }

    @Override
    public void setOnTop() {
        Window.setInput(new MainMenuInput(this));
        if (Game.getInstance().isEmptyGame()) {
            Game.createEmptyInstance();
            setUpScreens();
        }
    }

    @Override
    protected void resizeSelfTo(int width, int height) {
        setUpScreens();
    }

    @Override
    public void deleteSelf() {

    }

    private void setUpScreens() {
        float aspectRatio = Window.getAspectRatio();
        float blockSizeX = 0.08F;
        float blockSizeY = 0.08F * aspectRatio;

        gameScreen.setSizeToParent(0.95F / (Game.getInstance().SIZE_Y * aspectRatio / Game.getInstance().SIZE_X), 0.95F);
        gameScreen.setOffsetToParent(0.5F - gameScreen.getSizeToParent().x * 0.5F, 0.025F);

        heldPieceScreen.setSizeToParent(blockSizeX, blockSizeY);
        heldPieceScreen.setOffsetToParent(gameScreen.getOffsetToParent().x + gameScreen.getSizeToParent().x + 0.025F, 0.975F - blockSizeY);

        nextPiecesScreen.setSizeToParent(blockSizeX, blockSizeY * 5);
        nextPiecesScreen.setOffsetToParent(gameScreen.getOffsetToParent().x + gameScreen.getSizeToParent().x + 0.025F, 0.925F - 6 * blockSizeY);

        scoreBoard.setSizeToParent(0.95F / (3 * aspectRatio), 0.95F);
        scoreBoard.setOffsetToParent(nextPiecesScreen.getOffsetToParent().x + nextPiecesScreen.getSizeToParent().x + 0.025F, 0.025F);
    }

    private final BlockRenderer gameScreen = new BlockRenderer(new Vector2f(), new Vector2f());
    private final BlockRenderer nextPiecesScreen = new BlockRenderer(new Vector2f(), new Vector2f());
    private final BlockRenderer heldPieceScreen = new BlockRenderer(new Vector2f(), new Vector2f());
    private final ScoreDisplay scoreBoard = ScoreDisplay.newInstance(new Vector2f(), new Vector2f());
}
