package com.max.jumpingapp;

import com.max.jumpingapp.objects.visuals.PlayerObject;

/**
 * Created by normal on 29.10.2015.
 */
public class PlayerDiedException extends Throwable {
    public PlayerObject playerObject;
    public double height;
    public  PlayerDiedException(PlayerObject playerObject, double height){
        this.playerObject = playerObject;
        this.height = height;
    }
}
