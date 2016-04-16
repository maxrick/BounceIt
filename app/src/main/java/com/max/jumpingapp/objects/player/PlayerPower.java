package com.max.jumpingapp.objects.player;

import com.max.jumpingapp.game.JumpMissedEvent;
import com.max.jumpingapp.game.LeftTrampolinEvent;
import com.max.jumpingapp.game.LivePowerEvent;
import com.max.jumpingapp.game.MinPowerEvent;
import com.max.jumpingapp.types.PowerPercent;

import de.greenrobot.event.EventBus;

/**
 * Created by normal on 24.10.2015.
 */
public class PlayerPower {
    private PowerPercent powerPercent;
    private int minAccelerator = 10;
    private long increasePowerMikro;
    private long decreasePowerMikro;
    private double maxPower;
    private int accelerator;

    public PlayerPower() {
        increasePowerMikro = 0;
        decreasePowerMikro = 0;
        this.powerPercent = new PowerPercent();
        this.accelerator = 0;
    }

    public void onEvent(LeftTrampolinEvent event) {
        System.out.println("playerpower left trampolin");
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
        double minPower = minPowerFor(maxHeight);
        maxPowerFor(maxHeight);

        EventBus.getDefault().post(new MinPowerEvent(minPower));

        accelerator = (int) ((powerPercent.value() - minPower) / (100 - minPower) * maxPower);
        resetPower();
    }

    public void activateAccelaration(Player player) {//on left trampolin
        if (accelerator != 0) {
            System.out.println("accelerator: "+ accelerator);
            player.activateAccelaration(accelerator, maxPower);
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
            System.out.println("increasing: " + increasePowerMikro);
        }
        EventBus.getDefault().post(new LivePowerEvent((calculateElapsedTime()/(oscPeriod*2000000))*100));
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
}

