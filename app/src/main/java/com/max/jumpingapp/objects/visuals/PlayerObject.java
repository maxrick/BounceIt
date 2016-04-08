package com.max.jumpingapp.objects.visuals;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;

import com.max.jumpingapp.GamePanel;
import com.max.jumpingapp.objects.Blaetter;
import com.max.jumpingapp.types.XPosition;

/**
 * Created by normal on 24.10.2015.
 */
public class PlayerObject {
    public final Rect rect;
    private final int normalHeightRect;
    private final int minHeightRect;
    private int maxHeightRect;
    private final Paint paint;
    private boolean animateStrech;
    private final int GAPTOP = 200;
    public Bitmap image;
    private Paint imagePaint;
    private static final long timeToDisplayAnimation = 1000000000; //1 Second
    private long time;

    public PlayerObject(int xCenter, int width, int formHeight, int yPositionBottom, Bitmap res) {
        this.rect = new Rect(xCenter -width/2, GamePanel.heightNill - yPositionBottom - formHeight, xCenter + width/2, GamePanel.heightNill - yPositionBottom);
        normalHeightRect = rect.height();
        minHeightRect = 4*normalHeightRect/10;
        maxHeightRect = 3*normalHeightRect/2;
        paint = new Paint();
        paint.setColor(Color.RED);
        imagePaint = new Paint();
        //image = Bitmap.createScaledBitmap(res, rect.width(), rect.height(), false);
        image = Bitmap.createBitmap(res);
    }

    public void animate(boolean touching) {
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

    public void setRect(int curHeight, XPosition xAdjust) {
        int rectHeight = rect.bottom - rect.top;
        rect.set(xAdjust.adjusted(rect.left), GamePanel.heightNill - curHeight - rectHeight, xAdjust.adjusted(rect.right), GamePanel.heightNill - curHeight);
//        rect.set(rect.left, GamePanel.heightNill - curHeight - rectHeight, rect.right, GamePanel.heightNill - curHeight);
    }

    public void addBlattTo(Blaetter blaetter) {
        blaetter.newBlatt(rect.top - GamePanel.screenHeight);
    }

    public int getTouchingBlaetter(Blaetter blaetter) {
        int touchingBlaeter = blaetter.update(rect.top);
        return touchingBlaeter;
    }

    public int draw(Canvas canvas, Background shape) {
        resetImageAfterTime();
        int moveBy = 0;
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

    public void removeTouchingLeaves(Blaetter blaetter) {
        blaetter.removeTouching(rect.top);
    }

    public double getHeight() {
        return GamePanel.heightNill - rect.bottom;
    }
}
