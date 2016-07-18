package com.maxrick.bounceit.objects.visuals;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.max.jumpingapp.R;
import com.maxrick.bounceit.types.Height;
import com.maxrick.bounceit.types.Score;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/11/2016.
 */
public class DrawObjects {
    private MessageDisplayer messageDisplayer;
    private LastHeightDisplay lastHeightDisplay;
    private HighscoreDisplay highscoreDisplay;
    private ScoreBoardDisplay scoreBoardDisplay;
    private PowerDisplay powerDisplay;
//    public static Bitmap fingerReleaseImage;
    public static Bitmap fingerTouchImage;

    public DrawObjects(int[] highScores, Resources resources){
        this.messageDisplayer = new MessageDisplayer();
        this.lastHeightDisplay = new LastHeightDisplay();
        this.highscoreDisplay = new HighscoreDisplay(highScores);
        this.scoreBoardDisplay = new ScoreBoardDisplay(new Height(0), new Score(0));
        this.powerDisplay = new PowerDisplay();
        fingerTouchImage = BitmapFactory.decodeResource(resources, R.drawable.fingertouch);
        //fingerReleaseImage = BitmapFactory.decodeResource(resources, R.drawable.fingertouch);
        EventBus.getDefault().register(powerDisplay);
        EventBus.getDefault().register(messageDisplayer);
    }

    public void draw(Canvas canvas, int moveBy){
        messageDisplayer.draw(canvas);
        lastHeightDisplay.draw(canvas, moveBy);
        highscoreDisplay.draw(canvas, moveBy);
        scoreBoardDisplay.draw(canvas);
        powerDisplay.draw(canvas, scoreBoardDisplay.currentPaintColor());
    }

    public void update(Height height, Score score) {
        scoreBoardDisplay.update(height, score);
        lastHeightDisplay.update(height);
    }

    public void unregisterEventlisteners(){
        EventBus.getDefault().unregister(powerDisplay);
        EventBus.getDefault().unregister(messageDisplayer);
    }

}
