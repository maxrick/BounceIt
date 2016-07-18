package com.max.jumpingapp.types;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.game.MainThread;

/**
 * Created by max on 3/29/2016.
 */
public class XPosition {
    public static final int TIME_IN_MILI = 30;
    protected final double maxVelocity;
    protected double xVelocity;
    protected int value;
    public XPosition(){
        this.xVelocity = 10;
        this.value = 0;
        maxVelocity = MainThread.FPS * 0.1* GamePanel.screenWidth;
    }

    public int adjusted(int position){
        return (position+value+GamePanel.screenWidth) %GamePanel.screenWidth;
    }

    public void move(){
        value = (int) (xVelocity/ TIME_IN_MILI);
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
        value = 0;
    }

    public void velocityByWind(int windPower) {
        xVelocity += windPower;
        capVelocity();
    }

}
