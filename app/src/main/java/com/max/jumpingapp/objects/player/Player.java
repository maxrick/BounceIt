package com.max.jumpingapp.objects.player;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.max.jumpingapp.game.FingerReleasedEvent;
import com.max.jumpingapp.game.JumpMissedEvent;
import com.max.jumpingapp.game.JumpMissedException;
import com.max.jumpingapp.game.PlayerAcceleratedEvent;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.game.PlayerDiedException;
import com.max.jumpingapp.objects.visuals.Background;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.objects.Wind;
import com.max.jumpingapp.objects.visuals.PlayerObject;
import com.max.jumpingapp.types.Width;
import com.max.jumpingapp.types.XCenter;
import com.max.jumpingapp.types.XPosition;

import de.greenrobot.event.EventBus;

/**
 * Created by normal on 29.10.2015.
 */
public class Player {
    protected PlayerStatus playerStatus;
    protected Height curHeight;//this should be the bottom of the player
    protected PlayerPower playerPower;
    protected int maxHeight;
    protected PlayerObject playerObject;
    protected XPosition xPosition;
    protected Wind wind;

    public Player(XCenter playerXCenter, Width playerWidth, Bitmap playerImage, float footFromLeft, float footFromRight) {
        maxHeight = Math.abs(com.max.jumpingapp.game.GamePanel.HEIGHT_POS);
        this.playerObject = new PlayerObject(playerXCenter, playerWidth, com.max.jumpingapp.game.GamePanel.HEIGHT_POS, playerImage, footFromLeft, footFromRight);
        playerPower = new PlayerPower();
        EventBus.getDefault().register(playerPower);
        curHeight = new Height();
        wind = new Wind();
        Paint defaultPaint = new Paint();
        defaultPaint.setColor(Color.CYAN);

        playerStatus = new PlayerStatusFreeFalling(maxHeight);
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
        }
        playerObject.setRect(curHeight, xPosition);
        wind.blow(xPosition, curHeight);
        return curHeight;
    }

    public void updatePower(boolean fingerTouching, Trampolin trampolin, long timeMikro){
        playerStatus.updatePower(playerPower, fingerTouching);
        animate(fingerTouching);
    }


    public int draw(Canvas canvas, Background shape) {
        int moveBy = playerObject.draw(canvas, shape);

        return moveBy;
    }

    public void activateAccelaration(int accelerator, double maxPower) {
        maxHeight += accelerator;
        EventBus.getDefault().post(new PlayerAcceleratedEvent(accelerator));
        playerObject.setPaint(playerPower.accelerationPercentage(), wind);
        if(maxHeight <0){
            maxHeight=0;}
        playerStatus.updateFallperiod(maxHeight);
    }

    private void animate(boolean touching) {
        playerStatus.animate(playerObject, touching);

    }

    public float getLeft() {
        return playerObject.getLeft();
    }

    public float getRight() {
        return playerObject.getRight();
    }

    public float getBottom(){
        return playerObject.getBottom();
    }

    public void xAccel(int xSwipedToRight) {
        xPosition.adjustVelocity(xSwipedToRight);
    }

    public void onEvent(JumpMissedEvent event){
        System.out.println("jump missed");
        playerStatus.accelerate(maxHeight, playerPower);
        playerPower.activateAccelaration(this);
        playerObject.setMissedJump(true);
    }

    public void onEvent(FingerReleasedEvent event){
         playerStatus.onFingerReleased(playerPower, maxHeight);
    }

    public void unregisterEventlisteners() {
        EventBus.getDefault().unregister(playerPower);
    }
}
