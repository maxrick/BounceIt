package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.Score;
import com.max.jumpingapp.types.ScoreBoardData;

/**
 * Created by max on 4/11/2016.
 */
public class ScoreBoardDisplay {
    private ScoreBoardData scoreBoardData;
    private Paint backgroundPaint;
    private Paint dataPaint;

    public ScoreBoardDisplay(ScoreBoardData data){
        this.scoreBoardData = data;
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.CYAN);
        dataPaint = new Paint();
        dataPaint.setColor(Color.BLACK);
    }

    public void draw(Canvas canvas) {

        canvas.drawRect(new Rect(0, 0, 150, 120), backgroundPaint);
//        canvas.drawText("Height: " + (maxHeight), 20, 20, testPaint);
        canvas.drawText(scoreBoardData.scoreString(), 20, 40, dataPaint);
        canvas.drawText(scoreBoardData.heightString(), 20, 60, dataPaint);
    }

    public void update(ScoreBoardData data) {
        this.scoreBoardData.update(data);
    }
}
