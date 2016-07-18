package com.max.jumpingapp.game;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.visuals.Background;
import com.max.jumpingapp.objects.visuals.DrawObjects;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.Score;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/2/2016.
 */
public class Game {
    private Background bg;
    private Trampolin trampolin;
    private Player player;
    private Score score;
    private DrawObjects objects;

    public Game(Background background, Trampolin trampolin, Player player, int[] highScores, Resources resources) {
        this.bg = background;
        score = new Score(0);
        this.trampolin = trampolin;
        this.player = player;
        EventBus.getDefault().register(player);
        objects = new DrawObjects(highScores, resources);
    }

    public void draw(Canvas canvas) {
        int moveBy = trampolin.draw(canvas, player, bg);
        objects.draw(canvas, moveBy);
    }

    public void update(long timeMikro, boolean touching) throws PlayerDiedException {
        try {
            Height currentHeight = player.updatePosition(trampolin);
            score = score.update(currentHeight);
            objects.update(currentHeight, score);
            player.updatePower(touching, trampolin, timeMikro);
        }catch (PlayerDiedException e){
            throw new PlayerDiedException(e.player, score);
        }
    }

    public void swiped(int xSwipedToRight) {
        player.xAccel(xSwipedToRight);
    }

    public void unregisterEventlisteners() {
        EventBus.getDefault().unregister(player);
        player.unregisterEventlisteners();
        objects.unregisterEventlisteners();

    }
}
