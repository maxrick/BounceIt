package com.max.jumpingapp;

/**
 * Created by max on 3/29/2016.
 */
public class XAdjust {
    private int value;
    public XAdjust(int value){
        this.value = value;
    }

    public int adjusted(int position){
        return value+position;
    }
}
