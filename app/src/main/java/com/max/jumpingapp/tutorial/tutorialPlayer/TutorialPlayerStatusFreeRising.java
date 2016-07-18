package com.max.jumpingapp.tutorial.tutorialPlayer;

import com.max.jumpingapp.game.PlayerDiedException;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.player.PlayerPower;
import com.max.jumpingapp.objects.visuals.PlayerObject;
import com.max.jumpingapp.objects.visuals.PowerDisplay;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.XPosition;
import com.max.jumpingapp.util.Constants;
import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.objects.player.PlayerStatusFreeRising;
import com.max.jumpingapp.tutorial.FingerTouchingEvent;
import com.max.jumpingapp.tutorial.StopPlayerStatusInAir;
import com.max.jumpingapp.tutorial.StopPlayerTouchingTrampolinEvent;
import com.max.jumpingapp.tutorial.TutorialGamePanel;

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
