package com.max.jumpingapp.objects.visuals;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by normal on 29.08.2015.
 */
public class Background {
    public static final int GREY_STEP = 60;
    private Bitmap image;
    private int x, y;
    private static final long timeToDisplayAnimation = 300000000; //1 Second
    private long time;

    public Background(Bitmap res) {
        image = res;
//        double scaleFactor = GamePanel.screenWidth*1.0 / res.getWidth();
//        image = Bitmap.createScaledBitmap(res, GamePanel.screenWidth, (int)( GamePanel.screenHeight*scaleFactor), false); @// TODO: 4/11/2016 scale image
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
        paint.setAlpha(255 - alpha);
        y = -moveBy %image.getHeight();
        canvas.drawBitmap(image, x, y, paint);

        paint.setAlpha(255 - (alpha + GREY_STEP));
        if (y > 0) {
            canvas.drawBitmap(image, x, y-image.getHeight(), paint);
        }
    }

}
