package com.maxrick.bounceit.events;

import android.graphics.Canvas;

import com.maxrick.bounceit.tutorial.ScreenMessage;

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
