package com.max.jumpingapp.types;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.game.MainThread;

/**
 * Created by max on 3/29/2016.
 */
public class XPosition {
    private final double maxVelocity;
    private double xVelocity;
    private int value;
    private long lastUpdateTime;
    public XPosition(){
        this.xVelocity = 10;
        this.value = 0;
        this.lastUpdateTime = System.nanoTime();
        maxVelocity = MainThread.FPS * 0.1*GamePanel.screenWidth;
    }

    public int adjusted(int position){
        return (position+value+GamePanel.screenWidth) %GamePanel.screenWidth;
    }

    public void move(){
        long newTime = System.nanoTime();
        double elapsedSeconds = (newTime - lastUpdateTime)/GamePanel.secondInNanos;
        lastUpdateTime = newTime;
        value = (int) (elapsedSeconds*xVelocity);
    }

    public void adjustVelocity(int xSwipedToRight) {
        int SWIPE_STEP = 1;
        xVelocity += xSwipedToRight/ SWIPE_STEP;
        capVelocity();
    }

    private void capVelocity() {
        if(Math.abs(xVelocity) > maxVelocity){
            xVelocity = (xVelocity>0) ? maxVelocity : -maxVelocity;
        }
    }

    public void dontMove() {
        lastUpdateTime = System.nanoTime();
        value = 0;
    }

    public void velocityByWind(int windPower) {
        xVelocity += windPower;
        capVelocity();
    }
}
