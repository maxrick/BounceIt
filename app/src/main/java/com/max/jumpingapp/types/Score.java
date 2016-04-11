package com.max.jumpingapp.types;

/**
 * Created by max on 4/11/2016.
 */
public class Score {
    private int score;

    public Score(int score){
        this.score = score;
    }

    public void update(int height) {
        if(height > score){
            score = height;
        }
    }

    @Override
    public String toString() {
        return "Score: "+score;
    }

    public boolean betterThan(int scoreNum) {
        return score > scoreNum;
    }

    public int toInt() {
        return score;
    }

    public void setNewMaximum(Score score) {
        this.score = score.maximumOf(this.score);
    }

    private int maximumOf(int score) {
        return Math.max(this.score, score);
    }

    public String valueAsString() {
        return ""+score;
    }
}
