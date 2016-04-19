package com.max.jumpingapp.tutorial.tutorialPlayer;

import android.graphics.Bitmap;

import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.types.Width;
import com.max.jumpingapp.types.XCenter;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayer extends Player {
    public TutorialPlayer(XCenter playerXCenter, Width playerWidth, Bitmap playerImage) {
        super(playerXCenter, playerWidth, playerImage);
        this.playerStatus = new TutorialPlayerStatusFreeFalling(maxHeight);
    }
}
