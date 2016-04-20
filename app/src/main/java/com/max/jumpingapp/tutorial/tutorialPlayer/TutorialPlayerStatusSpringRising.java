package com.max.jumpingapp.tutorial.tutorialPlayer;

import com.max.jumpingapp.game.FingerReleasedEvent;
import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.game.LeftTrampolinEvent;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.player.PlayerStatus;
import com.max.jumpingapp.objects.player.PlayerStatusSpringRising;
import com.max.jumpingapp.tutorial.FingerTouchingEvent;
import com.max.jumpingapp.tutorial.StopPlayerTouchingTrampolinEvent;
import com.max.jumpingapp.tutorial.TutorialGamePanel;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayerStatusSpringRising extends PlayerStatusSpringRising {
    public static final double STOP_BEFORE_TIME = 0.1d;
    private double percentagePassedBeforeStop=0;

    public TutorialPlayerStatusSpringRising(double oscPeriod, double fallPeriod) {
        super(oscPeriod, fallPeriod);
        EventBus.getDefault().register(this);//@// TODO: 4/19/2016 not todo
        TutorialGamePanel.eventPleaseReleasePosted = false;
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > oscPeriod){
            EventBus.getDefault().post(new LeftTrampolinEvent(player));
            EventBus.getDefault().unregister(this);
            return new TutorialPlayerStatusFreeRising(oscPeriod, fallPeriod);
        }else if(oscPeriod - elapsed < STOP_BEFORE_TIME && !TutorialGamePanel.eventPleaseReleasePosted){
            TutorialGamePanel.eventPleaseReleasePosted = true;
            stopGame();
        }
        return this;
    }

    public void onEvent(FingerTouchingEvent event){
        continueGame();
    }

    private void continueGame() {
        long minusFaktor = (long) (percentagePassedBeforeStop*oscPeriod);
        lastUpdateTime = System.nanoTime() - minusFaktor;
//        lastUpdateTime = (long) (System.nanoTime() - oscPeriod* GamePanel.secondInNanos + STOP_BEFORE_TIME*GamePanel.secondInNanos);
    }

    public void onEvent(FingerReleasedEvent event) {
        if (!TutorialGamePanel.eventPleaseReleasePosted) {
            stopGame();
        }else {
            continueGame();
        }
    }

    private void stopGame() {
        double elapsedNanos = System.nanoTime() - lastUpdateTime;
        percentagePassedBeforeStop = elapsedNanos / oscPeriod;
        EventBus.getDefault().post(new StopPlayerTouchingTrampolinEvent());
    }
}