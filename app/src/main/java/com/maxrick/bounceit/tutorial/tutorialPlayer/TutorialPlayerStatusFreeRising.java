package com.maxrick.bounceit.tutorial.tutorialPlayer;

import com.maxrick.bounceit.game.PlayerDiedException;
import com.maxrick.bounceit.objects.Trampolin;
import com.maxrick.bounceit.objects.player.Player;
import com.maxrick.bounceit.objects.player.PlayerPower;
import com.maxrick.bounceit.objects.visuals.PlayerObject;
import com.maxrick.bounceit.objects.visuals.PowerDisplay;
import com.maxrick.bounceit.types.Height;
import com.maxrick.bounceit.types.XPosition;
import com.maxrick.bounceit.util.Constants;
import com.maxrick.bounceit.game.GamePanel;
import com.maxrick.bounceit.objects.player.PlayerStatusFreeRising;
import com.maxrick.bounceit.tutorial.FingerTouchingEvent;
import com.maxrick.bounceit.tutorial.StopPlayerStatusInAir;
import com.maxrick.bounceit.tutorial.StopPlayerTouchingTrampolinEvent;
import com.maxrick.bounceit.tutorial.TutorialGamePanel;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayerStatusFreeRising extends PlayerStatusFreeRising {
    private double percentagePassedBeforeStop=0;
    private boolean hadStoppedForPowerDisplay=false;

    public TutorialPlayerStatusFreeRising(double oscPeriod, double fallPeriod, PlayerObject playerObject) {
        super(oscPeriod, fallPeriod, playerObject);
        EventBus.getDefault().register(this);//@// TODO: 5/19/2016 not good
    }

    public void onEvent(FingerTouchingEvent event){
        continueIfPaused();
    }

    private void continueIfPaused() {
        if(percentagePassedBeforeStop > 0){
            continueGame();
        }
    }

    public void onEvent(StopPlayerStatusInAir event){
        stopGame(Constants.WIND,true, GamePanel.screenHeight/2);
    }
    private void continueGame() {
        long minusFaktor = (long) (percentagePassedBeforeStop*oscPeriod);
        GamePanel.lastUpdateTime = System.nanoTime() - minusFaktor;
        TutorialPlayer.getTutorialPlayer().unPause(oscPeriod);
    }

    @Override
    public Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException {
        Height curHeight = super.calculatePos(playerPower, maxHeight, xPosition, player, trampolin);
        if(curHeight.isGreaterThan(100) && !hadStoppedForPowerDisplay){
            stopGame(Constants.NOTICE_DISPLAY,false, PowerDisplay.BOTTOM+50);
            hadStoppedForPowerDisplay = true;
        }
        return curHeight;
    }

    private void stopGame(String message, boolean swipeToRestart, int yPos) {
        double elapsedNanos = System.nanoTime() - GamePanel.lastUpdateTime;
        percentagePassedBeforeStop = elapsedNanos / oscPeriod;
        TutorialGamePanel.onlyRestartWhenSwiped =swipeToRestart;
        TutorialPlayer.getTutorialPlayer().pause();
        EventBus.getDefault().post(new StopPlayerTouchingTrampolinEvent(message, 50,yPos));
    }
}
