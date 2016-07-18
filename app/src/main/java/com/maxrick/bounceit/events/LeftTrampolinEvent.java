package com.maxrick.bounceit.events;

import com.maxrick.bounceit.objects.player.Player;

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
