package game.menus;

import core.renderables.TextElement;
import game.language.UiMessages;
import game.logic.Game;
import game.saving.ScoreBoard;
import game.saving.ScoreSaver;
import org.joml.Vector2f;

import java.awt.*;
import java.util.ArrayList;

public final class ScoreDisplay extends BlockRenderer {

    private static ScoreDisplay instance;

    public static ScoreDisplay newInstance(Vector2f sizeToParent, Vector2f offsetToParent) {
        instance = new ScoreDisplay(sizeToParent, offsetToParent);
        return getInstance();
    }

    public static ScoreDisplay getInstance() {
        return instance;
    }

    private ScoreDisplay(Vector2f sizeToParent, Vector2f offsetToParent) {
        super(sizeToParent, offsetToParent);

        scoreBoard = new ScoreSaver().load(ScoreSaver.FILE_LOCATION);
        loadDisplays();
    }

    private void loadDisplays() {
        getChildren().clear();

        addRenderable(new TextElement(new Vector2f(0.05F, 0.95F), () -> UiMessages.BEST_SCORE.get() + scoreBoard.best(), Color.RED));
        addRenderable(new TextElement(new Vector2f(0.05F, 0.9F), () -> UiMessages.CURRENT_SCORE.get() + Game.getClearedLines(), Color.ORANGE));
        addRenderable(new TextElement(new Vector2f(0.05F, 0.85F), UiMessages.RECENT_SCORES, Color.YELLOW));

        ArrayList<Integer> scores = scoreBoard.scores();
        for (int count = 0; count < Math.min(16, scores.size()); count++) {
            int finalCount = count;
            addRenderable(new TextElement(new Vector2f(0.05F, 0.8F - count * 0.05F), () -> String.valueOf(scores.get(finalCount))));
        }
    }

    public void addScore(int clearedLines) {
        int best = Math.max(clearedLines, scoreBoard.best());
        ArrayList<Integer> scores = scoreBoard.scores();

        scores.addFirst(best);
        while (scores.size() > 16) scores.removeLast();

        scoreBoard = new ScoreBoard(best, scores);
        loadDisplays();
    }

    public void save() {
        new ScoreSaver().save(scoreBoard, ScoreSaver.FILE_LOCATION);
    }

    private ScoreBoard scoreBoard;
}
