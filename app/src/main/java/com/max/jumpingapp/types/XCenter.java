package com.max.jumpingapp.types;

/**
 * Created by max on 4/11/2016.
 */
public class XCenter {
    private final int xCenter;

    public XCenter(int i) {
        this.xCenter = i;
    }

    public int getLeftForWidth(int width) {
        return xCenter-width/2;
    }

    public int getRightForWidth(int width) {
        return xCenter+width/2;
    }
}
