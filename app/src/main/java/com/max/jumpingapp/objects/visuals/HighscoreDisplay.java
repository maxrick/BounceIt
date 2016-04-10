package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.max.jumpingapp.GamePanel;

/**
 * Created by max on 4/10/2016.
 */
public class HighscoreDisplay {
    private double[] highScores;
    private Paint paint;

    public HighscoreDisplay(double[] highScores) {
        this.highScores = highScores;
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2);
        paint.setTextSize(30);
    }

    public void draw(Canvas canvas, int moveBy) {
        if(highScores != null) {//@// TODO: 4/10/2016 better check
            for (double score : highScores) {
                drawLine(score, canvas, moveBy);
            }
        }
    }

    private void drawLine(double score, Canvas canvas, int moveBy) {
        float drawHeight = (float)(GamePanel.heightNill -score -moveBy -PlayerObject.normalHeightRect);// @// TODO: 4/10/2016 create DrawLine
        canvas.drawLine(0, drawHeight, GamePanel.screenWidth, drawHeight, paint);
        canvas.drawText(String.valueOf(score), 20, drawHeight-20, paint);
    }
}