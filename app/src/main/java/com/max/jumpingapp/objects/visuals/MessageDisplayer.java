package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.max.jumpingapp.events.DrawFingerSwipeEvent;
import com.max.jumpingapp.events.DrawableEvent;
import com.max.jumpingapp.events.HelpInstructionEvent;
import com.max.jumpingapp.events.JumpMissedEvent;
import com.max.jumpingapp.util.MathHelper;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by max on 4/10/2016.
 */
public class MessageDisplayer {
    private Paint paint;
    private ArrayList<DrawableEvent> events;

    public MessageDisplayer(){
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(MathHelper.adjustToScreensize(40));
        events = new ArrayList<DrawableEvent>();
    }

    public void onEvent(JumpMissedEvent event){
        event.setStartTime();
        this.events.add(event);
        //this.message = new ScreenMessage(Constants.JUMP_MISSED + "\n"+Constants.RELEASE_EARLIER,xPos,yPos);
    }

    public void onEvent(HelpInstructionEvent event){
        stopSwipe();
        event.setStartTime();
        this.events.add(event);
    }

    private void stopSwipe() {
        for (Iterator<DrawableEvent> iterator = events.iterator(); iterator.hasNext();) {
            DrawableEvent event = iterator.next();
            if(!event.equals(null) && event.getClass().getSuperclass()==DrawFingerSwipeEvent.class) {
                    iterator.remove();
            }
        }
    }

    public void onEvent(DrawFingerSwipeEvent event){
        boolean containsSwipe = false;
        for(DrawableEvent e : events){
            if(e.getClass().getSuperclass() == DrawFingerSwipeEvent.class){
                containsSwipe=true;
            }
        }
        if(!containsSwipe) {
            event.setStartTime();
            this.events.add(event);
        }
    }

    public void draw(Canvas canvas) {
        for (Iterator<DrawableEvent> iterator = events.iterator(); iterator.hasNext();) {
            DrawableEvent event = iterator.next();
            if(!event.equals(null)) {
                if (event.stillActive()) {
                    event.draw(canvas);
                } else {
                    iterator.remove();
                }
            }
        }
            //drawMessage(canvas);
//            canvas.drawText(Constants.JUMP_MISSED , xPos, yPos, paint);
//            canvas.drawText(Constants.RELEASE_EARLIER , xPos, yPos+60, paint);
    }
//    private void drawMessage(Canvas canvas) {
//            drawMultiline(message.getMessage(), message.getxPos(), message.getyPos(), paint, canvas);
//    }
//
//    public void drawMultiline(String str, int x, int y, Paint paint, Canvas canvas)
//    {
//        for (String line: str.split("\n"))
//        {
//            canvas.drawText(line, x, y, paint);
//            y += -paint.ascent() + paint.descent();
//        }
//    }
}
