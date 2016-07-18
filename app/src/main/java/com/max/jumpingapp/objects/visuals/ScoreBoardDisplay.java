package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.Score;
import com.max.jumpingapp.util.MathHelper;

/**
 * Created by max on 4/11/2016.
 */
public class ScoreBoardDisplay {
    public static final int TEXT_SIZE = MathHelper.adjustToScreensize(50);
    public static final int GAP_RIGHT = MathHelper.adjustToScreensize(300);
    public static final int GAP_LEFT = MathHelper.adjustToScreensize(20);
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

        canvas.drawText(score.toString(), GamePanel.screenWidth- GAP_RIGHT, MathHelper.adjustToScreensize(60), dataPaint);
        canvas.drawText(height.cappedToString(), GAP_LEFT, MathHelper.adjustToScreensize(60), dataPaint);
    }

    public void update(Height height, Score score) {
        this.height = height;
        this.score = score;
        float colorValue = height.devideByReal(10000);
        if(colorValue<0){
            colorValue=0;
        }
        if(colorValue>1){
            colorValue=1;
        }
        int colorRed = (int) (Math.pow(colorValue,8)*255);
        int colorGreen = (int) (Math.pow(colorValue,8)*255);
        int colorBlue = (int) (Math.pow(colorValue,8)*255);
        dataPaint.setColor(Color.rgb(colorRed, colorGreen, colorBlue));
    }

    public int currentPaintColor(){
        return dataPaint.getColor();
    }
}
