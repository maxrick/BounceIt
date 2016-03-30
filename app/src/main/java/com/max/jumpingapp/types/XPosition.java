package com.max.jumpingapp.types;

import com.max.jumpingapp.GamePanel;
import com.max.jumpingapp.MainThread;

/**
 * Created by max on 3/29/2016.
 */
public class XPosition {
    private static int SWIPE_STEP=1;
    private static double maxVelocity;
    private double xVelocity;
    private int value;
    private long lastUpdateTime;
    public XPosition(int xVelocity){
        this.xVelocity = 10;
        this.value = 0;
        this.lastUpdateTime = System.nanoTime();
        maxVelocity = MainThread.FPS * 0.1*GamePanel.screenWidth;
        System.out.println("max velocity x: "+maxVelocity);
    }

    public int adjusted(int position){
        return (int) (position+value+GamePanel.screenWidth) %GamePanel.screenWidth;
    }

    public void move(){
        long newTime = System.nanoTime();
        double elapsedSeconds = (newTime - lastUpdateTime)/GamePanel.secondInNanos;
        lastUpdateTime = newTime;
        value = (int) (elapsedSeconds*xVelocity);
    }

    public void adjustVelocity(int xSwipedToRight) {
        xVelocity += xSwipedToRight/SWIPE_STEP;
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
        boolean windPositive = (windPower >0);
        //xVelocity = windPower + (windPositive? xVelocity : -xVelocity);
        xVelocity += windPower;
        capVelocity();
    }
}
