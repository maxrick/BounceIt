package com.max.jumpingapp.objects;

import android.graphics.Canvas;
import android.graphics.Paint;

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

    public Trampolin(XCenter xCenter, Width width, double springconst) {
        xPos = width.getLeftWithCenter(xCenter);
        this.width = width.getValue();
        this.Springconst = springconst;
        this.trampolinVisual = new TrampolinVisual();
    }

    public int draw(Canvas canvas, Player player, Background shape) {
        return trampolinVisual.draw(canvas, player, shape, xPos, width);
    }

    public double getSpringconst(){
        return Springconst;
    }

    public double calcAccelerationFactor(double maxHeight) {
        double result = 10 - Springconst/10 + Math.pow(Springconst, 1.2)/(100*Springconst);//without *maxHeight, gets to exponential
        return result;//better formular needed
    }//Math.abs(playerPower)*maxHeight/100;

    public boolean supportingPlayer(Player player) {
        return (this.xPos <= player.getLeft() && (this.xPos+width) >= player.getRight() && player.getLeft() < player.getRight());
    }
}
