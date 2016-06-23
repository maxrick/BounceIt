package com.max.jumpingapp.game;

import android.graphics.Canvas;

import com.max.jumpingapp.tutorial.ScreenMessage;
import com.max.jumpingapp.util.Constants;
import com.max.jumpingapp.util.MathHelper;

/**
 * Created by max on 4/11/2016.
 */
public class JumpMissedEvent extends DrawableEvent{
    public JumpMissedEvent() {
        super(2000000000);
        paint.setTextSize(MathHelper.adjustToScreensize(60));
    }

    @Override
    public void draw(Canvas canvas) {
        int xPos = MathHelper.adjustToScreensize(40);
        int yPos = GamePanel.screenHeight/5;
        drawMultiline(Constants.JUMP_MISSED + "\n"+Constants.RELEASE_EARLIER,xPos,yPos, paint, canvas);
    }
}
