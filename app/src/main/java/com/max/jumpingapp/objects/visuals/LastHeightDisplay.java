package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.types.Height;

/**
 * Created by max on 4/10/2016.
 */
public class LastHeightDisplay {
    private Paint paint;
    private Height height;
    private String message;

    public LastHeightDisplay(){
        paint = new Paint();
        paint.setColor(Color.WHITE);
        this.paint.setStrokeWidth(2);
        this.paint.setStyle(Paint.Style.STROKE);
        height = new Height(0);
    }

    public void update(Height currentHeight) {
        height = height.setNewMaximum(currentHeight);
    }

    public void draw(Canvas canvas, int moveBy) {
        float drawHeight = height.calculateDrawHeight(moveBy);
        canvas.drawLine(0, drawHeight, GamePanel.screenWidth, drawHeight, paint);
    }
}
