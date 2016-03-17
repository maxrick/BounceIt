package com.max.jumpingapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by normal on 29.08.2015.
 */
public class Trampolin {
    private double Springconst;
    public int yPos;
    public int originalYPos;
    private int xPos;
    private int width;
    private Paint paint;
    private boolean down = true;
    private boolean jumpCounted=false;
    public int jumps=0;


    public Trampolin(int x, int y, int width, double springconst) {
        xPos = x;
        y = GamePanel.heightNill;
        yPos = y;
        originalYPos = y;
        this.width = width;
        this.Springconst = springconst;
        Paint defaultPaint = new Paint();
        defaultPaint.setColor(Color.YELLOW);
        defaultPaint.setStrokeWidth(2);
        defaultPaint.setStyle(Paint.Style.STROKE);
        defaultPaint.setAntiAlias(true);
        this.paint = defaultPaint;

    }

    public Trampolin(int x, int y, int width, double springconst, Paint paint) {
        this(x, y, width, springconst);
        this.paint = paint;
    }

    public Trampolin upgrade(double increaseSpringConst){
        return new Trampolin(this.xPos, this.yPos, this.width, this.Springconst + increaseSpringConst);
    }
    public int draw(Canvas canvas, Player player, int screenheight, int screenWidth, Drawable shape) {


       // Rect rect = player.getRect();
        int moveBy = player.draw(canvas, screenheight, screenWidth, shape);

//        Path myPath = calculateBezier(rect.exactCenterX(), rect.width(), rect.bottom);
        Path myPath = trampCurve(player.getLeft(), player.getRight(), player.getBottom());
        myPath.offset(0, -moveBy);
//        myPath.quadTo(30, 80, 50, 30);
        canvas.drawPath(myPath, paint);
//        canvas.drawText(String.valueOf(speed), 10, 10, paint);
//        canvas.drawText(String.valueOf(playerPower), 50, 20, paint);
        return moveBy;
    }

//    private Path trampCurve(float xCenter, int width, int bottom) {
//        Path myPath = new Path();
//        myPath.moveTo(xPos, yPos);
//        if (bottom > yPos) {
//            myPath.quadTo((xCenter - 0.5F * width) - 0.2F * (xCenter - 0.5F * width - xPos), yPos + 0.2F * (bottom - yPos), xCenter - 0.5F * width, bottom);
//            myPath.quadTo(xCenter, bottom + (bottom - yPos) * 0.3F, xCenter + 0.5F * width, bottom);
//        }
//        myPath.quadTo((xCenter + 0.5F * width) + 0.2F * (xPos + this.width - (xCenter + 0.5F * width)), yPos, xPos + this.width, yPos);
//        return myPath;
//    }
private Path trampCurve(float left, float right, float bottom) {
    Path myPath = new Path();
    myPath.moveTo(xPos, yPos);
    float xCenter = (left+right)/2;
    if (bottom > yPos) {
        myPath.quadTo(left - 0.2F * (left - xPos), yPos + 0.2F * (bottom - yPos), left, bottom);
        myPath.quadTo(xCenter, bottom + (bottom - yPos) * 0.3F, right, bottom);
    }
    myPath.quadTo(right + 0.2F * (xPos + this.width - (right)), yPos, xPos + this.width, yPos);
    return myPath;
}

    public boolean playerTouching(Player player) {
        Rect rect = player.getRect();
        boolean touching = (rect.bottom >= yPos);
        if(touching && !jumpCounted){
            jumpCounted = true;
            jumps++;
            player.addScore();
        }
        if(!touching && jumpCounted){
            jumpCounted = false;
        }
        return (touching);
    }

    public double toleranz(){
        return -Math.pow(Springconst, 2);
    }
    public double getSpringconst(){
        return Springconst;
    }

    public double calcAccelerationFactor(double maxHeight) {
        double result = 10 - Springconst/10 + Math.pow(Springconst, 1.2)/(100*Springconst);//without *maxHeight, gets to exponential
        return result;//better formular needed
    }//Math.abs(playerPower)*maxHeight/100;
}
