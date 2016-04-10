package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.max.jumpingapp.GamePanel;

/**
 * Created by max on 4/10/2016.
 */
public class MessageDisplayer {
    private Paint paint;
    private long timer;
    private long timeToDisplay;
    private String message;
    private float xPos;
    private float yPos;//@// TODO: 4/10/2016 create a type

    public MessageDisplayer(){
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        xPos = 100;
        yPos = GamePanel.screenHeight/5;
    }
    public void display(String s, long timeToDisplayMessage) {
        timer = System.nanoTime();
        timeToDisplay = timeToDisplayMessage;
        message = s;
    }

    public void draw(Canvas canvas) {
        long elapsed = System.nanoTime() - timer;
        if(elapsed < timeToDisplay){
            canvas.drawText(message , xPos, yPos, paint);
        }
    }
}
