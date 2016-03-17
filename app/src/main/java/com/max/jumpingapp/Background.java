package com.max.jumpingapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by normal on 29.08.2015.
 */
public class Background {
    private Bitmap image;
    private int x, y;

    public Background(Bitmap res, int dstWidth, int dstHeight) {
//		image = res;
        image = Bitmap.createScaledBitmap(res, dstWidth, dstHeight, false);
        x = 0;
        System.out.println("scaled " + dstWidth);
    }

    public void move(int moveBackground) {
        y += moveBackground;
    }

    public void update(int newY) {
        y = newY;
//        if(x<-image.getWidth() ){
//            x=0;
//        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
        if (x < 0) {
            canvas.drawBitmap(image, x + image.getWidth(), y, null);
        }
    }
}
