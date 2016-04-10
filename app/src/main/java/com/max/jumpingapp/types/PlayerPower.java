package com.max.jumpingapp.types;

import android.graphics.Canvas;

import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.player.PlayerStatus;
import com.max.jumpingapp.objects.visuals.PowerDisplay;

/**
 * Created by normal on 24.10.2015.
 */
public class PlayerPower {
    private double powerPercent;  //old name: accelerator
    private int minAccelerator = 10;
    private long increasePowerMikro;
    private long decreasePowerMikro;
    private double minPower;
    private double maxPower;
    private boolean accelerated;
    private int accelerator;
    private PowerDisplay powerDisplay;

    public PlayerPower() {
        increasePowerMikro = 0;
        decreasePowerMikro = 0;
        this.powerPercent = 0;
        this.accelerated = false;
        this.accelerator = 0;
        this.powerDisplay = new PowerDisplay();
    }

    private void resetAccelerator() {
        decreasePowerMikro = 0;
        increasePowerMikro = 0;
        powerPercent = 0;
        accelerated = false;
        accelerator = 0;
    }

    private void accelerate(double maxHeight, double oscPeriod) {
        powerPercent = 0;
        if (increasePowerMikro != 0) {
            long timeMikro = System.nanoTime() / 1000;
            long elapsed = timeMikro - increasePowerMikro;
            if (decreasePowerMikro != 0) {
                elapsed -= (increasePowerMikro - decreasePowerMikro);
            }
            if (elapsed > 0) {
                powerPercent = (elapsed / (oscPeriod * 2000000)) * 100;
            }
            if (powerPercent > 100) {
                powerPercent = 100;
            }
        }
        minPower = 100 - 90 * Math.pow(2, -maxHeight / 50000);
        maxPower = (4 * maxHeight) / 20 + 200;

        powerDisplay.setMinPower(minPower);

        accelerator = (int) ((powerPercent - minPower) / (100 - minPower) * maxPower);
    }

    public void draw(Canvas canvas) {
        powerDisplay.draw(canvas);
    }

    public void accelerateOnce(double maxHeight, double oscPeriod) {
        if (!accelerated) {
            accelerate(maxHeight, oscPeriod);
            //System.out.println("accelerated: "+ accelerator);
            accelerated = true;
        }
    }

    //@TODO: 18.03.2016 is overridden in accelerate()
    public void decelerate(double decelerateBy) {
        // powerPercent -= Math.abs(decelerateBy);
    }

    public boolean noPower() {
        return (this.powerPercent <= 0);
    }

    public void activateAccelaration(Player player) {
        if (accelerator != 0) {
            player.activateAccelaration(accelerator, maxPower);
            //System.out.println("accel activated: "+ accelerator);
            resetAccelerator();
        }else if(increasePowerMikro !=0){
            resetPower();
            player.missedJump();
        }else {
        }
    }

    public void activateAccelarationNoCheck(Player player){
        player.activateAccelaration(accelerator, maxPower);
        resetAccelerator();
    }

    public void decreasePower(double oscPeriod) {
        if (decreasePowerMikro == 0) {
//            increasePowerMikro = 0;
            decreasePowerMikro = System.nanoTime() / 1000;
        }
        livePowerDisplay(oscPeriod);
    }

    public void resetPower() {
        increasePowerMikro = 0;
        decreasePowerMikro = 0;
    }

    public void increasePower(double oscPeriod) {
        if (increasePowerMikro == 0) {
            increasePowerMikro = System.nanoTime() / 1000;
            accelerated = false;
            accelerator = 0;
            System.out.println("increasing: " + increasePowerMikro);
        }
        livePowerDisplay(oscPeriod);
    }

    public void livePowerDisplay(double oscPeriod) {
        long timeMikro = System.nanoTime() / 1000;
        double livePowerPercent;
        long liveIncreasePowerMikro = increasePowerMikro;
        if (liveIncreasePowerMikro == 0){
            liveIncreasePowerMikro = timeMikro;
        }
        long elapsed = timeMikro - liveIncreasePowerMikro;
        if (decreasePowerMikro != 0) {
            elapsed -= (liveIncreasePowerMikro - decreasePowerMikro);
        }
        if (decreasePowerMikro == 0 && liveIncreasePowerMikro == 0) {
            elapsed = 0;
        }
        livePowerPercent = (elapsed / (oscPeriod * 2000000)) * 100;
        if (powerPercent > 100) {
            powerPercent = 100;
        }
        powerDisplay.setPowerPercentage(livePowerPercent);
    }
}

