package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.max.jumpingapp.GamePanel;
import com.max.jumpingapp.objects.Game;

/**
 * Created by max on 4/10/2016.
 */
public class LastHeightDisplay {
    private Paint paint;
    private double height;
    private String message;

    public LastHeightDisplay(){
        paint = new Paint();
        paint.setColor(Color.WHITE);
        this.paint.setStrokeWidth(2);
        this.paint.setStyle(Paint.Style.STROKE);
        height = 0;
    }

    public void update(double currentHeight) {
        if(currentHeight > height){
            height = currentHeight;
        }
    }

    public void draw(Canvas canvas, int moveBy) {
//        Path myPath = new Path();
//        myPath.moveTo(0, (float) height);
//        myPath.moveTo(GamePanel.screenWidth, (float) height);
//        myPath.offset(0, -moveBy);
        float drawHeight = (float)(GamePanel.heightNill - height -moveBy -PlayerObject.normalHeightRect);
        canvas.drawLine(0, drawHeight, GamePanel.screenWidth, drawHeight, paint);
//        canvas.drawPath(myPath, paint);
    }
}
