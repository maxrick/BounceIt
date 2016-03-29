package com.max.jumpingapp.types;

import com.max.jumpingapp.GamePanel;

/**
 * Created by max on 3/29/2016.
 */
public class XPosition {
    private static int SWIPE_STEP=100;
    private double xVelocity;
    private int value;
    public XPosition(int xVelocity){
        this.xVelocity = 10;
        this.value = 0;
    }

    public int adjusted(int position){
        return (int) (position+value) %GamePanel.screenWidth;
    }

    public void move(double elapsedSeconds){
        value = (int) (elapsedSeconds*xVelocity);
    }

    public void adjustVelocity(int xSwipedToRight) {
        xVelocity += xSwipedToRight/SWIPE_STEP;
    }
}
