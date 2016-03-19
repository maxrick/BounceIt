package com.max.jumpingapp;

import android.graphics.Color;

/**
 * Created by normal on 25.10.2015.
 */
public class PlayerStatusSpringRising extends PlayerStatus {
    public PlayerStatusSpringRising(double oscPeriod, double fallPeriod, double toleranceHeight) {
        super(oscPeriod, fallPeriod, toleranceHeight);
    }

    @Override
    public void calculatePos(PlayerObject playerObject, PlayerPower playerPower, double maxHeight, Trampolin trampolin) throws PlayerDiedException {
        double elapsed = (System.nanoTime() - lastUpdateTime) / 1000000000.d;
        playerObject.setColor(Color.GRAY);
        if(testDieSet){
            playerObject.setColor(Color.RED);
        }
        int curHeight = (int) (-(Math.sqrt(2 * mass * PlayerStatus.gravitaion * maxHeight / trampolin.getSpringconst())
                * Math.sin((elapsed+this.oscPeriod) / Math.sqrt(mass / trampolin.getSpringconst()))));
        playerObject.setRect(curHeight);
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus() {
        double elapsed = (System.nanoTime() - lastUpdateTime) / PlayerStatus.secondInNanos;
        if(elapsed > oscPeriod){
            return new PlayerStatusFreeRising(oscPeriod, fallPeriod, toleranceHeight);
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
            playerPower.increasePower();
        }else {
            playerPower.accelerateOnce(maxHeight, oscPeriod);
            playerPower.resetPower();
        }
    }

    @Override
    public void countJump(JumpCounter jumps) {

    }

}
