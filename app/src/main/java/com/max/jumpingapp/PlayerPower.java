package com.max.jumpingapp;

/**
 * Created by normal on 24.10.2015.
 */
public class PlayerPower {
    private double powerPercent;  //old name: accelerator
    private int minAccelerator=10;
    private long increasePowerMikro = 0;
    private long decreasePowerMikro = 0;
    private int maxPower = 2000;
    private double minPower;

    public PlayerPower() {
        this.powerPercent = 0;
    }

    private void resetAccelerator() {
        decreasePowerMikro =0;
        increasePowerMikro =0;
        powerPercent=0;

    }

    public void accelerate(double maxHeight, double oscPeriod) {
        if(increasePowerMikro != 0) {
            long timeMikro = System.nanoTime() / 1000;
            long elapsed = timeMikro - increasePowerMikro;
            if (decreasePowerMikro != 0) {
                elapsed -= (increasePowerMikro - decreasePowerMikro);
            }
            powerPercent = 0;
            if (elapsed > 0) {
                powerPercent = (elapsed / (oscPeriod * 2000000))*100;
            }
            minPower = 100 - 90 * Math.pow(2, -maxHeight / 10000);
        }
    }

    //@TODO: 18.03.2016 is overridden in accelerate()
    public void decelerate(double decelerateBy) {
        powerPercent -= Math.abs(decelerateBy);
    }

    public boolean noPower() {
        return (this.powerPercent <= 0);
    }

    public void activateAccelaration(Player player) {
        if(0 != powerPercent){

            int accelerator = (int)((powerPercent-minPower)/(100-minPower)*maxPower);
            player.activateAccelaration(accelerator);
            resetAccelerator();
        }
    }

    public void decreasePower() {
        if(decreasePowerMikro ==0){
            decreasePowerMikro = System.nanoTime()/1000;
        }
    }

    public void resetPower() {
        increasePowerMikro = 0;
        decreasePowerMikro = 0;
    }

    public void increasePower() {
        if(increasePowerMikro == 0){
            increasePowerMikro = System.nanoTime()/1000;
        }
    }
}
