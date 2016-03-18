package com.max.jumpingapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by normal on 02.09.2015.
 */
public abstract class PlayerStatus {
    //physics
    private static final int MINSPEED = 3;
    public static double gravitaion = 1000;//10m/s -> 1000px/s
    public static double secondInNanos= 1000000000.d;
    protected boolean testDieSet=false;
    protected double mass = 1;

    protected double oscPeriod;
    protected double fallPeriod;
    protected long lastUpdateTime = System.nanoTime();
    protected double toleranceHeight;

    public PlayerStatus(double maxHeight, Trampolin trampolin) {
//        this.playerPower = new PlayerPower();
        oscPeriod = 0.5* Math.PI * Math.sqrt(mass / trampolin.getSpringconst());
        fallPeriod = Math.sqrt((2 * maxHeight) / PlayerStatus.gravitaion);
        toleranceHeight = trampolin.toleranz();
    }

    protected PlayerStatus(double oscPeriod, double fallperiod, double toleranceHeight){
        this.lastUpdateTime = System.nanoTime();
        this.oscPeriod = oscPeriod;
        this.fallPeriod = fallperiod;
        this.toleranceHeight = toleranceHeight;
    }

    public abstract  void calculatePos(PlayerObject playerObject, PlayerPower playerPower, double maxHeight, Trampolin trampolin) throws PlayerDiedException;

    public abstract PlayerStatus getCurrentPlayerStatus();

    public abstract boolean isRising();

    public void updateFallperiod(double maxHeight){
        fallPeriod = Math.sqrt( (2 * maxHeight) / PlayerStatus.gravitaion );
    }


    public abstract void updatePower(PlayerPower playerPower, boolean fingerTouching, Player player, double maxHeight, Trampolin trampolin, long timeMikro);

    public abstract void countJump(JumpCounter jumps);


    public void trampolinChanged(Trampolin trampolin){
        oscPeriod = 0.5* Math.PI * Math.sqrt(mass / trampolin.getSpringconst());
    }
}
