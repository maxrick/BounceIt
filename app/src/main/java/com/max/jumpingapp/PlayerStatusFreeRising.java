package com.max.jumpingapp;

import android.graphics.Color;

/**
 * Created by normal on 25.10.2015.
 */
public class PlayerStatusFreeRising extends PlayerStatus {
    public PlayerStatusFreeRising(double oscPeriod, double fallPeriod, double toleranceHeight) {
        super(oscPeriod, fallPeriod, toleranceHeight);
    }

    @Override
    public void calculatePos(PlayerObject playerObject, PlayerPower playerPower, double maxHeight, Trampolin trampolin) throws PlayerDiedException {
        double elapsed = (System.nanoTime() - lastUpdateTime) / PlayerStatus.secondInNanos;
        testDieSet=false;
        playerObject.setColor(Color.GREEN);
        int curHeight = (int) (- 0.5 * PlayerStatus.gravitaion * Math.pow((elapsed - Math.sqrt(2*maxHeight/ PlayerStatus.gravitaion)), 2)+ maxHeight);
        playerObject.setRect(curHeight);
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus() {
        double elapsed = (System.nanoTime() - lastUpdateTime) / PlayerStatus.secondInNanos;
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
