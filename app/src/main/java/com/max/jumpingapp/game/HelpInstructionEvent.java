package com.max.jumpingapp.game;

import com.max.jumpingapp.tutorial.ScreenMessage;

/**
 * Created by max on 6/20/2016.
 */
public class HelpInstructionEvent {
    private ScreenMessage message;
    private long timeToDisplay;
    public HelpInstructionEvent(ScreenMessage message, long time){
        this.message = message;
        this.timeToDisplay = time;
    }
    public ScreenMessage getMessage(){
        return message;
    }

    public long getTime() {
        return timeToDisplay;
    }
}
