package com.max.jumpingapp;

import android.graphics.Color;

/**
 * Created by normal on 25.10.2015.
 */
public class PlayerStatusFreeFalling extends PlayerStatus {

    public PlayerStatusFreeFalling(double oscPeriod, double fallPeriod, double toleranceHeight) {
        super(oscPeriod, fallPeriod, toleranceHeight);
    }
    public PlayerStatusFreeFalling(double maxHeight, Trampolin trampolin){
        super(maxHeight, trampolin);
    }

    @Override
    public void calculatePos(PlayerObject playerObject, PlayerPower playerPower, double maxHeight, Trampolin trampolin) {
        double elapsed = (System.nanoTime() - lastUpdateTime) / 1000000000.d;
        playerObject.setColor(Color.YELLOW);
        testDieSet = false;
        int curHeight = (int) (-0.5 * PlayerStatus.gravitaion * Math.pow(elapsed, 2) + maxHeight);
        playerObject.setRect(curHeight);
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus() {
        double elapsed = (System.nanoTime() - lastUpdateTime) / PlayerStatus.secondInNanos;
        if(elapsed > fallPeriod){
            return new PlayerStatusSpringFalling(oscPeriod, fallPeriod, toleranceHeight);
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
