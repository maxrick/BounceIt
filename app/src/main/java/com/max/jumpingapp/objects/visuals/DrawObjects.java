package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;

import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.Score;
import com.max.jumpingapp.types.ScoreBoardData;

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
        this.scoreBoardDisplay = new ScoreBoardDisplay(new ScoreBoardData(new Height(0), new Score(0)));
        this.powerDisplay = new PowerDisplay();
    }

    public void draw(Canvas canvas, int moveBy){
        messageDisplayer.draw(canvas);
        lastHeightDisplay.draw(canvas, moveBy);
        highscoreDisplay.draw(canvas, moveBy);
        scoreBoardDisplay.draw(canvas);
        powerDisplay.draw(canvas);
    }

    public void update(ScoreBoardData data) {
        scoreBoardDisplay.update(data);
        lastHeightDisplay.update(data.getHeight());
    }

    public void displayJumpMissed(){//// TODO: 4/11/2016 delete this method
//        messageDisplayer.display();
    }

//consturcot/
//    messageDisplayer = new MessageDisplayer();
//    lastHeightDisplay = new LastHeightDisplay();
//    highscoreDisplay = new HighscoreDisplay(highScores);

//    lastHeightDisplay.update(curHeight);

//    messageDisplayer.draw(canvas);
//    lastHeightDisplay.draw(canvas, moveBy);
//    highscoreDisplay.draw(canvas, moveBy);

//    messageDisplayer.display("Jumped missed", timeToDisplayMessage);
}
