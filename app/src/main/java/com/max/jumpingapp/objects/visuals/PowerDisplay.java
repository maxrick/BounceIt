package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.max.jumpingapp.game.GamePanel;

/**
 * Created by max on 4/6/2016.
 */
public class PowerDisplay {
    private static final long timeToDisplayPower = 1000000000; //1 Second
    public static final int LEFT = 500;
    public static final int TOP = 50;
    public static final int RIGHT = 700;
    public static final int BOTTOM = 100;
    private long time;
    Rect border;
    Rect powerLoader;
    Rect minPower;
    Paint minPowerPaint;
    Paint borderPaint;
    Paint powerLoaderPaint;

    public PowerDisplay() {
        time = System.nanoTime();
        border = new Rect(LEFT, TOP, RIGHT, BOTTOM);
        powerLoader = new Rect(LEFT, TOP, LEFT, BOTTOM);
        minPower = new Rect(LEFT, TOP, LEFT, BOTTOM);
        borderPaint = new Paint();
        borderPaint.setColor(Color.BLUE);
        borderPaint.setStyle(Paint.Style.STROKE);
        powerLoaderPaint = new Paint();
        powerLoaderPaint.setColor(GamePanel.GREEN1);
        powerLoaderPaint.setStyle(Paint.Style.FILL);
        minPowerPaint = new Paint();
        minPowerPaint.setColor(GamePanel.RED1);
        minPowerPaint.setStrokeWidth(3);
        minPowerPaint.setStyle(Paint.Style.STROKE);
    }

    public void setPowerPercentage(double percentage) {
        powerLoader.right = (int) (percentage / 100 * (RIGHT - LEFT) + LEFT);
        time = System.nanoTime();
    }

    public void resetAfterTime() {
        long elapsed = System.nanoTime() - time;
        if (elapsed > timeToDisplayPower) {
            powerLoader.right = LEFT;
        }
    }

    public void setMinPower(double minPercentage) {
        minPower.left = (int) (minPercentage / 100 * (RIGHT - LEFT) + LEFT);
        minPower.right = (int) (minPercentage / 100 * (RIGHT - LEFT) + LEFT);
    }

    public void draw(Canvas canvas) {
        resetAfterTime();
        canvas.drawRect(powerLoader, powerLoaderPaint);
        canvas.drawRect(border, borderPaint);
        canvas.drawRect(minPower, minPowerPaint);
    }
}
