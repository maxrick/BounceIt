package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.Score;

/**
 * Created by max on 4/11/2016.
 */
public class ScoreBoardDisplay {
    public static final int TEXT_SIZE = 50;
    public static final int GAP_RIGHT = 300;
    public static final int GAP_LEFT = 20;
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
        dataPaint.setTextSize(TEXT_SIZE);
    }

    public void draw(Canvas canvas) {

        canvas.drawText(score.toString(), GamePanel.screenWidth- GAP_RIGHT, 60, dataPaint);
        canvas.drawText(height.cappedToString(), GAP_LEFT, 60, dataPaint);
    }

    public void update(Height height, Score score) {
        this.height = height;
        this.score = score;
    }
}
