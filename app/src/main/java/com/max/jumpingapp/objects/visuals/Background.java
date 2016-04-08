package com.max.jumpingapp.objects.visuals;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;

import com.max.jumpingapp.GamePanel;

import java.util.Map;

/**
 * Created by normal on 29.08.2015.
 */
public class Background {
    public static final int GREY_STEP = 60;
    private Map<String, Bitmap> images;
    private Bitmap currentImage;
    private int x, y;
    private static final long timeToDisplayAnimation = 300000000; //1 Second
    private long time;

    public Background(Map<String, Bitmap> res) {
		images = res;
        currentImage = images.get(GamePanel.NORMAL);
//        image = Bitmap.createScaledBitmap(res, dstWidth, dstHeight, false);
        y = 0;
    }

    private void move(int moveBackground) {
        y = -moveBackground %currentImage.getHeight();
//        if(y>image.getHeight() || y < -image.getHeight()){
//            y=0;
//        }
    }

    private void draw(Canvas canvas) {
        canvas.drawBitmap(currentImage, x, y, null);
        if (y > 0) {
            canvas.drawBitmap(currentImage, x, y-currentImage.getHeight(), null);
        }
    }

    private void resetImageAfterTime() {
        long elapsed = System.nanoTime()-time;
        if(elapsed > timeToDisplayAnimation){
            currentImage = images.get(GamePanel.NORMAL);
        }
    }

    public void drawMovedBy(Canvas canvas, int moveBy) {
        if(currentImage != images.get(GamePanel.NORMAL)){
            resetImageAfterTime();
        }
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        int alpha = (-moveBy/currentImage.getHeight())* GREY_STEP;
        paint.setAlpha(255 - alpha);
        y = -moveBy %currentImage.getHeight();
        canvas.drawBitmap(currentImage, x, y, paint);

        paint.setAlpha(255 - (alpha + GREY_STEP));
        if (y > 0) {
            canvas.drawBitmap(currentImage, x, y-currentImage.getHeight(), paint);
        }
    }

    public void setCurrentImage(String image){
        if(images.containsKey(image)){
//            currentImage = images.get(image);
            time = System.nanoTime();
        }

    }
}
