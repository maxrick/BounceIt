package com.max.jumpingapp.game;

import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.player.PlayerStatus;
import com.max.jumpingapp.objects.player.PlayerStatusDead;
import com.max.jumpingapp.types.Score;

/**
 * Created by normal on 29.10.2015.
 */
public class PlayerDiedException extends Throwable {
    public Player player;
    public Score score;

    public  PlayerDiedException(Player player, Score score){
        this.player = player;
        this.score = score;
    }

    public PlayerDiedException(Player player) {
        this.player = player;
    }

}
