package com.max.jumpingapp.tutorial.tutorialPlayer;

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

    public TutorialPlayerStatusSpringFalling(double oscPeriod, double fallPeriod) {
        super(oscPeriod, fallPeriod);
        EventBus.getDefault().register(this);//@// TODO: 4/19/2016 not todo
        System.out.println("status spring falling created");
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > oscPeriod){
            EventBus.getDefault().unregister(this);
            return new TutorialPlayerStatusSpringRising(oscPeriod, fallPeriod);
        }
        return this;
    }

    public void onEvent(FingerTouchingEvent event){
        long minusFaktor = (long) (percentagePassedBeforeStop*oscPeriod);
        lastUpdateTime = System.nanoTime() - minusFaktor;// - 0);
        TutorialPlayer.getTutorialPlayer().unPause(oscPeriod);
        System.out.println("spring falling time reset");
    }

    public void onEvent(FingerReleasedEvent event){
        double elapsedNanos = System.nanoTime() - lastUpdateTime;
        percentagePassedBeforeStop = elapsedNanos/oscPeriod;
        System.out.println("spring falling would have stopped thread");
        TutorialPlayer.getTutorialPlayer().pause();
        EventBus.getDefault().post(new StopPlayerTouchingTrampolinEvent());
    }

    public void onFingerReleased(PlayerPower playerPower, int maxHeight) {
        if(!TutorialGamePanel.gamePaused){
            playerPower.setAccelerator(maxHeight, oscPeriod);
        }
    }
}
