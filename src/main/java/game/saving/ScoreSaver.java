package game.saving;

import core.utils.Saver;

import java.util.ArrayList;

public final class ScoreSaver extends Saver<ScoreBoard> {

    public static final String FILE_LOCATION = "saves/scores";

    @Override
    protected void save(ScoreBoard scoreBoard) {
        saveInt(scoreBoard.best());
        saveInt(scoreBoard.scores().size());
        for (int score: scoreBoard.scores()) saveInt(score);
    }

    @Override
    protected ScoreBoard load() {
        int best = loadInt();
        int count = loadInt();
        ArrayList<Integer> scores = new ArrayList<>(count);
        for (int index = 0; index < count; index++) scores.add(loadInt());
        return new ScoreBoard(best, scores);
    }

    @Override
    protected ScoreBoard getDefault() {
        return new ScoreBoard(0, new ArrayList<>());
    }

    @Override
    protected int getVersionNumber() {
        return 0;
    }
}
