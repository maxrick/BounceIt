package com.max.jumpingapp.tutorial.tutorialPlayer;

import com.max.jumpingapp.objects.visuals.PlayerObject;
import com.max.jumpingapp.util.Constants;
import com.max.jumpingapp.game.FingerReleasedEvent;
import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.player.PlayerPower;
import com.max.jumpingapp.objects.player.PlayerStatus;
import com.max.jumpingapp.objects.player.PlayerStatusSpringFalling;
import com.max.jumpingapp.tutorial.FingerTouchingEvent;
import com.max.jumpingapp.tutorial.StopPlayerTouchingTrampolinEvent;
import com.max.jumpingapp.tutorial.TutorialGamePanel;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayerStatusSpringFalling extends PlayerStatusSpringFalling {
    private double percentagePassedBeforeStop=0;

    public TutorialPlayerStatusSpringFalling(double oscPeriod, double fallPeriod, PlayerObject playerObject) {
        super(oscPeriod, fallPeriod, playerObject);
        EventBus.getDefault().register(this);//@// TODO: 4/19/2016 not todo
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > oscPeriod){
            EventBus.getDefault().unregister(this);
            return new TutorialPlayerStatusSpringRising(oscPeriod, fallPeriod, playerObject);
        }
        return this;
    }

    public void onEvent(FingerTouchingEvent event){
        long minusFactor = (long) (percentagePassedBeforeStop*oscPeriod);
        GamePanel.lastUpdateTime = System.nanoTime() - minusFactor;// - 0);
        TutorialPlayer.getTutorialPlayer().unPause(oscPeriod);
    }

    public void onEvent(FingerReleasedEvent event){
        double elapsedNanos = System.nanoTime() - GamePanel.lastUpdateTime;
        percentagePassedBeforeStop = elapsedNanos/oscPeriod;
        TutorialPlayer.getTutorialPlayer().pause();
        EventBus.getDefault().post(new StopPlayerTouchingTrampolinEvent(Constants.HOLD_DOWN_FULL));
    }

    public void onFingerReleased(PlayerPower playerPower, int maxHeight) {
        if(!TutorialGamePanel.gamePaused){
            playerPower.setAccelerator(maxHeight, oscPeriod);
        }
    }
}
