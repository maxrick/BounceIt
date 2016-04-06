package com.max.jumpingapp;

import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.visuals.PlayerObject;

/**
 * Created by normal on 29.10.2015.
 */
public class PlayerDiedException extends Throwable {
    public Player player;
    public double height;
    public  PlayerDiedException(Player player, double height){
        this.player = player;
        this.height = height;
    }
}
