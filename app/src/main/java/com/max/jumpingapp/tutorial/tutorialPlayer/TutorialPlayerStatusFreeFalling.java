package com.max.jumpingapp.tutorial.tutorialPlayer;

import com.max.jumpingapp.R;
import com.max.jumpingapp.game.FingerReleasedEvent;
import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.player.PlayerStatus;
import com.max.jumpingapp.objects.player.PlayerStatusFreeFalling;
import com.max.jumpingapp.objects.player.PlayerStatusSpringFalling;
import com.max.jumpingapp.tutorial.FingerTouchingEvent;
import com.max.jumpingapp.tutorial.StopPlayerStatusInAir;
import com.max.jumpingapp.tutorial.StopPlayerTouchingTrampolinEvent;
import com.max.jumpingapp.types.XPosition;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayerStatusFreeFalling extends PlayerStatusFreeFalling {
    private boolean fingerTouching = false;
    private double percentagePassedBeforeStop=0;
    private XPosition tempXPosition;

    public TutorialPlayerStatusFreeFalling(double maxHeight) {
        super(maxHeight);
        EventBus.getDefault().register(this);//@// TODO: 4/19/2016 not todo
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > fallPeriod){
            EventBus.getDefault().unregister(this);
            if(!fingerTouching){
                System.out.println("pause after free falling");
                TutorialPlayer.getTutorialPlayer().pause();
                EventBus.getDefault().post(new StopPlayerTouchingTrampolinEvent(StopPlayerTouchingTrampolinEvent.HOLD_DOWN_FULL));
            }
            return new TutorialPlayerStatusSpringFalling(oscPeriod, fallPeriod);
        }
        return this;
    }

    public void onEvent(FingerTouchingEvent event){
        fingerTouching = true;
        System.out.println("finger touching free falling");
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
        stopGame(StopPlayerTouchingTrampolinEvent.WIND, event.getXPosition());
    }
    private void continueGame() {
        long minusFaktor = (long) (percentagePassedBeforeStop*oscPeriod);
        lastUpdateTime = System.nanoTime() - minusFaktor;
        TutorialPlayer.getTutorialPlayer().unPause(oscPeriod);
//        lastUpdateTime = (long) (System.nanoTime() - oscPeriod* GamePanel.secondInNanos + STOP_BEFORE_TIME*GamePanel.secondInNanos);
    }

    private void stopGame(String message, XPosition xPosition) {
        this.tempXPosition = xPosition;
        double elapsedNanos = System.nanoTime() - lastUpdateTime;
        percentagePassedBeforeStop = elapsedNanos / oscPeriod;
        TutorialPlayer.getTutorialPlayer().pause();
        EventBus.getDefault().post(new StopPlayerTouchingTrampolinEvent(message));
    }

}
