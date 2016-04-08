package com.max.jumpingapp;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;

import com.max.jumpingapp.objects.Game;
import com.max.jumpingapp.objects.Wind;
import com.max.jumpingapp.objects.player.PlayerStatus;
import com.max.jumpingapp.objects.visuals.Background;

public class Animator {
    private static final long timeToDisplayAnimation = 1000000000; //1 Second
    private boolean animated = false;
    private long time;
    private Paint defaultPaint;
    private Paint currentPaint;
    private Background background;


    public Animator(Paint defaultPaint, Background background) {
        this.defaultPaint = defaultPaint;
        this.currentPaint = new Paint(defaultPaint);
        this.background = background;
    }

    void animate(boolean touching) {
//        if (touching && (playerStatus.getRect().height() - 3) >= playerStatus.getMinHeightRect()) {
//            playerStatus.getRect().top += 5;
//        }
//        if (!touching && ((playerStatus.getRect().height() + 10 < playerStatus.getNormalHeightRect()) || playerStatus.isAnimateStrech())) {
//            if (!playerStatus.isAnimateStrech()) {
//                playerStatus.setAnimateStrech(true);
//                playerStatus.setMaxHeightRect(2 * playerStatus.getNormalHeightRect() - playerStatus.getRect().height());
//            }
//            playerStatus.getRect().top -= 10;
//            if (playerStatus.getRect().height() > playerStatus.getMaxHeightRect()) {
//                playerStatus.setAnimateStrech(false);
//            }
//        }
//        if (!touching && !playerStatus.isAnimateStrech() && playerStatus.getRect().height() - 15 > playerStatus.getNormalHeightRect()) {
//            playerStatus.getRect().top += 15;
//        }

    }

    public void animate(double accelPercentage) {
        time = System.nanoTime();
        setAnimatedColor(accelPercentage);
        if(accelPercentage < 0){//@// TODO: 4/5/2016  does not belong here 
            Wind.moreWind();
        }
    }

    private void setAnimatedColor(double accelerator) {
        if (accelerator < -150) {
            currentPaint.setColor(GamePanel.RED5);
        } else if (accelerator < -80) {
            currentPaint.setColor(GamePanel.RED4);
        } else if (accelerator < -50) {
            currentPaint.setColor(GamePanel.RED3);
        } else if (accelerator < -20) {
            currentPaint.setColor(GamePanel.RED2);
        } else if (accelerator < 0) {
            currentPaint.setColor(GamePanel.RED1);
        } else if (accelerator < 20) {
            currentPaint.setColor(GamePanel.GREEN1);
        } else if (accelerator < 40) {
            currentPaint.setColor(GamePanel.GREEN2);
        } else if (accelerator < 60) {
            currentPaint.setColor(GamePanel.GREEN3);
        } else if (accelerator < 80) {
            currentPaint.setColor(GamePanel.GREEN4);
        } else {
            currentPaint.setColor(GamePanel.GREEN5);
        }

        if(accelerator<0){
            background.setCurrentImage(GamePanel.ERROR_3);
        }
        if(accelerator>0){
            background.setCurrentImage(GamePanel.SUCCESS_3);
        }
        if(accelerator == 0){
            background.setCurrentImage(GamePanel.GREY);
        }
    }

    public Paint adjustedPaint() {
        long elapsed = System.nanoTime()-time;
        if(elapsed < timeToDisplayAnimation){
            return currentPaint;
        }else {
            return defaultPaint;
        }

    }
}