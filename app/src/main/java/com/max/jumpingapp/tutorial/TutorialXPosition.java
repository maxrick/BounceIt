package com.max.jumpingapp.tutorial;

import com.max.jumpingapp.game.FingerReleasedEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 5/19/2016.
 */
public class TutorialXPosition extends com.max.jumpingapp.types.XPosition {
    public TutorialXPosition(){
        super();
        EventBus.getDefault().register(this);//@// TODO: 5/19/2016 bad
    }

    public void onEvent(FingerReleasedEvent event){
        if(TutorialGamePanel.gamePaused){
//            this.lastUpdateTime = System.nanoTime();
        }
    }

}
