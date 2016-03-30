package com.max.jumpingapp;

import android.graphics.Color;

import com.max.jumpingapp.objects.Player;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.types.XPosition;

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
    public int calculatePos(PlayerObject playerObject, PlayerPower playerPower, double maxHeight, Trampolin trampolin, XPosition xPosition) {
        double elapsedSeconds = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        xPosition.move();
        playerObject.setColor(Color.YELLOW);
        testDieSet = false;
        int curHeight = (int) (-0.5 * PlayerStatus.gravitaion * Math.pow(elapsedSeconds, 2) + maxHeight);
        playerObject.setRect(curHeight, xPosition);
        return curHeight;
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus() {
        double elapsed = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
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
