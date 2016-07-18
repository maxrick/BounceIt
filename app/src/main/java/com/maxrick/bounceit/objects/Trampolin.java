package com.maxrick.bounceit.objects;

import android.graphics.Canvas;

import com.maxrick.bounceit.game.GamePanel;
import com.maxrick.bounceit.objects.player.Player;
import com.maxrick.bounceit.objects.visuals.Background;
import com.maxrick.bounceit.objects.visuals.TrampolinVisual;
import com.maxrick.bounceit.types.Width;
import com.maxrick.bounceit.types.XCenter;

/**
 * Created by normal on 29.08.2015.
 */
public class Trampolin {
    private double Springconst;
    private int xPos;
    private int width;
    private TrampolinVisual trampolinVisual;

    public Trampolin(XCenter xCenter, Width width) {
        xPos = width.getLeftWithCenter(xCenter);
        this.width = width.getValue();
        this.Springconst = (double) GamePanel.SPRINGCONST;
        this.trampolinVisual = new TrampolinVisual();
    }

    public int draw(Canvas canvas, Player player, Background shape) {
        return trampolinVisual.draw(canvas, player, shape, xPos, width);
    }

    public boolean supportingPlayer(Player player) {
        return (this.xPos <= player.getLeft() && (this.xPos+width) >= player.getRight() && player.getLeft() < player.getRight());
    }
}
