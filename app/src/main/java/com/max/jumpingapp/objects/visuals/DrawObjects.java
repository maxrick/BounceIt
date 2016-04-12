package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;

import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.Score;

/**
 * Created by max on 4/11/2016.
 */
public class DrawObjects {
    private MessageDisplayer messageDisplayer;
    private LastHeightDisplay lastHeightDisplay;
    private HighscoreDisplay highscoreDisplay;
    private ScoreBoardDisplay scoreBoardDisplay;
    private PowerDisplay powerDisplay;

    public DrawObjects(int[] highScores){
        this.messageDisplayer = new MessageDisplayer();
        this.lastHeightDisplay = new LastHeightDisplay();
        this.highscoreDisplay = new HighscoreDisplay(highScores);
        this.scoreBoardDisplay = new ScoreBoardDisplay(new Height(0), new Score(0));
        this.powerDisplay = new PowerDisplay();
    }

    public void draw(Canvas canvas, int moveBy){
        messageDisplayer.draw(canvas);
        lastHeightDisplay.draw(canvas, moveBy);
        highscoreDisplay.draw(canvas, moveBy);
        scoreBoardDisplay.draw(canvas);
        powerDisplay.draw(canvas);
    }

    public void update(Height height, Score score) {
        scoreBoardDisplay.update(height, score);
        lastHeightDisplay.update(height);
    }

}
