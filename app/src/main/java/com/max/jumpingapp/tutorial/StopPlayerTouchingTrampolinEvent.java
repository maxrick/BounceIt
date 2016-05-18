package com.max.jumpingapp.tutorial;

import com.max.jumpingapp.tutorial.tutorialPlayer.TutorialPlayerPower;

/**
 * Created by max on 4/19/2016.
 */
public class StopPlayerTouchingTrampolinEvent {

    public static final String HOLD_DOWN_FULL = "Hold down to increase power";
    private final String message;

    public StopPlayerTouchingTrampolinEvent(){
        this("");
    }

    public StopPlayerTouchingTrampolinEvent(String holdDownFull) {
        this.message = holdDownFull;
        TutorialGamePanel.gamePaused = true;
    }

    public String getMessage() {
        return message;
    }
}
