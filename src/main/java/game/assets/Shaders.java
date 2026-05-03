package game.assets;

import core.assets.identifiers.ShaderIdentifier;
import core.rendering_api.shaders.Shader;

public enum Shaders implements ShaderIdentifier {
    BLOCK,
    PARTICLE;

    @Override
    public Shader generateAsset() {
        return ShaderLoader.loadShader(this);
    }
}
