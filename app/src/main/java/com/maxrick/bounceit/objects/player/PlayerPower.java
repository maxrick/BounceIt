package com.maxrick.bounceit.objects.player;

import com.maxrick.bounceit.events.JumpMissedEvent;
import com.maxrick.bounceit.events.LeftTrampolinEvent;
import com.maxrick.bounceit.events.LivePowerEvent;
import com.maxrick.bounceit.types.PowerPercent;

import de.greenrobot.event.EventBus;

/**
 * Created by normal on 24.10.2015.
 */
public class PlayerPower {
    private PowerPercent powerPercent;
    protected long increasePowerMikro;//@// TODO: 4/21/2016 change to nano
    protected long decreasePowerMikro;
    private double maxPower;
    private int accelerator;

    public PlayerPower() {
        increasePowerMikro = 0;
        decreasePowerMikro = 0;
        this.powerPercent = new PowerPercent();
        this.accelerator = 0;
    }

    public void onEvent(LeftTrampolinEvent event) {
        activateAccelaration(event.getPlayer());
    }

    private void resetAccelerator() {
        powerPercent = new PowerPercent();
        accelerator = 0;
    }

    public void resetPower() {
        increasePowerMikro = 0;
        decreasePowerMikro = 0;
    }

    public void setAccelerator(double maxHeight, double oscPeriod) {//on finger released, on jump missed
        this.powerPercent = calculateUncappedPowerPercent(oscPeriod).cap();
        double minPower = minPowerFor(maxHeight);//only necessary for when jump missed;
        maxPowerFor(maxHeight);
        if(powerPercent.value() > 0){
            minPower = 0;
        }

        accelerator = (int) ((powerPercent.value() - minPower) / (100 - minPower) * maxPower);
        resetPower();
    }

    public void activateAccelaration(Player player) {//on left trampolin
        if (accelerator != 0) {
            player.activateAccelaration(accelerator);
            resetAccelerator();
        } else if (increasePowerMikro != 0) {
            resetPower();
            EventBus.getDefault().post(new JumpMissedEvent());
        }
    }

    public void decreasePower(double oscPeriod) {
        if (decreasePowerMikro == 0) {
            decreasePowerMikro = System.nanoTime() / 1000;
        }
        EventBus.getDefault().post(new LivePowerEvent((calculateElapsedTime()/(oscPeriod*2000000))*100));
    }

    public void increasePower(double oscPeriod) {
        if (increasePowerMikro == 0) {
            increasePowerMikro = System.nanoTime() / 1000;
        }
        long elapsed = calculateElapsedTime();
        EventBus.getDefault().post(new LivePowerEvent((elapsed/(oscPeriod*2000000))*100));
    }

    private void maxPowerFor(double maxHeight) {
        maxPower = (4 * maxHeight) / 20 + 200;
    }

    private double minPowerFor(double maxHeight) {
        return 100 - 90 * Math.pow(2, -maxHeight / 50000);
    }

    private PowerPercent calculateUncappedPowerPercent(double oscPeriod) {
        long elapsed = calculateElapsedTime();
        return new PowerPercent((elapsed/(oscPeriod*2000000))*100);
    }

    private long calculateElapsedTime() {
        long timeMikro = System.nanoTime() / 1000;
        long liveIncreaseMikro = calculateLiveIncreaseMikro(timeMikro);
        long liveDecreaseMikro = calculateLiveDecreaseMikro(liveIncreaseMikro);
        return timeMikro - 2 * liveIncreaseMikro + liveDecreaseMikro;
    }

    private long calculateLiveDecreaseMikro(long liveIncreaseMikro) {
        if (decreasePowerMikro == 0) {
            return liveIncreaseMikro;
        }
        return decreasePowerMikro;
    }

    private long calculateLiveIncreaseMikro(long timeMikro) {
        if (increasePowerMikro == 0) {
            return timeMikro;
        }
        return increasePowerMikro;
    }

    public double accelerationPercentage() {
        return accelerator/maxPower*100;
    }

    public void unPause(double oscPeriod) {
        //@// TODO: 4/21/2016 needed for tutorial
    }

    public void pause() {
        //@// TODO: 4/21/2016 needed for tutorial
    }
}

