package com.max.jumpingapp.objects.visuals;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.objects.Wind;
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
    private boolean missedJump;
    private Animator animator;

    public PlayerObject(XCenter xCenter, Width width, int yPositionBottom, Bitmap res) {
        image = Bitmap.createBitmap(res);
        this.rect = new Rect(width.getLeftWithCenter(xCenter), GamePanel.HEIGHT_NILL - yPositionBottom - FORM_HEIGHT, width.getRighWithCenter(xCenter), GamePanel.HEIGHT_NILL - yPositionBottom);
        normalHeightRect = rect.height();
        missedJump = false;
        minHeightRect = 4*normalHeightRect/10;
        maxHeightRect = 3*normalHeightRect/2;
        paint = new Paint();
        paint.setColor(Color.RED);
        animator = new Animator(new Paint());
//        image = Bitmap.createScaledBitmap(res, rect.width(), rect.height()/2, false);
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
        if (crouch(touching)) {
            rect.top += 5;
        }
        if (strech(touching)) {
            if (!animateStrech) {
                animateStrech = true;
                maxHeightRect = (2 * normalHeightRect - rect.height());
            }
            rect.top -= 25;
            if (rect.height() > maxHeightRect) {
                animateStrech = false;
            }
        }
        if (backToNormalSize(touching)) {
            rect.top += 20;
        } else if (!touching && !animateStrech && rect.height()  > normalHeightRect) {
            rect.top += rect.height()-normalHeightRect;
        }
    }

    private boolean backToNormalSize(boolean touching) {
        return !touching && !animateStrech && rect.height()  > normalHeightRect+10;
    }

    private boolean strech(boolean touching) {
        return !touching && ((rect.height() +10< normalHeightRect) || animateStrech);
    }

    private boolean crouch(boolean touching) {
        return touching && (rect.height() ) >= minHeightRect;
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setRect(Height curHeight, XPosition xAdjust) {
        rect.set(xAdjust.adjusted(rect.left), curHeight.topOfRect(rect), xAdjust.adjusted(rect.right), curHeight.bottomOfRect());
    }

    public int draw(Canvas canvas, Background shape) {
        int moveBy = 0;
        int GAPTOP = 200;
        if (rect.top - GAPTOP < 0) {
            moveBy = rect.top - GAPTOP;
        }
        if (rect.bottom > GamePanel.screenHeight) {
            moveBy = rect.bottom - GamePanel.screenHeight;
        }
        shape.drawMovedBy(canvas, moveBy);
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
            canvas.drawBitmap(image, null, secondRect, animator.adjustedPaint());
        }

        canvas.drawBitmap(image, null, r, animator.adjustedPaint());

        return moveBy;
    }

    public double getHeight() {
        return GamePanel.HEIGHT_NILL - rect.bottom;
    }

    public void setMissedJump(boolean missedJump) {
        this.missedJump = missedJump;
    }

    public void setPaint(double percentage, Wind wind) {
        animator.animate(percentage, wind);
    }

    public float getLeft() {
        return  (rect.left + 0.3F*rect.width());
    }

    public float getRight() {
        return (rect.right - 0.2F*rect.width());
    }

    public float getBottom() {
        return rect.bottom;
    }
}
