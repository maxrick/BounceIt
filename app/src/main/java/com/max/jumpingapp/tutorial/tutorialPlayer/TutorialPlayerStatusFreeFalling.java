package com.max.jumpingapp.tutorial.tutorialPlayer;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.player.PlayerStatus;
import com.max.jumpingapp.objects.player.PlayerStatusFreeFalling;
import com.max.jumpingapp.objects.player.PlayerStatusSpringFalling;
import com.max.jumpingapp.tutorial.StopPlayerTouchingTrampolinEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayerStatusFreeFalling extends PlayerStatusFreeFalling {
    public TutorialPlayerStatusFreeFalling(double maxHeight) {
        super(maxHeight);
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > fallPeriod){
            EventBus.getDefault().post(new StopPlayerTouchingTrampolinEvent());
            return new TutorialPlayerStatusSpringFalling(oscPeriod, fallPeriod);
        }
        return this;
    }
}
