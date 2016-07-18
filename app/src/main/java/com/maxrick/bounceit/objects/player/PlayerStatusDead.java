package com.maxrick.bounceit.objects.player;

import com.maxrick.bounceit.game.PlayerDiedException;
import com.maxrick.bounceit.objects.Trampolin;
import com.maxrick.bounceit.objects.visuals.PlayerObject;
import com.maxrick.bounceit.types.Height;
import com.maxrick.bounceit.types.XPosition;

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
