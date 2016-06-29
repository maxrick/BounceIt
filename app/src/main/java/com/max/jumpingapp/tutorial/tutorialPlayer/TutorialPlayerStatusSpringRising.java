package com.max.jumpingapp.tutorial.tutorialPlayer;

import com.max.jumpingapp.game.PlayerDiedException;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.objects.visuals.PlayerObject;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.types.XPosition;
import com.max.jumpingapp.util.Constants;
import com.max.jumpingapp.events.FingerReleasedEvent;
import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.events.LeftTrampolinEvent;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.player.PlayerPower;
import com.max.jumpingapp.objects.player.PlayerStatus;
import com.max.jumpingapp.objects.player.PlayerStatusSpringRising;
import com.max.jumpingapp.tutorial.FingerTouchingEvent;
import com.max.jumpingapp.tutorial.StopPlayerTouchingTrampolinEvent;
import com.max.jumpingapp.tutorial.TutorialGamePanel;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayerStatusSpringRising extends PlayerStatusSpringRising {
    public static final double STOP_BEFORE_TIME = 0.1d;
    private double percentagePassedBeforeStop=0;

    public TutorialPlayerStatusSpringRising(double oscPeriod, double fallPeriod, PlayerObject playerObject) {
        super(oscPeriod, fallPeriod, playerObject);
        EventBus.getDefault().register(this);//@// TODO: 4/19/2016 not todo
        TutorialGamePanel.eventPleaseReleasePosted = false;
    }

    @Override
    public Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException {
        double elapsedSeconds = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        xPosition.dontMove();
        Height curHeight = new Height((int) (-(Math.sqrt(2 * mass * PlayerStatus.gravitaion * maxHeight / GamePanel.SPRINGCONST)
                * Math.sin((elapsedSeconds + this.oscPeriod) / Math.sqrt(mass / GamePanel.SPRINGCONST)))));
        playerObject.setRect(curHeight, xPosition);
        return curHeight;
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > oscPeriod){
            EventBus.getDefault().post(new LeftTrampolinEvent(player));
            EventBus.getDefault().unregister(this);
            return new TutorialPlayerStatusFreeRising(oscPeriod, fallPeriod, playerObject);
        }else if(oscPeriod - elapsed < STOP_BEFORE_TIME && !TutorialGamePanel.eventPleaseReleasePosted){
            TutorialGamePanel.eventPleaseReleasePosted = true;
            stopGame(Constants.RELEASE);
        }
        return this;
    }

    public void onEvent(FingerTouchingEvent event){
        continueGame();
    }

    private void continueGame() {
        long minusFaktor = (long) (percentagePassedBeforeStop*oscPeriod);
        GamePanel.lastUpdateTime = System.nanoTime() - minusFaktor;
        TutorialPlayer.getTutorialPlayer().unPause(oscPeriod);
    }

    public void onEvent(FingerReleasedEvent event) {
        if (!TutorialGamePanel.eventPleaseReleasePosted) {
            stopGame(Constants.HOLD_DOWN_FULL);
        }else {
            continueGame();
        }
    }

    private void stopGame(String message) {
        double elapsedNanos = System.nanoTime() - GamePanel.lastUpdateTime;
        percentagePassedBeforeStop = elapsedNanos / oscPeriod;
        TutorialPlayer.getTutorialPlayer().pause();
        EventBus.getDefault().post(new StopPlayerTouchingTrampolinEvent(message));
    }

    public void onFingerReleased(PlayerPower playerPower, int maxHeight) {
        if(!TutorialGamePanel.gamePaused || TutorialGamePanel.eventPleaseReleasePosted){
            playerPower.setAccelerator(maxHeight, oscPeriod);
        }
    }
}
