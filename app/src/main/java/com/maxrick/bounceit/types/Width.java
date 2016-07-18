package com.maxrick.bounceit.types;

/**
 * Created by max on 4/11/2016.
 */
public class Width {
    private final int width;

    public Width(double width) {
        this.width = (int) width;
    }

    public int getLeftWithCenter(XCenter xCenter) {
        return xCenter.getLeftForWidth(width);
    }

    public int getValue() {
        return width;
    }

    public int getRighWithCenter(XCenter xCenter) {
        return xCenter.getRightForWidth(width);
    }
}
