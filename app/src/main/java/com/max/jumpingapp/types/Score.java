package com.max.jumpingapp.types;

/**
 * Created by max on 4/11/2016.
 */
public class Score {
    private final int score;

    public Score(int score) {
        this.score = score;
    }

    public Score update(Height height) {
        return new Score(height.maxOf(score));
    }

    @Override
    public String toString() {
        return "Score: " + score;
    }

    public boolean betterThan(int scoreNum) {
        return score > scoreNum;
    }

    public int toInt() {
        return score;
    }

    public String valueAsString() {
        return "" + score;
    }
}
