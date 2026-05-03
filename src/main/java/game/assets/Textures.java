package game.assets;

import core.assets.identifiers.TextureIdentifier;

public enum Textures implements TextureIdentifier {
    BLOCK("Block.png"),
    BACKGROUND("Background.png");

    Textures(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String fileName() {
        return fileName;
    }

    private final String fileName;
}
