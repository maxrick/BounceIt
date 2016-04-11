package com.max.jumpingapp.types;

/**
 * Created by max on 4/11/2016.
 */
public class ScoreBoardData {
    private Height height;
    private Score score;

    public ScoreBoardData(Height height, Score score){
        this.height = height;
        this.score = score;
    }

    public String scoreString() {
        return score.toString();
    }

    public String heightString() {
        return height.toString();
    }

    public Height getHeight() {
        return height;
    }

    public void update(ScoreBoardData data) {
        height = data.height;
        score.setNewMaximum(data.score);
    }
}
