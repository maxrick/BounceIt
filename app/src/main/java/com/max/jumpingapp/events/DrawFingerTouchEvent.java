package com.max.jumpingapp.events;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.max.jumpingapp.objects.visuals.DrawObjects;
import com.max.jumpingapp.tutorial.ScreenMessage;

/**
 * Created by Max Rickayzen on 23.06.2016.
 */
public class DrawFingerTouchEvent extends HelpInstructionEvent {
    public DrawFingerTouchEvent(ScreenMessage message, long time) {
        super(message, time);
    }

    @Override
    public void draw(Canvas canvas) {
        //drawMultiline(message.getMessage(), message.getxPos(), message.getyPos(), paint, canvas);
        Bitmap image;
        image= DrawObjects.fingerTouchImage;
        canvas.drawBitmap(image, message.getxPos(), message.getyPos(), paint);
    }
}
