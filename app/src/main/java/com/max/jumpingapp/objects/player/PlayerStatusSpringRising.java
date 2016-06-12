package com.max.jumpingapp.objects.player;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.game.LeftTrampolinEvent;
import com.max.jumpingapp.objects.visuals.PlayerObject;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.game.PlayerDiedException;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.types.XPosition;

import de.greenrobot.event.EventBus;

/**
 * Created by normal on 25.10.2015.
 */
public class PlayerStatusSpringRising extends PlayerStatus {
    public PlayerStatusSpringRising(double oscPeriod, double fallPeriod, PlayerObject playerObject) {
        super(oscPeriod, fallPeriod, playerObject);
    }

    @Override
    public Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException {
        double elapsedSeconds = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        xPosition.dontMove();
        Height curHeight = new Height((int) (-(Math.sqrt(2 * mass * PlayerStatus.gravitaion * maxHeight / GamePanel.SPRINGCONST)
                * Math.sin((elapsedSeconds + this.oscPeriod) / Math.sqrt(mass / GamePanel.SPRINGCONST)))));
        playerObject.setRect(curHeight, xPosition);
        return curHeight;
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > oscPeriod){
            EventBus.getDefault().post(new LeftTrampolinEvent(player));
            return new PlayerStatusFreeRising(oscPeriod, fallPeriod, playerObject);
        }
        return this;
    }

    @Override
    public void updatePower(PlayerPower playerPower, boolean fingerTouching) {
        if(fingerTouching){
            playerPower.increasePower(oscPeriod);
        }
    }

    @Override
    public void onFingerReleased(PlayerPower playerPower, int maxHeight) {
        playerPower.setAccelerator(maxHeight, oscPeriod);
    }

    @Override
    public void animate(boolean touching) {
        playerObject.animate(touching);
    }


}
