package com.max.jumpingapp.tutorial.tutorialPlayer;

import com.max.jumpingapp.objects.visuals.PlayerObject;
import com.max.jumpingapp.util.Constants;
import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.objects.player.PlayerStatusFreeRising;
import com.max.jumpingapp.tutorial.FingerTouchingEvent;
import com.max.jumpingapp.tutorial.StopPlayerStatusInAir;
import com.max.jumpingapp.tutorial.StopPlayerTouchingTrampolinEvent;
import com.max.jumpingapp.tutorial.TutorialGamePanel;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayerStatusFreeRising extends PlayerStatusFreeRising {
    private double percentagePassedBeforeStop=0;

    public TutorialPlayerStatusFreeRising(double oscPeriod, double fallPeriod, PlayerObject playerObject) {
        super(oscPeriod, fallPeriod, playerObject);
        EventBus.getDefault().register(this);//@// TODO: 5/19/2016 not good
    }

    public void onEvent(FingerTouchingEvent event){
        continueIfPaused();
    }

    private void continueIfPaused() {
        if(percentagePassedBeforeStop > 0){
            continueGame();
        }
    }

    public void onEvent(StopPlayerStatusInAir event){
        stopGame(Constants.WIND);
    }
    private void continueGame() {
        long minusFaktor = (long) (percentagePassedBeforeStop*oscPeriod);
        GamePanel.lastUpdateTime = System.nanoTime() - minusFaktor;
        TutorialPlayer.getTutorialPlayer().unPause(oscPeriod);
    }

    private void stopGame(String message) {
        double elapsedNanos = System.nanoTime() - GamePanel.lastUpdateTime;
        percentagePassedBeforeStop = elapsedNanos / oscPeriod;
        TutorialGamePanel.onlyRestartWhenSwiped =true;
        TutorialPlayer.getTutorialPlayer().pause();
        EventBus.getDefault().post(new StopPlayerTouchingTrampolinEvent(message, 50, GamePanel.screenHeight/2));
    }
}
