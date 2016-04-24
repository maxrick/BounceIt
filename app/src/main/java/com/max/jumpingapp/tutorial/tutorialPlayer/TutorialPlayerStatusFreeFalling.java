package com.max.jumpingapp.tutorial.tutorialPlayer;

import com.max.jumpingapp.game.FingerReleasedEvent;
import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.player.PlayerStatus;
import com.max.jumpingapp.objects.player.PlayerStatusFreeFalling;
import com.max.jumpingapp.objects.player.PlayerStatusSpringFalling;
import com.max.jumpingapp.tutorial.FingerTouchingEvent;
import com.max.jumpingapp.tutorial.StopPlayerTouchingTrampolinEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayerStatusFreeFalling extends PlayerStatusFreeFalling {
    private boolean fingerTouching = false;

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
                EventBus.getDefault().post(new StopPlayerTouchingTrampolinEvent());
            }
            return new TutorialPlayerStatusSpringFalling(oscPeriod, fallPeriod);
        }
        return this;
    }

    public void onEvent(FingerTouchingEvent event){
        fingerTouching = true;
        System.out.println("finger touching free falling");
    }
    public void onEvent(FingerReleasedEvent event){
        fingerTouching = false;
    }

}
