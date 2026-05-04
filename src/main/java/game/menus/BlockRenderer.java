package game.menus;

import core.assets.AssetManager;
import core.assets.CoreShaders;
import core.assets.Texture;
import core.renderables.Renderable;
import core.rendering_api.Window;
import core.rendering_api.shaders.GuiShader;
import core.settings.CoreFloatSettings;
import core.settings.optionSettings.TexturePack;

import game.assets.Shaders;
import game.assets.Textures;

import game.logic.Block;
import game.logic.Game;
import game.logic.Piece;
import game.logic.PieceType;
import org.joml.Vector2f;

import java.awt.*;
import java.util.ArrayList;

public class BlockRenderer extends Renderable {

    public BlockRenderer(Vector2f sizeToParent, Vector2f offsetToParent) {
        super(sizeToParent, offsetToParent);
        setDoAutoFocusScaling(false);
        setPlayFocusSound(false);
    }

    @Override
    protected void renderSelf(Vector2f position, Vector2f size) {
        float guiSize = scalesWithGuiSize() ? CoreFloatSettings.GUI_SIZE.value() : 1.0F;
        float rimThickness = CoreFloatSettings.RIM_THICKNESS.value() * guiSize;

        GuiShader shader = (GuiShader) AssetManager.get(CoreShaders.GUI_BACKGROUND);
        Texture background = AssetManager.get(TexturePack.get(Textures.BACKGROUND));
        shader.bind();
        shader.setUniform("rimWidth", CoreFloatSettings.RIM_THICKNESS.value() * guiSize);
        shader.setUniform("aspectRatio", Window.getAspectRatio());
        shader.drawQuadCustomScale(position, size, background, guiSize);

        position.add(rimThickness / Window.getAspectRatio(), rimThickness);
        size.sub(2 * rimThickness / Window.getAspectRatio(), 2 * rimThickness);

        if (blocks != null) renderBlocks(position, size);
        for (RenderPiece piece : pieces) renderLivePiece(position, size, piece);
        blocks = null;
        pieces.clear();
    }

    public void renderBlocks(PieceType[][] blocks) {
        this.blocks = blocks;
    }

    public void renderPiece(Piece piece, int boardSizeX, int boardSizeY, boolean transparent) {
        pieces.add(new RenderPiece(piece, boardSizeX, boardSizeY, transparent));
    }

    @Override
    public void setSizeToParent(float x, float y) {
        float guiSize = scalesWithGuiSize() ? CoreFloatSettings.GUI_SIZE.value() : 1.0F;
        float rimThickness = CoreFloatSettings.RIM_THICKNESS.value() * guiSize;
        super.setSizeToParent(x + 2 * rimThickness / Window.getAspectRatio(), y + 2 * rimThickness);
    }

    @Override
    public void setOffsetToParent(float x, float y) {
        float guiSize = scalesWithGuiSize() ? CoreFloatSettings.GUI_SIZE.value() : 1.0F;
        float rimThickness = CoreFloatSettings.RIM_THICKNESS.value() * guiSize;
        super.setOffsetToParent(x - rimThickness / Window.getAspectRatio(), y - rimThickness);
    }

    private void renderBlocks(Vector2f position, Vector2f size) {
        GuiShader shader = (GuiShader) AssetManager.get(Shaders.BLOCK);
        Texture blockTexture = AssetManager.get(TexturePack.get(Textures.BLOCK));
        shader.bind();

        Vector2f blockPosition = new Vector2f();
        Vector2f blockSize = new Vector2f(size).div(blocks[0].length, blocks.length);

        for (int blockY = 0; blockY < blocks.length; blockY++)
            for (int blockX = 0; blockX < blocks[blockY].length; blockX++) {
                PieceType piece = blocks[blockY][blockX];
                if (piece == null) continue;

                blockPosition.set(blockSize).mul(blockX, blockY).add(position);
                shader.setUniform("color", Game.getPieceColor(piece));
                shader.drawQuad(blockPosition, blockSize, blockTexture);
            }
    }

    private void renderLivePiece(Vector2f position, Vector2f size, RenderPiece piece) {
        GuiShader shader = (GuiShader) AssetManager.get(Shaders.BLOCK);
        Texture blockTexture = AssetManager.get(TexturePack.get(Textures.BLOCK));
        shader.bind();

        Color color = piece.piece().getColor();
        if (piece.transparent) color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 127);
        shader.setUniform("color", color);

        Vector2f blockSize = new Vector2f(size).div(piece.boardSizeX, piece.boardSizeY);
        renderBlock(shader, blockTexture, position, blockSize, piece.piece.block1);
        renderBlock(shader, blockTexture, position, blockSize, piece.piece.block2);
        renderBlock(shader, blockTexture, position, blockSize, piece.piece.block3);
        renderBlock(shader, blockTexture, position, blockSize, piece.piece.block4);
    }

    private void renderBlock(GuiShader shader, Texture blockTexture, Vector2f position, Vector2f size, Block block) {
        Vector2f blockPosition = new Vector2f(size).mul(block.x, block.y).add(position);
        shader.drawQuad(blockPosition, size, blockTexture);
    }

    private PieceType[][] blocks;
    private final ArrayList<RenderPiece> pieces = new ArrayList<>();

    private record RenderPiece(Piece piece, int boardSizeX, int boardSizeY, boolean transparent) {
    }
}
