package com.maxrick.bounceit.tutorial;

import com.maxrick.bounceit.objects.Wind;
import com.maxrick.bounceit.types.Height;
import com.maxrick.bounceit.types.XPosition;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 5/19/2016.
 */
public class TutorialWind extends Wind {
    private static final int TUTORIAL_WIND_POWER = 120;
    protected boolean tutorialMode;

    public TutorialWind(){
        super();
        tutorialMode = true;
    }

    @Override
    public void blow(XPosition xPosition, Height curHeight) {
        if(tutorialMode && curHeight.isGreaterThan(1000)){
            tutorialMode = false;
            tutorialBlow(xPosition);
            pause();
        }
        if(!tutorialMode){
            super.blow(xPosition, curHeight);
        }
    }

    private void tutorialBlow(XPosition xPosition) {
        xPosition.velocityByWind(TUTORIAL_WIND_POWER);
    }

    private void pause() {
        EventBus.getDefault().post(new StopPlayerStatusInAir());
    }

    @Override
    public void moreWind() {
        if(!tutorialMode){
            super.moreWind();
        }
    }
}
