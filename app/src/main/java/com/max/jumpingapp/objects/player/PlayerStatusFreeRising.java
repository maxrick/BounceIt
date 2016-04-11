package com.max.jumpingapp.objects.player;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.game.JumpMissedException;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.game.PlayerDiedException;
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
    public Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException {
        double elapsedSeconds = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        testDieSet=false;
        xPosition.move();
        return new Height((int) (- 0.5 * PlayerStatus.gravitaion * Math.pow((elapsedSeconds - Math.sqrt(2*maxHeight/ PlayerStatus.gravitaion)), 2)+ maxHeight));
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
    public void updatePower(PlayerPower playerPower, boolean fingerTouching, Player player, double maxHeight, Trampolin trampolin, long timeMikro) throws JumpMissedException {
        if(fingerTouching){
            playerPower.decreasePower(oscPeriod);
        }else {
            playerPower.resetPower();
        }
        playerPower.activateAccelaration(player);
    }


}
