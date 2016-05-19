package com.max.jumpingapp.tutorial;

import com.max.jumpingapp.types.XPosition;

/**
 * Created by max on 5/19/2016.
 */
public class StopPlayerStatusInAir {
    private final XPosition xPosition;

    public StopPlayerStatusInAir(XPosition xPosition) {
        this.xPosition = xPosition;
    }

    public XPosition getXPosition(){
        return xPosition;
    }
}
