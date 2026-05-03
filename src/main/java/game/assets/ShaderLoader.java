package game.assets;

import core.assets.identifiers.ShaderIdentifier;
import core.rendering_api.shaders.GuiShader;
import core.rendering_api.shaders.Shader;

public final class ShaderLoader {

    private ShaderLoader() {

    }

    public static Shader loadShader(ShaderIdentifier identifier) {
        return switch (identifier) {

            case Shaders.BLOCK -> new GuiShader("Gui.vert", "Block.frag", identifier);
            case Shaders.PARTICLE -> new GuiShader("Particle.vert", "Block.frag", identifier);

            default -> throw new RuntimeException("Did you forget to add the line to load the shader " + identifier);
        };
    }
}
