package com.max.jumpingapp.objects;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.max.jumpingapp.GamePanel;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.visuals.Background;
import com.max.jumpingapp.objects.visuals.PlayerObject;
import com.max.jumpingapp.objects.visuals.TrampolinVisual;

/**
 * Created by normal on 29.08.2015.
 */
public class Trampolin {
    private double Springconst;
    private int xPos;
    private int width;
    private TrampolinVisual trampolinVisual;
//    private Paint paint;

    public Trampolin(int xCenter, int width, double springconst) {
        this(xCenter,width,springconst,null);
    }

    public Trampolin(int xCenter, int width, double springconst, Paint paint) {
        xPos = xCenter - width/2;
        this.width = width;
        this.Springconst = springconst;
        this.trampolinVisual = new TrampolinVisual(paint);
    }

    public Trampolin upgrade(double increaseSpringConst){
        return new Trampolin(this.xPos, this.width, this.Springconst + increaseSpringConst);//was yPos instead of yPos
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

    public boolean supportingPlayer(PlayerObject playerObject) {
        return (this.xPos <= playerObject.rect.left && (this.xPos+width) >= playerObject.rect.right && playerObject.rect.left < playerObject.rect.right);
    }
}
