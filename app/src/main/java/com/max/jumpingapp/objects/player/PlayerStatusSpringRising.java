package com.max.jumpingapp.objects.player;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.game.LeftTrampolinEvent;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.game.PlayerDiedException;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.types.XPosition;

import de.greenrobot.event.EventBus;

/**
 * Created by normal on 25.10.2015.
 */
public class PlayerStatusSpringRising extends PlayerStatus {
    public PlayerStatusSpringRising(double oscPeriod, double fallPeriod) {
        super(oscPeriod, fallPeriod);
    }

    @Override
    public Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException {
        double elapsedSeconds = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        xPosition.dontMove();
        return new Height((int) (-(Math.sqrt(2 * mass * PlayerStatus.gravitaion * maxHeight / GamePanel.SPRINGCONST)
                * Math.sin((elapsedSeconds+this.oscPeriod) / Math.sqrt(mass / GamePanel.SPRINGCONST)))));
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > oscPeriod){
            EventBus.getDefault().post(new LeftTrampolinEvent(player));
            System.out.println("left trampolin");
            return new PlayerStatusFreeRising(oscPeriod, fallPeriod);
        }
        return this;
    }

    @Override
    public void updatePower(PlayerPower playerPower, boolean fingerTouching, Player player, double maxHeight, Trampolin trampolin, long timeMikro) {
        if(fingerTouching){
            playerPower.increasePower(oscPeriod);
        }else {
            playerPower.accelerateOnce(maxHeight, oscPeriod);
            playerPower.resetPower();
        }
    }


}
