package com.max.jumpingapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

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
    private JumpCounter jumps=new JumpCounter();

    //game
    private double score=0;
    private int right;


    public Player(int left, int right, int formHeight, int heightPos, Trampolin trampolin, Bitmap playerImage) {
        maxHeight = Math.abs(heightPos);
        playerObject = new PlayerObject(
                new Rect(left, GamePanel.heightNill - (int) maxHeight - formHeight, right, GamePanel.heightNill - (int) maxHeight), playerImage
        );
        playerPower = new PlayerPower();

        playerStatus = new PlayerStatusFreeFalling(maxHeight, trampolin);

    }

    public void updatePosition(Blaetter blaetter, Trampolin trampolin){
        playerStatus = playerStatus.getCurrentPlayerStatus();
        playerStatus.countJump(jumps);
            playerObject.addBlattTo(blaetter);
        try {
            playerStatus.calculatePos(playerObject, playerPower, (int) maxHeight, trampolin);
        } catch (PlayerDiedException e) {
            die();
        }
        updateBlaetter(blaetter);
        if ((GamePanel.heightNill - curHeight) > maxScore) {
            maxScore = (GamePanel.heightNill - curHeight);
        }
    }

    private void die() {
        System.out.println("dead");
        int dec = (int) (maxHeight/3);
        playerPower.decelerate(dec);
    }

    public void updatePower(boolean fingerTouching, Trampolin trampolin) {
        playerStatus.updatePower(playerPower, fingerTouching, this, maxHeight, trampolin);
        animate(fingerTouching);
    }

    public void removeBlaetter(Blaetter blaetter) {
        playerObject.removeTouchingLeaves(blaetter);
    }

    public int draw(Canvas canvas, int screenheight, int screenWidth, Drawable shape) {
        int moveBy = playerObject.draw(canvas, screenheight, screenWidth, shape);
        Paint testPaint = new Paint();
        testPaint.setColor(Color.CYAN);
        canvas.drawRect(new Rect(0, 0, 150, 120), testPaint);
        testPaint.setColor(Color.BLACK);
        canvas.drawText("Height: " + ( maxHeight), 20, 20, testPaint);
        canvas.drawText("Score: " + (score), 20, 50, testPaint);
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
}
