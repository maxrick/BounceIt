package com.max.jumpingapp.tutorial.tutorialPlayer;

import com.max.jumpingapp.util.Constants;
import com.max.jumpingapp.game.FingerReleasedEvent;
import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.player.PlayerStatus;
import com.max.jumpingapp.objects.player.PlayerStatusFreeFalling;
import com.max.jumpingapp.tutorial.FingerTouchingEvent;
import com.max.jumpingapp.tutorial.StopPlayerStatusInAir;
import com.max.jumpingapp.tutorial.StopPlayerTouchingTrampolinEvent;
import com.max.jumpingapp.tutorial.TutorialGamePanel;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayerStatusFreeFalling extends PlayerStatusFreeFalling {
    private boolean fingerTouching = false;
    private double percentagePassedBeforeStop=0;

    public TutorialPlayerStatusFreeFalling(double maxHeight) {
        super(maxHeight);
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
            return new TutorialPlayerStatusSpringFalling(oscPeriod, fallPeriod);
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
