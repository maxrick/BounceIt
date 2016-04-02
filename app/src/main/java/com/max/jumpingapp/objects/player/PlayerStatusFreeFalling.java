package com.max.jumpingapp.objects.player;

import com.max.jumpingapp.GamePanel;
import com.max.jumpingapp.JumpCounter;
import com.max.jumpingapp.PlayerDiedException;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.objects.visuals.PlayerObject;
import com.max.jumpingapp.types.PlayerPower;
import com.max.jumpingapp.types.XPosition;

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
    public int calculatePos(PlayerPower playerPower, double maxHeight, XPosition xPosition, PlayerObject playerObject, Trampolin trampolin) throws PlayerDiedException {
        double elapsedSeconds = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        xPosition.move();
        testDieSet = false;
        int curHeight = (int) (-0.5 * PlayerStatus.gravitaion * Math.pow(elapsedSeconds, 2) + maxHeight);
        return curHeight;
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus() {
        double elapsed = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > fallPeriod){
            return new PlayerStatusSpringFalling(oscPeriod, fallPeriod);
        }
        return this;
    }

    @Override
    public boolean isRising() {
        return (0==fallPeriod);
    }

    @Override
    public void updatePower(PlayerPower playerPower, boolean fingerTouching, Player player, double maxHeight, Trampolin trampolin, long timeMikro) {
        if(fingerTouching){
            playerPower.decreasePower();
        }else {
            playerPower.resetPower();
        }
        //playerPower.activateAccelaration(player);
    }

    @Override
    public void countJump(JumpCounter jumps) {

    }

}
