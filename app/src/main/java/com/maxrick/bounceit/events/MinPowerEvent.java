package com.maxrick.bounceit.events;

/**
 * Created by max on 4/12/2016.
 */
public class MinPowerEvent {
    private double minPower;
    public MinPowerEvent(double minPower) {
        this.minPower = minPower;
    }

    public double value() {
        return minPower;
    }
}
