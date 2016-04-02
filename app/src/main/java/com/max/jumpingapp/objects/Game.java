package com.max.jumpingapp.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.max.jumpingapp.GamePanel;
import com.max.jumpingapp.PlayerDiedException;
import com.max.jumpingapp.R;
import com.max.jumpingapp.Wind;

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
        int moveBy = getTrampolin().draw(canvas, player, bg);
        blaetter.draw(canvas, moveBy);
    }

    public void update(long timeMikro, boolean touching) {
        try {
            player.updatePosition(blaetter, getTrampolin(), wind);
        } catch (PlayerDiedException e) {
            System.out.println("Player died with height :"+ +e.height);
            this.player = GamePanel.createPlayer(e.playerObject.image);
        }
        player.updatePower(touching, getTrampolin(), timeMikro);
    }

    private void upgradeTrampolin() {
        this.trampolin = getTrampolin().upgrade(10);
        player.trampolinChanged(getTrampolin());
    }

    public void swiped(int xSwipedToRight) {
        player.removeBlaetter(blaetter);
        player.xAccel(xSwipedToRight);
    }

    public Trampolin getTrampolin() {
        return trampolin;
    }

}
