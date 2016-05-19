package com.max.jumpingapp.tutorial;

import com.max.jumpingapp.tutorial.tutorialPlayer.TutorialPlayerPower;

/**
 * Created by max on 4/19/2016.
 */
public class StopPlayerTouchingTrampolinEvent {

    public static final String HOLD_DOWN_FULL = "Hold down to increase power";
    public static final String RELEASE = "Release to activate power";
    public static final String WIND = "Sometimes wind will move you sideways.\nSwipe to move back to the middle.\nSwipe more often and longer to move quicker.";
    private final String message;

    public StopPlayerTouchingTrampolinEvent(){
        this("");
    }

    public StopPlayerTouchingTrampolinEvent(String message) {
        this.message = message;
        TutorialGamePanel.gamePaused = true;
    }

    public String getMessage() {
        return message;
    }
}
