package com.max.jumpingapp;

import android.graphics.Color;

import com.max.jumpingapp.objects.Player;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.types.XPosition;

/**
 * Created by normal on 25.10.2015.
 */
public class PlayerStatusFreeRising extends PlayerStatus {
    public PlayerStatusFreeRising(double oscPeriod, double fallPeriod, double toleranceHeight) {
        super(oscPeriod, fallPeriod, toleranceHeight);
    }

    @Override
    public int calculatePos(PlayerObject playerObject, PlayerPower playerPower, double maxHeight, Trampolin trampolin, XPosition xPosition) throws PlayerDiedException {
        double elapsedSeconds = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        testDieSet=false;
        xPosition.move();
        playerObject.setColor(Color.GREEN);
        int curHeight = (int) (- 0.5 * PlayerStatus.gravitaion * Math.pow((elapsedSeconds - Math.sqrt(2*maxHeight/ PlayerStatus.gravitaion)), 2)+ maxHeight);
        playerObject.setRect(curHeight, xPosition);
        return curHeight;
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus() {
        double elapsed = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > fallPeriod){
            return new PlayerStatusFreeFalling(oscPeriod, fallPeriod, toleranceHeight);
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
            playerPower.decreasePower();
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
