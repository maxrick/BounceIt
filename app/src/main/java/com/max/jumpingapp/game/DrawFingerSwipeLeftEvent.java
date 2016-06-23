package com.max.jumpingapp.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.max.jumpingapp.objects.visuals.DrawObjects;
import com.max.jumpingapp.tutorial.ScreenMessage;

/**
 * Created by Max Rickayzen on 23.06.2016.
 */
public class DrawFingerSwipeLeftEvent extends DrawFingerSwipeEvent{
    protected int xMover = 300;
    public DrawFingerSwipeLeftEvent(ScreenMessage message, long time) {
        super(message, time);
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap image;
        image= DrawObjects.fingerReleaseImage;
        xMover-=20;
        canvas.drawBitmap(image, message.getxPos()+xMover, message.getyPos(), paint);
    }
}
