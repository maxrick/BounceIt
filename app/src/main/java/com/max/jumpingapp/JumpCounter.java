package com.max.jumpingapp;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by normal on 31.10.2015.
 */
public class JumpCounter {
    private int jumps=0;
    private boolean counted=false;
    public void addJump(){
        if(!counted){
            jumps++;
            counted=true;
        }
    }

    public void draw(Canvas canvas, int xPos, int yPos, Paint testPaint) {
        canvas.drawText("Jumps: " + (jumps), xPos, yPos, testPaint);
    }

    public void notYetCounted() {
        counted=false;
    }
}
