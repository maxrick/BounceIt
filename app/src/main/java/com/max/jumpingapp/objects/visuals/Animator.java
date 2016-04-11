package com.max.jumpingapp.objects.visuals;

import android.graphics.Color;
import android.graphics.Paint;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.objects.Wind;
import com.max.jumpingapp.objects.visuals.PlayerObject;

public class Animator {
    private static final long timeToDisplayAnimation = 1000000000; //1 Second
    private boolean animated = false;
    private long time;
    private Paint defaultPaint;
    private Paint currentPaint;


    public Animator(Paint defaultPaint) {
        this.defaultPaint = defaultPaint;
        this.currentPaint = new Paint(defaultPaint);
    }

    public void animate(double accelPercentage, PlayerObject playerObject) {
        time = System.nanoTime();
        setAnimatedColor(accelPercentage, playerObject);
        if(accelPercentage < 0){//@// TODO: 4/5/2016  does not belong here 
            Wind.moreWind();
        }
    }

    private void setAnimatedColor(double accelerator, PlayerObject playerObject) {
        if (accelerator < -150) {
            currentPaint.setColor(GamePanel.RED5);
            playerObject.setPlayerColor(GamePanel.RED5);
        } else if (accelerator < -80) {
            currentPaint.setColor(GamePanel.RED4);
            playerObject.setPlayerColor(GamePanel.RED4);
        } else if (accelerator < -50) {
            currentPaint.setColor(GamePanel.RED3);
            playerObject.setPlayerColor(GamePanel.RED3);
        } else if (accelerator < -20) {
            currentPaint.setColor(GamePanel.RED2);
            playerObject.setPlayerColor(GamePanel.RED2);
        } else if (accelerator < 0) {
            currentPaint.setColor(GamePanel.RED1);
            playerObject.setPlayerColor(GamePanel.RED1);
        }else if(accelerator ==0){
            currentPaint.setColor(defaultPaint.getColor());
            playerObject.setPlayerColor(Color.DKGRAY);
        }else if (accelerator < 20) {
            currentPaint.setColor(GamePanel.GREEN1);
            playerObject.setPlayerColor(GamePanel.GREEN1);
        } else if (accelerator < 40) {
            currentPaint.setColor(GamePanel.GREEN2);
            playerObject.setPlayerColor(GamePanel.GREEN2);
        } else if (accelerator < 60) {
            currentPaint.setColor(GamePanel.GREEN3);
            playerObject.setPlayerColor(GamePanel.GREEN3);
        } else if (accelerator < 80) {
            currentPaint.setColor(GamePanel.GREEN4);
            playerObject.setPlayerColor(GamePanel.GREEN4);
        } else {
            currentPaint.setColor(GamePanel.GREEN5);
            playerObject.setPlayerColor(GamePanel.GREEN5);
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