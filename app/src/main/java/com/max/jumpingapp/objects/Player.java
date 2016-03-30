package com.max.jumpingapp.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.max.jumpingapp.GamePanel;
import com.max.jumpingapp.JumpCounter;
import com.max.jumpingapp.PlayerDiedException;
import com.max.jumpingapp.PlayerObject;
import com.max.jumpingapp.PlayerPower;
import com.max.jumpingapp.PlayerStatus;
import com.max.jumpingapp.PlayerStatusFreeFalling;
import com.max.jumpingapp.Wind;
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


    public Player(int left, int right, int formHeight, int heightPos, Trampolin trampolin, Bitmap playerImage) {
        maxHeight = Math.abs(heightPos);
        playerObject = new PlayerObject(
                new Rect(left, GamePanel.heightNill - (int) maxHeight - formHeight, right, GamePanel.heightNill - (int) maxHeight), playerImage
        );
        playerPower = new PlayerPower();

        playerStatus = new PlayerStatusFreeFalling(maxHeight, trampolin);
        xPosition = new XPosition(0);

    }

    public void updatePosition(Blaetter blaetter, Trampolin trampolin, Wind wind){
        playerStatus = playerStatus.getCurrentPlayerStatus();
        playerStatus.countJump(jumps);
            playerObject.addBlattTo(blaetter);
        try {
            curHeight = playerStatus.calculatePos(playerObject, playerPower, (int) maxHeight, trampolin, xPosition);
        } catch (PlayerDiedException e) {
            die(e.trampolin);
            //System.out.println("would have died");
        }
        wind.blow(xPosition, curHeight);
        updateBlaetter(blaetter);
        if (maxHeight > maxScore) {
            maxScore = maxHeight;
        }
    }

    private void die(Trampolin trampolin) {
        System.out.println("dead with maxHeight: "+maxHeight);
        Player p = new Player(this.getRect().left, this.getRect().right, 200, 300, trampolin, playerObject.image);
        GamePanel.meGamePanel.setNewPlayer(p);
        //int dec = (int) (maxHeight/3);
        //playerPower.decelerate(dec);
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
