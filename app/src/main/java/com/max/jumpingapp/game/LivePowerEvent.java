package com.max.jumpingapp.game;

/**
 * Created by max on 4/12/2016.
 */
public class LivePowerEvent {
    private double livePower;
    public LivePowerEvent(double livePower) {
        this.livePower = livePower;
    }

    public double value() {
        return livePower;
    }
}
