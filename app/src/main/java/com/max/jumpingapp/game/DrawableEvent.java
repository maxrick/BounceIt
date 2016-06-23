package com.max.jumpingapp.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.max.jumpingapp.util.MathHelper;

/**
 * Created by Max Rickayzen on 23.06.2016.
 */
public abstract class DrawableEvent {
    protected Paint paint;
    protected long timer;
    protected long timeToDisplay;

    public DrawableEvent(long timeToDisplay){
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(MathHelper.adjustToScreensize(40));
        this.timeToDisplay = timeToDisplay;
        timer = System.nanoTime();
    }
    public abstract void draw(Canvas canvas);

    protected void drawMultiline(String str, int x, int y, Paint paint, Canvas canvas)
    {
        for (String line: str.split("\n"))
        {
            canvas.drawText(line, x, y, paint);
            y += -paint.ascent() + paint.descent();
        }
    }

    public void setStartTime() {
        timer = System.nanoTime();
    }

    public boolean stillActive() {
        long elapsed = System.nanoTime() - timer;
        return elapsed<timeToDisplay;
    }
}
