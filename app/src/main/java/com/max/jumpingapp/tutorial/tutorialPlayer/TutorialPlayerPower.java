package com.max.jumpingapp.tutorial.tutorialPlayer;

import com.max.jumpingapp.objects.player.PlayerPower;

/**
 * Created by max on 4/20/2016.
 */
public class TutorialPlayerPower extends PlayerPower {

    private long pauseBeginning = System.nanoTime();

    public void pause() {
        System.out.println("pause power");
        this.pauseBeginning = System.nanoTime();
    }

    public void unPause(double oscPeriod) {
        long elapsedMikro = (System.nanoTime() - this.pauseBeginning) / 1000;
        this.pauseBeginning = System.nanoTime();//@// TODO: 4/21/2016 temporary, event is caught to often
        if (this.increasePowerMikro != 0) {
            this.increasePowerMikro += elapsedMikro;
            System.out.println("power unpaused");
        }
        if(this.decreasePowerMikro != 0){
            this.decreasePowerMikro += elapsedMikro;
        }
    }
}
