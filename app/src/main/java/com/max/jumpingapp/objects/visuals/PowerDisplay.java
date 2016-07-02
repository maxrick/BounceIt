package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.max.jumpingapp.events.LeftTrampolinEvent;
import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.events.LivePowerEvent;
import com.max.jumpingapp.events.PlayerAcceleratedEvent;
import com.max.jumpingapp.events.ResetPowerDisplayEvent;

/**
 * Created by max on 4/6/2016.
 */
public class PowerDisplay {
    private static final long timeToDisplayPower = 1000000000; //1 Second
    public static final int LEFT = GamePanel.screenWidth - ScoreBoardDisplay.GAP_RIGHT;//500;
    public static final int TOP = 100;
    public static final int RIGHT = GamePanel.screenWidth - ScoreBoardDisplay.GAP_RIGHT + 200;//700;
    public static final int BOTTOM = 150;
    private long lastUpdateTime;
    private boolean leftTrampolin;
    Rect border;
    Rect powerLoader;
    Rect minPower;
    Paint minPowerPaint;
    Paint borderPaint;
    Paint powerLoaderPaint;
    Paint textPaint;

    public PowerDisplay() {
        border = new Rect(LEFT, TOP, RIGHT, BOTTOM);
        powerLoader = new Rect(LEFT, TOP, LEFT, BOTTOM);
        borderPaint = new Paint();
        borderPaint.setColor(Color.BLUE);
        borderPaint.setStyle(Paint.Style.STROKE);
        powerLoaderPaint = new Paint();
        powerLoaderPaint.setColor(GamePanel.GREEN1);
        powerLoaderPaint.setStyle(Paint.Style.FILL);
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        leftTrampolin=false;
        lastUpdateTime=System.nanoTime();
    }

    public void onEvent(LivePowerEvent event) {
        resetColor();
        powerLoader.right = (int) (event.value() / 100 * (RIGHT - LEFT) + LEFT);
        if(powerLoader.right < powerLoader.left){
            powerLoaderPaint.setColor(GamePanel.RED1);
        }
    }

    public void onEvent(ResetPowerDisplayEvent event){
        reset();
    }

    public void onEvent(PlayerAcceleratedEvent event){
        Animator.setAnimatedColor(event.getAccelerator(), powerLoaderPaint);
    }

    private void reset() {
        powerLoader.right = LEFT;
        resetColor();
    }

    private void resetColor() {
        powerLoaderPaint.setColorFilter(null);
        powerLoaderPaint.setColor(GamePanel.GREEN1);
    }

    public void onEvent(LeftTrampolinEvent event){
        leftTrampolin=true;
        lastUpdateTime =System.nanoTime();
    }

    public void resetAfterTime() {
        long elapsed = System.nanoTime() - lastUpdateTime;
        if (leftTrampolin && elapsed > timeToDisplayPower) {
            System.out.println("reset after time");
            leftTrampolin = false;
            reset();
        }
    }

    public void draw(Canvas canvas) {
        resetAfterTime();
        System.out.println("draw power: "+powerLoader.right);
        canvas.drawText("Power",LEFT, TOP-10,textPaint );
        canvas.drawRect(powerLoader, powerLoaderPaint);
        canvas.drawRect(border, borderPaint);
    }
}
