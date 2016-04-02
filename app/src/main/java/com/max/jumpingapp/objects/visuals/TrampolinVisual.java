package com.max.jumpingapp.objects.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.max.jumpingapp.GamePanel;
import com.max.jumpingapp.objects.player.Player;

/**
 * Created by max on 4/2/2016.
 */
public class TrampolinVisual {
    private Paint paint;

    public TrampolinVisual(Paint paint){
        this.paint = paint;
        if(this.paint == null){
            this.paint = new Paint();
            this.paint.setColor(Color.YELLOW);
            this.paint.setStrokeWidth(2);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setAntiAlias(true);
        }
    }

    public int draw(Canvas canvas, Player player, Background shape, int trampXPos, int trampWidth) {
        int moveBy = player.draw(canvas, shape);
        Path myPath = trampCurve(player.getLeft(), player.getRight(), player.getBottom(), trampXPos, trampWidth);
        myPath.offset(0, -moveBy);
        canvas.drawPath(myPath, paint);
        return moveBy;
    }

    private Path trampCurve(float playerLeft, float playerRight, float playerBottom, int trampXPos, int trampWidth) {
        Path myPath = new Path();
        int trampYPos = GamePanel.heightNill;
        myPath.moveTo(trampXPos, trampYPos);
        float xCenter = (playerLeft+playerRight)/2;
        if (playerBottom > trampYPos) {
            myPath.quadTo(playerLeft - 0.2F * (playerLeft - trampXPos), trampYPos + 0.2F * (playerBottom - trampYPos), playerLeft, playerBottom);
            myPath.quadTo(xCenter, playerBottom + (playerBottom - trampYPos) * 0.3F, playerRight, playerBottom);
        }
        myPath.quadTo(playerRight + 0.2F * (trampXPos + trampWidth - (playerRight)), trampYPos, trampXPos + trampWidth, trampYPos);
        return myPath;
    }
}
