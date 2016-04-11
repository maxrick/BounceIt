package com.max.jumpingapp.game;

import android.graphics.Canvas;

import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.objects.Wind;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.visuals.Background;
import com.max.jumpingapp.objects.visuals.DrawObjects;
import com.max.jumpingapp.types.ScoreBoardData;

/**
 * Created by max on 4/2/2016.
 */
public class Game {
    private Background bg;
    private Trampolin trampolin;
    private Player player;
    private Wind wind;
    private DrawObjects objects;

    public Game(Background background, Trampolin trampolin, Player player, int[] highScores) {
        this.bg = background;
        this.trampolin = trampolin;
        this.player = player;
        wind = new Wind();
        objects = new DrawObjects(highScores);
    }

    public void setNewPlayer(Player newPlayer) {
        this.player = newPlayer;
    }

    public void draw(Canvas canvas) {
        int moveBy = trampolin.draw(canvas, player, bg);
        objects.draw(canvas, moveBy);
    }

    public void update(long timeMikro, boolean touching) throws PlayerDiedException {
        ScoreBoardData data = player.updatePosition(trampolin, wind);
        objects.update(data);
//        try {
//            player.updatePosition(blaetter, trampolin, wind);
//        } catch (PlayerDiedException e) {
//            System.out.println("Player died with height :"+ +e.height);
//            this.player = GamePanel.createPlayer(e.playerObject.image);
//        }
        player.updatePower(touching, trampolin, timeMikro);
    }

    public void swiped(int xSwipedToRight) {
        player.xAccel(xSwipedToRight);
    }

}
