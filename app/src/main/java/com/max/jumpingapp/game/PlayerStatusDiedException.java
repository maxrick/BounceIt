package com.max.jumpingapp.game;

import com.max.jumpingapp.objects.player.PlayerStatusDead;

/**
 * Created by max on 6/12/2016.
 */
public class PlayerStatusDiedException extends Throwable {
    public final PlayerStatusDead playerStatusDead;

    public PlayerStatusDiedException(PlayerStatusDead playerStatusDead) {
        this.playerStatusDead = playerStatusDead;
    }
}
