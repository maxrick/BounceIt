package com.max.jumpingapp.tutorial;

import android.content.res.Resources;

import com.max.jumpingapp.game.Game;
import com.max.jumpingapp.game.PlayerDiedException;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.visuals.Background;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialGame extends Game {
    public TutorialGame(Background background, Trampolin trampolin, Player player, int[] highScores, Resources resources) {
        super(background, trampolin, player, highScores, resources);
    }

    @Override
    public void update(long timeMikro, boolean touching) throws PlayerDiedException {
        super.update(timeMikro, touching);
        TutorialGamePanel.gamePaused = false;
    }
}
