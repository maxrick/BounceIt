package com.maxrick.bounceit.objects.player;

import android.graphics.Canvas;

import com.maxrick.bounceit.game.GamePanel;
import com.maxrick.bounceit.game.PlayerStatusDiedException;
import com.maxrick.bounceit.objects.Wind;
import com.maxrick.bounceit.objects.visuals.Background;
import com.maxrick.bounceit.objects.visuals.PlayerObject;
import com.maxrick.bounceit.types.Height;
import com.maxrick.bounceit.game.PlayerDiedException;
import com.maxrick.bounceit.objects.Trampolin;
import com.maxrick.bounceit.types.XPosition;

/**
 * Created by normal on 02.09.2015.
 */
public abstract class PlayerStatus {
    //physics
    public static double gravitaion = 1000;//10m/s -> 1000px/s
    protected boolean testDieSet=false;
    protected static double mass = 1;

    protected double oscPeriod;
    protected double fallPeriod;
    protected PlayerObject playerObject;

    public PlayerStatus(double maxHeight,PlayerObject  playerObject) {
        this( (0.5* Math.PI * Math.sqrt(mass / GamePanel.SPRINGCONST)) , (Math.sqrt((2 * maxHeight) / PlayerStatus.gravitaion)) , playerObject);
    }

    protected PlayerStatus(double oscPeriod, double fallperiod, PlayerObject playerObject){
        GamePanel.refreshUpdateTime();
        this.oscPeriod = oscPeriod;
        this.fallPeriod = fallperiod;
        this.playerObject = playerObject;
    }

    public abstract Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException, PlayerStatusDiedException;

    public abstract PlayerStatus getCurrentPlayerStatus(Player player);

    public void updateFallperiod(double maxHeight){
        fallPeriod = Math.sqrt( (2 * maxHeight) / PlayerStatus.gravitaion );
    }


    public abstract void updatePower(PlayerPower playerPower, boolean fingerTouching);

    public void accelerate(double maxHeight, PlayerPower playerPower) {
        playerPower.setAccelerator(maxHeight, oscPeriod);
    }

    public abstract void onFingerReleased(PlayerPower playerPower, int maxHeight);

    public abstract void animate(boolean touching);

    public int draw(Canvas canvas, Background shape) {
        return playerObject.draw(canvas, shape);
    }

    public void setPaint(double accelerationPercentage, Wind wind) {
        playerObject.setPaint(accelerationPercentage, wind);
    }

    public float getLeft() {
        return playerObject.getLeft();
    }

    public float getRight() {
        return playerObject.getRight();
    }

    public float getBottom() {
        return playerObject.getBottom();
    }

    public void setMissedJump(boolean b) {
        playerObject.setMissedJump(b);
    }
}
