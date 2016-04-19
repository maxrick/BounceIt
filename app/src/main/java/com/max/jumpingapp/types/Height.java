package com.max.jumpingapp.types;

import android.graphics.Rect;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.objects.visuals.PlayerObject;

/**
 * Created by max on 4/11/2016.
 */
public class Height {
    private final int height;

    public Height(int height){
        this.height = height;
    }

    public Height() {
        this(0);
    }

    public int topOfRect(Rect rect) {
        int rectHeight = rect.bottom - rect.top;
        return bottomOfRect() - rectHeight;
    }

    public int bottomOfRect() {
        return GamePanel.HEIGHT_NILL - height;
    }

    public boolean isPositive() {
        return height>0;
    }

    public int devideBy(int devider) {
        return height/devider;
    }

    @Override
    public String toString() {
        return "Height: "+height;
    }

    public Height setNewMaximum(Height currentHeight) {//@// TODO: 4/11/2016 needs better name
        return new Height(currentHeight.maximumOf(height));
    }

    private int maximumOf(int height) {
        return Math.max(this.height, height);
    }

    public float calculateDrawHeight(int moveBy) {
        return (GamePanel.HEIGHT_NILL - height -moveBy - PlayerObject.normalHeightRect);
    }

    public int maxOf(int score) {
        return Math.max(height, score);
    }

    public String cappedToString() {
        if(this.isPositive()){
            return toString();
        }
        return "Height: 0";
    }
}
