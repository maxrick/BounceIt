package com.max.jumpingapp.tutorial.tutorialPlayer;

import com.max.jumpingapp.game.Game;
import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.game.LeftTrampolinEvent;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.player.PlayerStatus;
import com.max.jumpingapp.objects.player.PlayerStatusFreeRising;
import com.max.jumpingapp.objects.player.PlayerStatusSpringRising;
import com.max.jumpingapp.tutorial.GameContinuedEvent;
import com.max.jumpingapp.tutorial.StopPlayerTouchingTrampolinEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayerStatusSpringRising extends PlayerStatusSpringRising {
    public static final double STOP_BEFORE_TIME = 0.1d;
    private boolean eventPosted = false;

    public TutorialPlayerStatusSpringRising(double oscPeriod, double fallPeriod) {
        super(oscPeriod, fallPeriod);
        EventBus.getDefault().register(this);//@// TODO: 4/19/2016 not todo
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > oscPeriod){
            EventBus.getDefault().post(new LeftTrampolinEvent(player));
            EventBus.getDefault().unregister(this);
            return new TutorialPlayerStatusFreeRising(oscPeriod, fallPeriod);
        }else if(oscPeriod - elapsed < STOP_BEFORE_TIME && !eventPosted){
            EventBus.getDefault().post(new StopPlayerTouchingTrampolinEvent());
            eventPosted = true;
        }
        return this;
    }

    public void onEvent(GameContinuedEvent event){
        lastUpdateTime = (long) (System.nanoTime() - oscPeriod* GamePanel.secondInNanos + STOP_BEFORE_TIME*GamePanel.secondInNanos);
    }
}
