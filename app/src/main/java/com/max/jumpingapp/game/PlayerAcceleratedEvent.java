package com.max.jumpingapp.game;

/**
 * Created by max on 4/19/2016.
 */
public class PlayerAcceleratedEvent {
    private final int accelerator;

    public PlayerAcceleratedEvent(int accelerator) {
        this.accelerator = accelerator;
    }

    public int getAccelerator(){
        return accelerator;
    }
}
