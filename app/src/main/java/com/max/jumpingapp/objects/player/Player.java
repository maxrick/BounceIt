package com.max.jumpingapp.objects.player;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.max.jumpingapp.game.MainThread;
import com.max.jumpingapp.objects.visuals.Animator;
import com.max.jumpingapp.objects.visuals.HighscoreDisplay;
import com.max.jumpingapp.objects.visuals.LastHeightDisplay;
import com.max.jumpingapp.objects.visuals.MessageDisplayer;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.JumpCounter;
import com.max.jumpingapp.game.PlayerDiedException;
import com.max.jumpingapp.objects.visuals.Background;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.objects.Wind;
import com.max.jumpingapp.objects.visuals.PlayerObject;
import com.max.jumpingapp.types.PlayerPower;
import com.max.jumpingapp.types.Score;
import com.max.jumpingapp.types.ScoreBoardData;
import com.max.jumpingapp.types.Width;
import com.max.jumpingapp.types.XCenter;
import com.max.jumpingapp.types.XPosition;

/**
 * Created by normal on 29.10.2015.
 */
public class Player {
    private static final long timeToDisplayMessage = 1000000000;
    private PlayerStatus playerStatus;
    //position
    private Height curHeight;//this should be the bottom of the player
    protected PlayerPower playerPower;
    private int maxHeight;
    private Score maxScore;
    private PlayerObject playerObject;
    protected XPosition xPosition;
    private JumpCounter jumps=new JumpCounter();
    private Animator animator;


    //game
    private double score=0;
    private int right;


    public Player(XCenter playerXCenter, Width playerWidth, int height_pos, Bitmap playerImage) {
        maxHeight = Math.abs(height_pos);
        maxScore = new Score(maxHeight);
        this.playerObject = new PlayerObject(playerXCenter, playerWidth, height_pos, playerImage);
        playerPower = new PlayerPower();
        curHeight = new Height();

        Paint defaultPaint = new Paint();
        defaultPaint.setColor(Color.CYAN);
        animator = new Animator(defaultPaint);

        playerStatus = new PlayerStatusFreeFalling(maxHeight);
        xPosition = new XPosition(0);

    }

    public ScoreBoardData updatePosition(Trampolin trampolin, Wind wind) throws PlayerDiedException {
        playerStatus = playerStatus.getCurrentPlayerStatus();
        playerStatus.countJump(jumps);
        try{
            curHeight = playerStatus.calculatePos(playerPower, maxHeight, xPosition, this, trampolin);
        }catch (PlayerDiedException e){
            throw new PlayerDiedException(e.player, maxScore);
        }
        playerObject.setRect(curHeight, xPosition);
        wind.blow(xPosition, curHeight);
        maxScore.update(maxHeight);
        return new ScoreBoardData(curHeight, maxScore);
    }

    public void updatePower(boolean fingerTouching, Trampolin trampolin, long timeMikro) {
        playerStatus.updatePower(playerPower, fingerTouching, this, maxHeight, trampolin, timeMikro);
        animate(fingerTouching);
    }


    public int draw(Canvas canvas, Background shape) {
        int moveBy = playerObject.draw(canvas, shape);

//        jumps.draw(canvas, 20, 80, testPaint);//@// TODO: 4/11/2016 delet jumcounter

        playerPower.draw(canvas);
        return moveBy;
    }

    public Rect getRect() {
        return playerObject.rect;
    }

    public void activateAccelaration(int accelerator, double maxPower) {
        System.out.println("maxh: "+ maxHeight + " accel: " + accelerator);
        maxHeight += accelerator;
        animator.animate(100*accelerator/maxPower, playerObject);
        if(maxHeight <0){
            maxHeight=0;}
        playerStatus.updateFallperiod(maxHeight);
    }

    private void animate(boolean touching) {
        playerObject.animate(touching);

    }

    public void addScore() {
        score += maxHeight;
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

    public void missedJump() {
        System.out.println("jump missed");
        playerStatus.accelerateOnce(maxHeight, playerPower);
        playerPower.activateAccelarationNoCheck(this);

//        playerObject.setMissedJump(true);
//        animator.animate(0, playerObject);
//        playerStatus.updatePowerDisplay(playerPower);
    }

}
