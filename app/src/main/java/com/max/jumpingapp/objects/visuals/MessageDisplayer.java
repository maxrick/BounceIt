package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.Settings;

import com.max.jumpingapp.game.HelpInstructionEvent;
import com.max.jumpingapp.tutorial.ScreenMessage;
import com.max.jumpingapp.util.Constants;
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
    private ScreenMessage message;
    private int xPos;
    private int yPos;//@// TODO: 4/10/2016 create a type

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
        this.message = new ScreenMessage(Constants.JUMP_MISSED + "\n"+Constants.RELEASE_EARLIER,xPos,yPos);
    }

    public void onEvent(HelpInstructionEvent event){
        timer = System.nanoTime();
        timeToDisplay = event.getTime();
        this.message = event.getMessage();
    }

    public void draw(Canvas canvas) {
        long elapsed = System.nanoTime() - timer;
        if(elapsed < timeToDisplay){
            drawMessage(canvas);
//            canvas.drawText(Constants.JUMP_MISSED , xPos, yPos, paint);
//            canvas.drawText(Constants.RELEASE_EARLIER , xPos, yPos+60, paint);
        }
    }
    private void drawMessage(Canvas canvas) {
            drawMultiline(message.getMessage(), message.getxPos(), message.getyPos(), paint, canvas);
    }

    public void drawMultiline(String str, int x, int y, Paint paint, Canvas canvas)
    {
        for (String line: str.split("\n"))
        {
            canvas.drawText(line, x, y, paint);
            y += -paint.ascent() + paint.descent();
        }
    }
}
