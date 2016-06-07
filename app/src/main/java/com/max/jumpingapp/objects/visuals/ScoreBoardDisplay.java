package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.Score;

/**
 * Created by max on 4/11/2016.
 */
public class ScoreBoardDisplay {
    private Height height;
    private Score score;
    private Paint backgroundPaint;
    private Paint dataPaint;

    public ScoreBoardDisplay(Height height, Score score){
        this.height = height;
        this.score = score;
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.CYAN);
        dataPaint = new Paint();
        dataPaint.setColor(Color.BLACK);
        dataPaint.setTextSize(50);
    }

    public void draw(Canvas canvas) {

        canvas.drawText(score.toString(), GamePanel.screenWidth-300, 60, dataPaint);
        canvas.drawText(height.cappedToString(), 20, 60, dataPaint);
    }

    public void update(Height height, Score score) {
        this.height = height;
        this.score = score;
    }
}
