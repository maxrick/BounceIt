package com.max.jumpingapp.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.max.jumpingapp.objects.visuals.DrawObjects;
import com.max.jumpingapp.tutorial.ScreenMessage;
import com.max.jumpingapp.util.MathHelper;

/**
 * Created by max on 6/20/2016.
 */
public class HelpInstructionEvent extends DrawableEvent {
    protected ScreenMessage message;

    public HelpInstructionEvent(ScreenMessage message, long time){
        super(time);
        this.message = message;
    }
    public ScreenMessage getMessage(){
        return message;
    }

    public long getTime() {
        return timeToDisplay;
    }

    public void draw(Canvas canvas) {
        drawMultiline(message.getMessage(), message.getxPos(), message.getyPos(), paint, canvas);
    }

}
