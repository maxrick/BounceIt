package com.max.jumpingapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;

/**
 * Created by normal on 29.08.2015.
 */
public class Background {
    public static final int GREY_STEP = 60;
    private Bitmap image;
    private int x, y;

    public Background(Bitmap res) {
		image = res;
//        image = Bitmap.createScaledBitmap(res, dstWidth, dstHeight, false);
        y = 0;
    }

    private void move(int moveBackground) {
        y = -moveBackground %image.getHeight();
//        if(y>image.getHeight() || y < -image.getHeight()){
//            y=0;
//        }
    }

    private void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
        if (y > 0) {
            canvas.drawBitmap(image, x, y-image.getHeight(), null);
        }
    }

    public void drawMovedBy(Canvas canvas, int moveBy) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        int alpha = (-moveBy/image.getHeight())* GREY_STEP;
        paint.setAlpha(255-alpha);

        y = -moveBy %image.getHeight();
        canvas.drawBitmap(image, x, y, paint);

        paint.setAlpha(255-(alpha+GREY_STEP));
        System.out.println("moved by: "+moveBy+" alpha: "+ alpha);
        if (y > 0) {
            canvas.drawBitmap(image, x, y-image.getHeight(), paint);
        }
    }
}
