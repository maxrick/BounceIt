package com.max.jumpingapp.objects.player;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.max.jumpingapp.JumpCounter;
import com.max.jumpingapp.PlayerDiedException;
import com.max.jumpingapp.objects.visuals.Background;
import com.max.jumpingapp.objects.Blaetter;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.objects.Wind;
import com.max.jumpingapp.objects.visuals.PlayerObject;
import com.max.jumpingapp.types.PlayerPower;
import com.max.jumpingapp.types.XPosition;

/**
 * Created by normal on 29.10.2015.
 */
public class Player {
    private PlayerStatus playerStatus;
    //position
    private int curHeight = 0;//this should be the bottom of the player
    protected PlayerPower playerPower;
    private double maxHeight;
    private double maxScore;
    private PlayerObject playerObject;
    protected XPosition xPosition;
    private JumpCounter jumps=new JumpCounter();

    //game
    private double score=0;
    private int right;


    public Player(int playerXCenter, int playerWidth, int formHeight, int height_pos, Bitmap playerImage) {
        maxHeight = Math.abs(height_pos);
        this.playerObject = new PlayerObject(playerXCenter, playerWidth, formHeight, height_pos, playerImage);
        playerPower = new PlayerPower();

        playerStatus = new PlayerStatusFreeFalling(maxHeight);
        xPosition = new XPosition(0);

    }

    public void updatePosition(Blaetter blaetter, Trampolin trampolin, Wind wind) throws PlayerDiedException {
        playerStatus = playerStatus.getCurrentPlayerStatus();
        playerStatus.countJump(jumps);
        playerObject.addBlattTo(blaetter);
        curHeight = playerStatus.calculatePos(playerPower, (int) maxHeight, xPosition, playerObject, trampolin);
        playerObject.setRect(curHeight, xPosition);
        wind.blow(xPosition, curHeight);
        updateBlaetter(blaetter);
        if (maxHeight > maxScore) {
            maxScore = maxHeight;
        }
    }

    public void updatePower(boolean fingerTouching, Trampolin trampolin, long timeMikro) {
        playerStatus.updatePower(playerPower, fingerTouching, this, maxHeight, trampolin, timeMikro);
        animate(fingerTouching);
    }

    public void removeBlaetter(Blaetter blaetter) {
        playerObject.removeTouchingLeaves(blaetter);
    }

    public int draw(Canvas canvas, Background shape) {
        int moveBy = playerObject.draw(canvas, shape);
        Paint testPaint = new Paint();
        testPaint.setColor(Color.CYAN);
        canvas.drawRect(new Rect(0, 0, 150, 120), testPaint);
        testPaint.setColor(Color.BLACK);
        canvas.drawText("Height: " + (maxHeight), 20, 20, testPaint);
        canvas.drawText("Score: " + (score), 20, 40, testPaint);
        canvas.drawText("Current: " + (curHeight), 20, 60, testPaint);
        jumps.draw(canvas, 20, 80, testPaint);
        return moveBy;
    }

    public Rect getRect() {
        return playerObject.rect;
    }

    public void activateAccelaration(int accelerator) {
        System.out.println("maxh: "+ maxHeight + " accel: " + accelerator);
        maxHeight += accelerator;
        if(maxHeight <0){
            maxHeight=0;}
        playerStatus.updateFallperiod(maxHeight);
    }

    private void animate(boolean touching) {
        playerObject.animate(touching);

    }

    private void updateBlaetter(Blaetter blaetter) {
        int touchingLeaves = playerObject.getTouchingBlaetter(blaetter) ;
        if(playerStatus.isRising() && touchingLeaves > 0) {
            playerPower.decelerate(touchingLeaves);
            playerPower.activateAccelaration(this);
        }
    }

    public void addScore() {
        score += maxHeight;
    }

    public void trampolinChanged(Trampolin trampolin) {
        playerStatus.trampolinChanged(trampolin);
    }

    public float getLeft() {
        float left = (float) (getRect().left + 0.3*getRect().width());
        return left;
    }

    public float getRight() {
        float right = (float) (getRect().right - 0.2*getRect().width());
        return right;
    }

    public float getBottom(){
        return getRect().bottom;
    }

    public void xAccel(int xSwipedToRight) {
        xPosition.adjustVelocity(xSwipedToRight);
    }
}
