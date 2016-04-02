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
public class PlayerStatusSpringRising extends PlayerStatus {
    public PlayerStatusSpringRising(double oscPeriod, double fallPeriod) {
        super(oscPeriod, fallPeriod);
    }

    @Override
    public int calculatePos(PlayerPower playerPower, double maxHeight, XPosition xPosition, PlayerObject playerObject, Trampolin trampolin) throws PlayerDiedException {
        double elapsedSeconds = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        xPosition.dontMove();
        int curHeight = (int) (-(Math.sqrt(2 * mass * PlayerStatus.gravitaion * maxHeight / GamePanel.SPRINGCONST)
                * Math.sin((elapsedSeconds+this.oscPeriod) / Math.sqrt(mass / GamePanel.SPRINGCONST))));
        return curHeight;
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus() {
        double elapsed = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > oscPeriod){
            return new PlayerStatusFreeRising(oscPeriod, fallPeriod);
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