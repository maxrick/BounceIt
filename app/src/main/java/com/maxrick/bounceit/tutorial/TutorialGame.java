package com.maxrick.bounceit.tutorial;

import android.content.res.Resources;

import com.maxrick.bounceit.game.Game;
import com.maxrick.bounceit.game.PlayerDiedException;
import com.maxrick.bounceit.objects.Trampolin;
import com.maxrick.bounceit.objects.player.Player;
import com.maxrick.bounceit.objects.visuals.Background;

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
