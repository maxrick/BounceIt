package com.max.jumpingapp.objects.player;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.JumpCounter;
import com.max.jumpingapp.game.PlayerDiedException;
import com.max.jumpingapp.objects.Trampolin;
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
    public Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException {
        double elapsedSeconds = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        xPosition.move();
        testDieSet = false;
        return new Height((int) (-0.5 * PlayerStatus.gravitaion * Math.pow(elapsedSeconds, 2) + maxHeight));
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
            playerPower.decreasePower(oscPeriod);
        }else {
            playerPower.resetPower();
        }
        //playerPower.activateAccelaration(player);
    }

    @Override
    public void countJump(JumpCounter jumps) {

    }

}
