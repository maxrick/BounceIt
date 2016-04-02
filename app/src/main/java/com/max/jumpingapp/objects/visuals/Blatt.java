package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.max.jumpingapp.objects.player.PlayerStatus;

/**
 * Created by normal on 19.09.2015.
 */
public class Blatt {
    private double height;
    private Paint paint;

    public Blatt(int height) {
        this.height = height;
        paint = new Paint();
        paint.setColor(Color.GREEN);
    }

    public int update(int top) {
        int touchingPlayer = 0;
        height += (0.005 * PlayerStatus.gravitaion);
        if (top < height) {
            height = top;
            touchingPlayer = 1;
        }
        return touchingPlayer;
    }

    public void draw(Canvas canvas, int moveBy) {
        canvas.drawLine(200, (int) (height - moveBy), 600, (int) (height - moveBy), paint);
    }

    public boolean touching(int top) {
        return (Math.abs(height-top)<20);
    }
}

