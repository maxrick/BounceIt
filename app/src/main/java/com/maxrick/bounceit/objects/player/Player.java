package com.maxrick.bounceit.objects.player;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.maxrick.bounceit.events.FingerReleasedEvent;
import com.maxrick.bounceit.events.JumpMissedEvent;
import com.maxrick.bounceit.events.PlayerAcceleratedEvent;
import com.maxrick.bounceit.game.GamePanel;
import com.maxrick.bounceit.game.PlayerStatusDiedException;
import com.maxrick.bounceit.types.Height;
import com.maxrick.bounceit.game.PlayerDiedException;
import com.maxrick.bounceit.objects.visuals.Background;
import com.maxrick.bounceit.objects.Trampolin;
import com.maxrick.bounceit.objects.Wind;
import com.maxrick.bounceit.objects.visuals.PlayerObject;
import com.maxrick.bounceit.types.Width;
import com.maxrick.bounceit.types.XCenter;
import com.maxrick.bounceit.types.XPosition;

import de.greenrobot.event.EventBus;

/**
 * Created by normal on 29.10.2015.
 */
public class Player {
    protected PlayerStatus playerStatus;
    protected Height curHeight;//this should be the bottom of the player
    protected PlayerPower playerPower;
    protected int maxHeight;
    protected XPosition xPosition;
    protected Wind wind;

    public Player(XCenter playerXCenter, Width playerWidth, Bitmap playerImage, float footFromLeft, float footFromRight) {
        maxHeight = Math.abs(GamePanel.HEIGHT_POS);
        playerPower = new PlayerPower();
        EventBus.getDefault().register(playerPower);
        curHeight = new Height();
        wind = new Wind();
        Paint defaultPaint = new Paint();
        defaultPaint.setColor(Color.CYAN);

        playerStatus = new PlayerStatusFreeFalling(maxHeight, new PlayerObject(playerXCenter, playerWidth, GamePanel.HEIGHT_POS, playerImage, footFromLeft, footFromRight));
        xPosition = new XPosition();

    }

    public Player() {
    }

    public Height updatePosition(Trampolin trampolin) throws PlayerDiedException {
        playerStatus = playerStatus.getCurrentPlayerStatus(this);
        try{
            curHeight = playerStatus.calculatePos(playerPower, maxHeight, xPosition, this, trampolin);
        }catch (PlayerDiedException e){
            throw new PlayerDiedException(e.player);
        } catch (PlayerStatusDiedException e) {
            playerStatus = e.playerStatusDead;
            curHeight = new Height(0);
        }
        wind.blow(xPosition, curHeight);
        return curHeight;
    }

    public void updatePower(boolean fingerTouching, Trampolin trampolin, long timeMikro){
        playerStatus.updatePower(playerPower, fingerTouching);
        animate(fingerTouching);
    }


    public int draw(Canvas canvas, Background shape) {
        int moveBy = playerStatus.draw(canvas, shape);

        return moveBy;
    }

    public void activateAccelaration(int accelerator) {
        maxHeight += accelerator;
        EventBus.getDefault().post(new PlayerAcceleratedEvent(accelerator));
        playerStatus.setPaint(playerPower.accelerationPercentage(), wind);
        if(maxHeight <0){
            maxHeight=0;}
        playerStatus.updateFallperiod(maxHeight);
    }

    private void animate(boolean touching) {
        playerStatus.animate(touching);

    }

    public float getLeft() {
        return playerStatus.getLeft();
    }

    public float getRight() {
        return playerStatus.getRight();
    }

    public float getBottom(){
        return playerStatus.getBottom();
    }

    public void xAccel(int xSwipedToRight) {
        xPosition.adjustVelocity(xSwipedToRight);
    }

    public void onEvent(JumpMissedEvent event){
        playerStatus.accelerate(maxHeight, playerPower);
        playerPower.activateAccelaration(this);
        playerStatus.setMissedJump(true);
    }

    public void onEvent(FingerReleasedEvent event){
         playerStatus.onFingerReleased(playerPower, maxHeight);
    }

    public void unregisterEventlisteners() {
        EventBus.getDefault().unregister(playerPower);
    }
}
