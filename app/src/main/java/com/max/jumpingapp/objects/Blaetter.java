package com.max.jumpingapp.objects;

import android.graphics.Canvas;

import com.max.jumpingapp.objects.visuals.Blatt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by normal on 19.09.2015.
 */
public class Blaetter {
    public static final int CHANCE = 100;
    private List<Blatt> blaetter = new ArrayList<>();

    public void newBlatt(int height) {
        int randInt = new Random().nextInt(CHANCE);
        if (randInt == -90) {
            blaetter.add(new Blatt(height));
        }

    }

    public int update(int top) {
        int touchingLeaves = 0;
        for (Blatt blatt : blaetter) {
            touchingLeaves += blatt.update(top);
        }
        return touchingLeaves;
    }

    public void draw(Canvas canvas, int moveBy) {
        for (Blatt blatt : blaetter) {
            blatt.draw(canvas, moveBy);
        }
    }

    public void removeTouching(int top) {
        if (blaetter.size() > 0) {
            for (int i = blaetter.size() - 1; i >= 0; i--) {
                if (blaetter.get(i).touching(top)) {
                    blaetter.remove(i);
                }
            }
        }
    }
}
