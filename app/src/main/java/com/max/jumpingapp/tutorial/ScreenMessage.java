package com.max.jumpingapp.tutorial;

import com.max.jumpingapp.game.GamePanel;

/**
 * Created by max on 5/20/2016.
 */
public class ScreenMessage {
    protected final int xPos;
    protected final int yPos;
    private final String message;

    public ScreenMessage(String message, int xPos, int yPos){
        this.message=message;
        this.xPos = xPos;
        this.yPos = yPos;
    }
    public ScreenMessage(String message){
        this.message = message;
        xPos = 60;
        yPos = GamePanel.screenHeight/5;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public String getMessage() {
        return message;
    }
}
