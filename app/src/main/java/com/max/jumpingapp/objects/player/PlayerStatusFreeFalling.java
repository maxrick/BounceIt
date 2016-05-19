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
public class PlayerStatusFreeFalling extends PlayerStatus {

    public PlayerStatusFreeFalling(double oscPeriod, double fallPeriod) {
        super(oscPeriod, fallPeriod);
    }
    public PlayerStatusFreeFalling(double maxHeight){
        super(maxHeight);
    }

    @Override
    public Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException {
        double elapsedSeconds = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        xPosition.move();
        testDieSet = false;
        System.out.println("elapsed free falling: "+elapsedSeconds);
        return new Height((int) (-0.5 * PlayerStatus.gravitaion * Math.pow(elapsedSeconds, 2) + maxHeight));
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > fallPeriod){
            return new PlayerStatusSpringFalling(oscPeriod, fallPeriod);
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
    public void animate(PlayerObject playerObject, boolean touching) {
        playerObject.animate(touching);
    }


}
