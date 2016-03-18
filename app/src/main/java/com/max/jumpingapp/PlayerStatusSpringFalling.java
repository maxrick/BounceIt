package com.max.jumpingapp;

import android.graphics.Color;

/**
 * Created by normal on 25.10.2015.
 */
public class PlayerStatusSpringFalling extends PlayerStatus {

    public PlayerStatusSpringFalling(double oscPeriod, double fallPeriod, double toleranceHeight) {
        super(oscPeriod, fallPeriod, toleranceHeight);
    }

    @Override
    public void calculatePos(PlayerObject playerObject, PlayerPower playerPower, double maxHeight, Trampolin trampolin) throws PlayerDiedException {
        double elapsed = (System.nanoTime() - lastUpdateTime) / PlayerStatus.secondInNanos;
        playerObject.setColor(Color.BLACK);
        if(testDieSet){
            playerObject.setColor(Color.RED);
        }
        int curHeight = (int) (-(Math.sqrt(2 * mass * PlayerStatus.gravitaion * maxHeight / trampolin.getSpringconst()) *
                Math.sin(elapsed / Math.sqrt(mass / trampolin.getSpringconst()))));

//        if(curHeight < -200 && playerPower.noPower()){
        if(elapsed > oscPeriod*0.45 && playerPower.noPower() && curHeight < toleranceHeight){
            if(!testDieSet){
                testDieSet = true;
                throw new PlayerDiedException();
            }
        }
        playerObject.setRect(curHeight);
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus() {
        double elapsed = (System.nanoTime() - lastUpdateTime) / PlayerStatus.secondInNanos;
        if(elapsed > oscPeriod){
            return new PlayerStatusSpringRising(oscPeriod, fallPeriod, toleranceHeight);
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
            playerPower.increasePower();
        }else {
            playerPower.accelerate(maxHeight,oscPeriod);
            playerPower.resetPower();
        }
    }

    @Override
    public void countJump(JumpCounter jumps) {
        jumps.notYetCounted();
    }

}
