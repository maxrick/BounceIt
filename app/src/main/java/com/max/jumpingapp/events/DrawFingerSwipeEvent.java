package com.max.jumpingapp.events;

import com.max.jumpingapp.tutorial.ScreenMessage;

/**
 * Created by Max Rickayzen on 23.06.2016.
 */
public abstract class DrawFingerSwipeEvent extends DrawableEvent {
    protected ScreenMessage message;

    public DrawFingerSwipeEvent(ScreenMessage message, long time){
        super(time);
        this.message = message;
    }
    public ScreenMessage getMessage(){
        return message;
    }

    public long getTime() {
        return timeToDisplay;
    }

}
