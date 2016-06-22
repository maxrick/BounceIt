package com.max.jumpingapp.objects.visuals;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

import com.max.jumpingapp.R;
import com.max.jumpingapp.game.GamePanel;

/**
 * Created by normal on 29.08.2015.
 */
public class Background {
    public static final int GREY_STEP = 60;
    private Bitmap image;
    private Bitmap stars;
    private int x, y;

    public Background(Bitmap res, Bitmap noChange) {
        image = res;
        this.stars = noChange;
        double scaleFactor = GamePanel.screenWidth*1.0 / res.getWidth();
        image = Bitmap.createScaledBitmap(res, GamePanel.screenWidth, (int)( res.getHeight()*scaleFactor), false); // TODO: 4/11/2016 scale image
        y = 0;
        x = 0;
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
//        int alpha = (-moveBy/image.getHeight())* GREY_STEP;
//        paint.setAlpha(255 - alpha);
        y = -moveBy %image.getHeight();
        int stage = (-moveBy/image.getHeight());
        if(stage>0){
            double greenMulDuble = Math.pow(((178.d * 255.d) / 238.d) / 255.d, stage);
            int greenMul = (int) (greenMulDuble *255);
            double blueMulDouble = Math.pow(((178.d * 255.d) / 236.d) / 255.d, stage);
            int blueMul = (int) (blueMulDouble *255);
            paint.setColorFilter(new LightingColorFilter(Color.rgb(0, greenMul, blueMul),Color.rgb(33,0,0)));
        }
        canvas.drawBitmap(image, x, y, paint);
        canvas.drawBitmap(stars,x,y, new Paint());

//        paint.setAlpha(255 - (alpha + GREY_STEP));
        if (y > 0) {
            double greenMulDuble = Math.pow(((178.d * 255.d) / 238.d) / 255.d, stage + 1);
            int greenMul = (int) (greenMulDuble *255);
            double blueMulDouble = Math.pow(((178.d * 255.d) / 236.d) / 255.d, stage + 1);
            int blueMul = (int) (blueMulDouble *255);
            paint.setColorFilter(new LightingColorFilter(Color.rgb(0, greenMul, blueMul),Color.rgb(33,0,0)));
            canvas.drawBitmap(image, x, y-image.getHeight(), paint);
            canvas.drawBitmap(stars,x,y-image.getHeight(), new Paint());
        }
    }

}
