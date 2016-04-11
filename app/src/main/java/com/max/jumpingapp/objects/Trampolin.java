package com.max.jumpingapp.objects;

import android.graphics.Canvas;

import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.visuals.Background;
import com.max.jumpingapp.objects.visuals.TrampolinVisual;
import com.max.jumpingapp.types.Width;
import com.max.jumpingapp.types.XCenter;

/**
 * Created by normal on 29.08.2015.
 */
public class Trampolin {
    private double Springconst;
    private int xPos;
    private int width;
    private TrampolinVisual trampolinVisual;
//    private Paint paint;

    public Trampolin(XCenter xCenter, Width width) {
        xPos = width.getLeftWithCenter(xCenter);
        this.width = width.getValue();
        this.Springconst = (double) com.max.jumpingapp.game.GamePanel.SPRINGCONST;
        this.trampolinVisual = new TrampolinVisual();
    }

    public int draw(Canvas canvas, Player player, Background shape) {
        return trampolinVisual.draw(canvas, player, shape, xPos, width);
    }

    public boolean supportingPlayer(Player player) {
        return (this.xPos <= player.getLeft() && (this.xPos+width) >= player.getRight() && player.getLeft() < player.getRight());
    }
}
