package com.max.jumpingapp.objects.player;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.game.ResetPowerDisplayEvent;
import com.max.jumpingapp.objects.visuals.PlayerObject;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.game.PlayerDiedException;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.types.XPosition;

import de.greenrobot.event.EventBus;

/**
 * Created by normal on 25.10.2015.
 */
public class PlayerStatusFreeRising extends PlayerStatus {
    public PlayerStatusFreeRising(double oscPeriod, double fallPeriod, PlayerObject playerObject) {
        super(oscPeriod, fallPeriod, playerObject);
    }

    @Override
    public Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException {
        double elapsedSeconds = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        testDieSet=false;
        xPosition.move();
        Height curHeight = new Height((int) (-0.5 * PlayerStatus.gravitaion * Math.pow((elapsedSeconds - Math.sqrt(2 * maxHeight / PlayerStatus.gravitaion)), 2) + maxHeight));
        playerObject.setRect(curHeight, xPosition);
        return curHeight;
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > fallPeriod){
            return new PlayerStatusFreeFalling(oscPeriod, fallPeriod, playerObject);
        }
        return this;
    }

    @Override
    public void updatePower(PlayerPower playerPower, boolean fingerTouching) {
        if(fingerTouching){
            playerPower.decreasePower(oscPeriod);
        }
    }

    @Override
    public void onFingerReleased(PlayerPower playerPower, int maxHeight) {
        playerPower.resetPower();
        EventBus.getDefault().post(new ResetPowerDisplayEvent());
    }

    @Override
    public void animate(boolean touching) {
        playerObject.animate(false);// no new animation
    }


}
