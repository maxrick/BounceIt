package com.max.jumpingapp.tutorial;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.tutorial.tutorialPlayer.TutorialPlayerPower;

/**
 * Created by max on 4/19/2016.
 */
public class StopPlayerTouchingTrampolinEvent {

    protected int xPos;
    protected int yPos;
    private final String message;

    public StopPlayerTouchingTrampolinEvent(){
        this("");
    }

    public StopPlayerTouchingTrampolinEvent(String message){
        this(message, 100, GamePanel.screenHeight/5);
    }

    public StopPlayerTouchingTrampolinEvent(String message, int xPos, int yPos) {
        this.message = message;
        TutorialGamePanel.gamePaused = true;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public String getMessage() {
        return message;
    }
    public int getxPos(){
        return xPos;
    }
    public int getyPos(){
        return yPos;
    }
}
