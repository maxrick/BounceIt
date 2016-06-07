package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.max.jumpingapp.Constants;
import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.game.JumpMissedEvent;

/**
 * Created by max on 4/10/2016.
 */
public class MessageDisplayer {
    private static final long timeToDisplayMessage = 2000000000;
    private Paint paint;
    private long timer;
    private long timeToDisplay;
    private float xPos;
    private float yPos;//@// TODO: 4/10/2016 create a type

    public MessageDisplayer(){
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        xPos = 60;
        yPos = GamePanel.screenHeight/5;
    }

    public void onEvent(JumpMissedEvent event){
        timer = System.nanoTime();
        timeToDisplay = timeToDisplayMessage;
    }

    public void draw(Canvas canvas) {
        long elapsed = System.nanoTime() - timer;
        if(elapsed < timeToDisplay){
            canvas.drawText(Constants.JUMP_MISSED , xPos, yPos, paint);
            canvas.drawText(Constants.RELEASE_EARLIER , xPos, yPos+60, paint);
        }
    }
}
