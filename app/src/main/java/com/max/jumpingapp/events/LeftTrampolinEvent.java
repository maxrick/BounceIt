package com.max.jumpingapp.events;

import com.max.jumpingapp.objects.player.Player;

/**
 * Created by max on 4/11/2016.
 */
public class LeftTrampolinEvent {
    private final Player player;

    public LeftTrampolinEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer(){
        return player;
    }
}
