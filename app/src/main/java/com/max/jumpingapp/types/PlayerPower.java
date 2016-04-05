 package com.max.jumpingapp.types;

 import com.max.jumpingapp.objects.player.Player;

 /**
 * Created by normal on 24.10.2015.
 */
public class PlayerPower {
    private double powerPercent;  //old name: accelerator
    private int minAccelerator=10;
    private long increasePowerMikro = 0;
    private long decreasePowerMikro = 0;
    private double minPower;
    private double maxPower;
    private boolean accelerated;
    private int accelerator;

    public PlayerPower() {
        this.powerPercent = 0;
        this.accelerated = false;
        this.accelerator =0;
    }

    private void resetAccelerator() {
        decreasePowerMikro =0;
        increasePowerMikro =0;
        powerPercent=0;
        accelerated = false;
        accelerator = 0;
    }

    private void accelerate(double maxHeight, double oscPeriod) {
        powerPercent = 0;
        if(increasePowerMikro != 0) {
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
        minPower = 100 - 90 * Math.pow(2, -maxHeight / 10000);
        maxPower = (4*maxHeight)/20 + 200;

        accelerator = (int)((powerPercent-minPower)/(100-minPower)*maxPower);
    }

    public void accelerateOnce(double maxHeight, double oscPeriod){
        if(!accelerated){
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
        if(accelerator != 0){
            player.activateAccelaration(accelerator, maxPower);
            //System.out.println("accel activated: "+ accelerator);
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
            accelerated = false;
            accelerator = 0;
            System.out.println("increasing: "+increasePowerMikro);
        }
    }
}
