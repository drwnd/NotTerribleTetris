package game.menus;

import core.assets.AssetManager;
import core.assets.Texture;
import core.renderables.Renderable;
import core.rendering_api.shaders.GuiShader;
import core.settings.optionSettings.TexturePack;
import game.assets.Shaders;
import game.assets.Textures;
import game.logic.Game;
import game.logic.PieceType;
import org.joml.Vector2f;

import java.awt.*;

public final class Particle extends Renderable {

    public Particle(Vector2f sizeToParent, Vector2f offsetToParent, PieceType type) {
        super(sizeToParent, offsetToParent);

        this.color = Game.getPieceColor(type);
        this.spawnTime = System.nanoTime();
        this.rotationSpeed = (float) (Math.random() * 6 - 3);
        this.velocity = new Vector2f((float) (Math.random() * 0.2 - 0.1), (float) (Math.random() * 0.2 - 0.1));
    }

    @Override
    protected void renderSelf(Vector2f position, Vector2f size) {
        float aliveSeconds = (float) ((System.nanoTime() - spawnTime) / 1_000_000_000D);

        Texture blockTexture = AssetManager.get(TexturePack.get(Textures.BLOCK));
        GuiShader shader = (GuiShader) AssetManager.get(Shaders.PARTICLE);

        shader.bind();
        shader.setUniform("color", color);
        shader.setUniform("gravity", GRAVITY);
        shader.setUniform("aliveSeconds", aliveSeconds);
        shader.setUniform("rotationSpeed", rotationSpeed);
        shader.setUniform("velocity", velocity);

        shader.drawQuad(position, size, blockTexture);
    }

    public long getSpawnTime() {
        return spawnTime;
    }

    private final Vector2f velocity;
    private final float rotationSpeed;
    private final Color color;
    private final long spawnTime;

    private static final float GRAVITY = 0.5F;
}
