package com.max.jumpingapp.tutorial;

import com.max.jumpingapp.game.Game;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.visuals.Background;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialGame extends Game {
    public TutorialGame(Background background, Trampolin trampolin, Player player, int[] highScores) {
        super(background, trampolin, player, highScores);
    }
}
