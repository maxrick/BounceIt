package com.max.jumpingapp.objects.player;

import com.max.jumpingapp.GamePanel;
import com.max.jumpingapp.objects.visuals.LastHeightDisplay;
import com.max.jumpingapp.types.JumpCounter;
import com.max.jumpingapp.PlayerDiedException;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.types.PlayerPower;
import com.max.jumpingapp.types.XPosition;

/**
 * Created by normal on 25.10.2015.
 */
public class PlayerStatusFreeRising extends PlayerStatus {
    public PlayerStatusFreeRising(double oscPeriod, double fallPeriod) {
        super(oscPeriod, fallPeriod);
    }

    @Override
    public int calculatePos(PlayerPower playerPower, double maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException {
        double elapsedSeconds = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        testDieSet=false;
        xPosition.move();
        int curHeight = (int) (- 0.5 * PlayerStatus.gravitaion * Math.pow((elapsedSeconds - Math.sqrt(2*maxHeight/ PlayerStatus.gravitaion)), 2)+ maxHeight);
        return curHeight;
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus() {
        double elapsed = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > fallPeriod){
            return new PlayerStatusFreeFalling(oscPeriod, fallPeriod);
        }
        return this;
    }

    @Override
    public boolean isRising() {
        return true;
    }

    @Override
    public void updatePower(PlayerPower playerPower, boolean fingerTouching, Player player, double maxHeight, Trampolin trampolin, long timeMikro) {
        if(fingerTouching){
            playerPower.decreasePower(oscPeriod);
        }else {
            playerPower.resetPower();
        }
        playerPower.activateAccelaration(player);
    }

    @Override
    public void countJump(JumpCounter jumps) {
        jumps.addJump();
    }

}
