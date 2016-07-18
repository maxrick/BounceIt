package com.maxrick.bounceit.tutorial.tutorialPlayer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import com.maxrick.bounceit.game.GamePanel;
import com.maxrick.bounceit.objects.player.Player;
import com.maxrick.bounceit.objects.visuals.PlayerObject;
import com.maxrick.bounceit.tutorial.TutorialWind;
import com.maxrick.bounceit.types.Height;
import com.maxrick.bounceit.types.Width;
import com.maxrick.bounceit.types.XCenter;
import com.maxrick.bounceit.types.XPosition;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayer extends Player {
    private static TutorialPlayer mePlayer;

    private TutorialPlayer(XCenter playerXCenter, Width playerWidth, Bitmap playerImage, float footFromLeft, float footFromRight) {
        maxHeight = Math.abs(GamePanel.HEIGHT_POS);

        this.curHeight = new Height();
        this.wind = new TutorialWind();

        Paint defaultPaint = new Paint();
        defaultPaint.setColor(Color.CYAN);

        xPosition = new XPosition();

        this.playerPower = new TutorialPlayerPower();
        PlayerObject playerObject = new PlayerObject(playerXCenter, playerWidth, GamePanel.HEIGHT_POS, playerImage, footFromLeft, footFromRight);
        this.playerStatus = new TutorialPlayerStatusFreeFalling(maxHeight, playerObject);
        EventBus.getDefault().register(playerPower);
    }

    public static TutorialPlayer createTutorialPlayer(XCenter playerXCenter, Width playerWidth, Bitmap playerImage, float footFromLeft, float footFromRight) {
        mePlayer = new TutorialPlayer(playerXCenter, playerWidth, playerImage, footFromLeft, footFromRight);
        return mePlayer;
    }

    public static TutorialPlayer getTutorialPlayer() {
        return mePlayer;
    }


    public void unPause(double oscPeriod) {
        playerPower.unPause(oscPeriod);
    }

    public void pause() {
        playerPower.pause();
    }

    @Override
    public void unregisterEventlisteners() {
        super.unregisterEventlisteners();
        EventBus.getDefault().unregister(playerStatus);
    }
}
