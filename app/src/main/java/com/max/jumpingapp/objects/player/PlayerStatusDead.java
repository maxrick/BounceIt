package com.max.jumpingapp.objects.player;

import com.max.jumpingapp.game.PlayerDiedException;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.objects.visuals.PlayerObject;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.XPosition;

/**
 * Created by max on 6/12/2016.
 */
public class PlayerStatusDead extends PlayerStatus{
    public PlayerStatusDead(double oscPeriod, double fallPeriod, PlayerObject playerObject) {
        super(oscPeriod, fallPeriod, playerObject);
    }

    @Override
    public Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException {
        playerObject.animateDead(player);
        return new Height(0);
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        return this;
    }

    @Override
    public void updatePower(PlayerPower playerPower, boolean fingerTouching) {

    }

    @Override
    public void onFingerReleased(PlayerPower playerPower, int maxHeight) {

    }

    @Override
    public void animate(boolean touching) {

    }
}
