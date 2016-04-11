package com.max.jumpingapp.objects.visuals;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.Width;
import com.max.jumpingapp.types.XCenter;
import com.max.jumpingapp.types.XPosition;

/**
 * Created by normal on 24.10.2015.
 */
public class PlayerObject {
    public final Rect rect;
    public static final int FORM_HEIGHT= 200;
    public static int normalHeightRect=0;//@// TODO: 4/10/2016 do better
    private final int minHeightRect;
    private int maxHeightRect;
    private final Paint paint;
    private boolean animateStrech;
    public Bitmap image;
    private Paint imagePaint;
    private static final long timeToDisplayAnimation = 1000000000; //1 Second
    private long time;
    private boolean missedJump;

    public PlayerObject(XCenter xCenter, Width width, int yPositionBottom, Bitmap res) {
        this.rect = new Rect(width.getLeftWithCenter(xCenter), GamePanel.HEIGHT_NILL - yPositionBottom - FORM_HEIGHT, width.getRighWithCenter(xCenter), GamePanel.HEIGHT_NILL - yPositionBottom);
        normalHeightRect = rect.height();
        missedJump = false;
        minHeightRect = 4*normalHeightRect/10;
        maxHeightRect = 3*normalHeightRect/2;
        paint = new Paint();
        paint.setColor(Color.RED);
        imagePaint = new Paint();
        //image = Bitmap.createScaledBitmap(res, rect.width(), rect.height(), false);
        image = Bitmap.createBitmap(res);
    }

    public void animate(boolean touching) {
        if(!missedJump){
            animateStrech(touching);
        }else {
            animateNoStrech();
        }
    }

    private void animateNoStrech() {
        if (rect.height() +10< normalHeightRect) {
            rect.top -= 15;
        }else {
            setMissedJump(false);
        }
    }

    private void animateStrech(boolean touching) {
        if (touching && (rect.height() ) >= minHeightRect) {
            rect.top += 5;
        }
        if (!touching && ((rect.height() +10< normalHeightRect) || animateStrech)) {
            if (!animateStrech) {
                animateStrech = true;
                maxHeightRect = (2 * normalHeightRect - rect.height());
            }
            rect.top -= 15;
            if (rect.height() > maxHeightRect) {
                animateStrech = false;
            }
        }
        if (!touching && !animateStrech && rect.height() - 15 > normalHeightRect) {
            rect.top += 10;
        }
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setRect(Height curHeight, XPosition xAdjust) {
        int rectHeight = rect.bottom - rect.top;
        rect.set(xAdjust.adjusted(rect.left), curHeight.topOfRect(rect), xAdjust.adjusted(rect.right), curHeight.bottomOfRect());
//        rect.set(rect.left, GamePanel.HEIGHT_NILL - curHeight - rectHeight, rect.right, GamePanel.HEIGHT_NILL - curHeight);
    }

    public int draw(Canvas canvas, Background shape) {
        resetImageAfterTime();
        int moveBy = 0;
        int GAPTOP = 200;
        if (rect.top - GAPTOP < 0) {
            moveBy = rect.top - GAPTOP;
        }
        if (rect.bottom > GamePanel.screenHeight) {
            moveBy = rect.bottom - GamePanel.screenHeight;
        }
        shape.drawMovedBy(canvas, moveBy);
//        shape.setBounds(0, -1000, screenWidth, screenheight + 200 - moveBy / 2);
//        shape.draw(canvas);
        Rect r = new Rect(rect);
        r.offset(0, -moveBy);
        if(r.left > r.right){
            Rect secondRect;
            if(r.right < 0){
                r.right += GamePanel.screenWidth;
                secondRect = new Rect(r);
                secondRect.offset(-GamePanel.screenWidth, 0);
            }else {
                r.left -= GamePanel.screenWidth;
                secondRect = new Rect(r);
                secondRect.offset(GamePanel.screenWidth, 0);
            }
            canvas.drawBitmap(image, null, secondRect, imagePaint);
        }
        //canvas.drawRect(r, paint);

        canvas.drawBitmap(image, null, r, imagePaint);

        return moveBy;
    }

    public void setPlayerColor(int color){
        imagePaint.setColorFilter(new LightingColorFilter(Color.rgb(0, 0, 0), color));
        time = System.nanoTime();
    }
    private void resetImageAfterTime() {
        long elapsed = System.nanoTime()-time;
        if(elapsed > timeToDisplayAnimation){
            imagePaint = new Paint();
        }
    }

    public double getHeight() {
        return GamePanel.HEIGHT_NILL - rect.bottom;
    }

    public void setMissedJump(boolean missedJump) {
        this.missedJump = missedJump;
    }
}
