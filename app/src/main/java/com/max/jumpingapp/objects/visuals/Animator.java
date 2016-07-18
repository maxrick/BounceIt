package com.max.jumpingapp.objects.visuals;

import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.objects.Wind;

public class Animator {
    private static final long timeToDisplayAnimation = 1000000000; //1 Second
    private long time;
    private Paint defaultPaint;
    private Paint currentPaint;


    public Animator(Paint defaultPaint) {
        this.defaultPaint = defaultPaint;
        this.currentPaint = new Paint(defaultPaint);
    }

    public void animate(double accelPercentage, Wind wind) {
        time = System.nanoTime();
        setAnimatedColorThreeSteps(accelPercentage);
        if(accelPercentage < 0){//@// TODO: 4/5/2016  does not belong here 
            wind.moreWind();
        }
    }

    private void setAnimatedColorThreeSteps(double accelerator) {
        if (accelerator < 0) {
            currentColor(GamePanel.RED1, currentPaint);
        }
    }

    public static void setAnimatedColor(double accelerator, Paint currentPaint) {
        if (accelerator < -150) {
            currentColor(GamePanel.RED5, currentPaint);
        } else if (accelerator < -80) {
            currentColor(GamePanel.RED4, currentPaint);
        } else if (accelerator < -50) {
            currentColor(GamePanel.RED3, currentPaint);
        } else if (accelerator < -20) {
            currentColor(GamePanel.RED2, currentPaint);
        } else if (accelerator < 0) {
            currentColor(GamePanel.RED1, currentPaint);
        }else if(accelerator ==0){
            currentColor(Color.DKGRAY, currentPaint);
        }else if (accelerator < 20) {
            currentColor(GamePanel.GREEN1, currentPaint);
        } else if (accelerator < 40) {
            currentColor(GamePanel.GREEN2, currentPaint);
        } else if (accelerator < 60) {
            currentColor(GamePanel.GREEN3, currentPaint);
        } else if (accelerator < 80) {
            currentColor(GamePanel.GREEN4, currentPaint);
        } else {
            currentColor(GamePanel.GREEN5, currentPaint);
        }

    }

    public static void currentColor(int color, Paint currentPaint) {
        currentPaint.setColorFilter(new LightingColorFilter(Color.rgb(0, 0, 0), color));
    }

    public Paint adjustedPaint() {
        long elapsed = System.nanoTime()-time;
        if(elapsed > timeToDisplayAnimation){
            currentPaint = new Paint(defaultPaint);
        }
        return currentPaint;
    }
}