package com.max.jumpingapp.objects;

import android.graphics.Canvas;

import com.max.jumpingapp.GamePanel;
import com.max.jumpingapp.PlayerDiedException;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.visuals.Background;

/**
 * Created by max on 4/2/2016.
 */
public class Game {
    private Background bg;
    private Trampolin trampolin;
    private Player player;
    private Blaetter blaetter;
    private Wind wind;

    public Game(Background background, Trampolin trampolin, Player player) {
        this.bg = background;
        this.trampolin = trampolin;
        this.player = player;
        blaetter = new Blaetter();
        wind = new Wind();
    }

    public void setNewPlayer(Player newPlayer) {
        this.player = newPlayer;
    }

    public void draw(Canvas canvas) {
        int moveBy = trampolin.draw(canvas, player, bg);
        blaetter.draw(canvas, moveBy);
    }

    public void update(long timeMikro, boolean touching) {
        try {
            player.updatePosition(blaetter, trampolin, wind);
        } catch (PlayerDiedException e) {
            System.out.println("Player died with height :"+ +e.height);
            this.player = GamePanel.createPlayer(e.playerObject.image);
        }
        player.updatePower(touching, trampolin, timeMikro);
    }

    private void upgradeTrampolin() {
        this.trampolin = trampolin.upgrade(10);
        player.trampolinChanged(trampolin);
    }

    public void swiped(int xSwipedToRight) {
        player.removeBlaetter(blaetter);
        player.xAccel(xSwipedToRight);
    }

}
