package com.max.jumpingapp.tutorial.tutorialPlayer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import com.max.jumpingapp.game.FingerReleasedEvent;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.player.PlayerPower;
import com.max.jumpingapp.objects.player.PlayerStatusFreeFalling;
import com.max.jumpingapp.objects.visuals.PlayerObject;
import com.max.jumpingapp.tutorial.TutorialGamePanel;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.Width;
import com.max.jumpingapp.types.XCenter;
import com.max.jumpingapp.types.XPosition;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayer extends Player {
    private static TutorialPlayer mePlayer;

    private TutorialPlayer(XCenter playerXCenter, Width playerWidth, Bitmap playerImage) {
        maxHeight = Math.abs(com.max.jumpingapp.game.GamePanel.HEIGHT_POS);
        this.playerObject = new PlayerObject(playerXCenter, playerWidth, com.max.jumpingapp.game.GamePanel.HEIGHT_POS, playerImage);

        this.curHeight = new Height();

        Paint defaultPaint = new Paint();
        defaultPaint.setColor(Color.CYAN);

        xPosition = new XPosition();

        this.playerPower = new TutorialPlayerPower();
        this.playerStatus = new TutorialPlayerStatusFreeFalling(maxHeight);
        EventBus.getDefault().register(playerPower);
    }

    public static TutorialPlayer createTutorialPlayer(XCenter playerXCenter, Width playerWidth, Bitmap playerImage) {
        mePlayer = new TutorialPlayer(playerXCenter, playerWidth, playerImage);
        return mePlayer;
    }

    public static TutorialPlayer getTutorialPlayer() {
        return mePlayer;
    }

    @Override
    public void onEvent(FingerReleasedEvent event) {
        super.onEvent(event);
    }

    public void unPause(double oscPeriod) {
        playerPower.unPause(oscPeriod);
    }

    public void pause() {
        playerPower.pause();
    }
}
