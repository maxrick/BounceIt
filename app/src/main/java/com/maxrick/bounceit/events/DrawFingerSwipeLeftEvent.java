package com.maxrick.bounceit.events;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.maxrick.bounceit.objects.visuals.DrawObjects;
import com.maxrick.bounceit.tutorial.ScreenMessage;

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
        image= DrawObjects.fingerTouchImage;
        xMover-=40;
        canvas.drawBitmap(image, message.getxPos()+xMover, message.getyPos(), paint);
    }
}
