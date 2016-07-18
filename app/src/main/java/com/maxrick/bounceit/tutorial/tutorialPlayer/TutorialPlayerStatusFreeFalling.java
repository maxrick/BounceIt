package com.maxrick.bounceit.tutorial.tutorialPlayer;

import com.maxrick.bounceit.objects.visuals.PlayerObject;
import com.maxrick.bounceit.util.Constants;
import com.maxrick.bounceit.events.FingerReleasedEvent;
import com.maxrick.bounceit.game.GamePanel;
import com.maxrick.bounceit.objects.player.Player;
import com.maxrick.bounceit.objects.player.PlayerStatus;
import com.maxrick.bounceit.objects.player.PlayerStatusFreeFalling;
import com.maxrick.bounceit.tutorial.FingerTouchingEvent;
import com.maxrick.bounceit.tutorial.StopPlayerStatusInAir;
import com.maxrick.bounceit.tutorial.StopPlayerTouchingTrampolinEvent;
import com.maxrick.bounceit.tutorial.TutorialGamePanel;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayerStatusFreeFalling extends PlayerStatusFreeFalling {
    private boolean fingerTouching = false;
    private double percentagePassedBeforeStop=0;

    public TutorialPlayerStatusFreeFalling(double maxHeight, PlayerObject playerObject) {
        super(maxHeight, playerObject);
        EventBus.getDefault().register(this);//@// TODO: 4/19/2016 not todo
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > fallPeriod){
            EventBus.getDefault().unregister(this);
            if(!fingerTouching){
                TutorialPlayer.getTutorialPlayer().pause();
                EventBus.getDefault().post(new StopPlayerTouchingTrampolinEvent(Constants.HOLD_DOWN_FULL));
            }
            return new TutorialPlayerStatusSpringFalling(oscPeriod, fallPeriod, playerObject);
        }
        return this;
    }

    public void onEvent(FingerTouchingEvent event){
        fingerTouching = true;
        continueIfPaused();
    }

    private void continueIfPaused() {
        if(percentagePassedBeforeStop > 0){
            continueGame();
        }
    }

    public void onEvent(FingerReleasedEvent event){
        fingerTouching = false;
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
