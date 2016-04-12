package com.max.jumpingapp.objects.player;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.game.JumpMissedException;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.game.PlayerDiedException;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.types.XPosition;

/**
 * Created by normal on 02.09.2015.
 */
public abstract class PlayerStatus {
    //physics
    public static double gravitaion = 1000;//10m/s -> 1000px/s
    protected boolean testDieSet=false;
    protected double mass = 1;

    protected double oscPeriod;
    protected double fallPeriod;
    protected long lastUpdateTime = System.nanoTime();

    public PlayerStatus(double maxHeight) {
//        this.playerPower = new PlayerPower();
        oscPeriod = 0.5* Math.PI * Math.sqrt(mass / GamePanel.SPRINGCONST);
        fallPeriod = Math.sqrt((2 * maxHeight) / PlayerStatus.gravitaion);
    }

    protected PlayerStatus(double oscPeriod, double fallperiod){
        this.lastUpdateTime = System.nanoTime();
        this.oscPeriod = oscPeriod;
        this.fallPeriod = fallperiod;
    }

    public abstract Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException;

    public abstract PlayerStatus getCurrentPlayerStatus(Player player);

    public void updateFallperiod(double maxHeight){
        fallPeriod = Math.sqrt( (2 * maxHeight) / PlayerStatus.gravitaion );
    }


    public abstract void updatePower(PlayerPower playerPower, boolean fingerTouching, Player player, double maxHeight, Trampolin trampolin, long timeMikro) throws JumpMissedException;//@// TODO: 4/11/2016 does this belong here?

    public void accelerate(double maxHeight, PlayerPower playerPower) {
        playerPower.setAccelerator(maxHeight, oscPeriod);
    }

    public abstract void onFingerReleased(PlayerPower playerPower, int maxHeight);
}
