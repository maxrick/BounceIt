package com.maxrick.bounceit.tutorial.tutorialPlayer;

import com.maxrick.bounceit.game.PlayerDiedException;
import com.maxrick.bounceit.game.PlayerStatusDiedException;
import com.maxrick.bounceit.objects.Trampolin;
import com.maxrick.bounceit.objects.player.PlayerStatusDead;
import com.maxrick.bounceit.objects.visuals.PlayerObject;
import com.maxrick.bounceit.types.Height;
import com.maxrick.bounceit.types.XPosition;
import com.maxrick.bounceit.util.Constants;
import com.maxrick.bounceit.events.FingerReleasedEvent;
import com.maxrick.bounceit.game.GamePanel;
import com.maxrick.bounceit.objects.player.Player;
import com.maxrick.bounceit.objects.player.PlayerPower;
import com.maxrick.bounceit.objects.player.PlayerStatus;
import com.maxrick.bounceit.objects.player.PlayerStatusSpringFalling;
import com.maxrick.bounceit.tutorial.FingerTouchingEvent;
import com.maxrick.bounceit.tutorial.StopPlayerTouchingTrampolinEvent;
import com.maxrick.bounceit.tutorial.TutorialGamePanel;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialPlayerStatusSpringFalling extends PlayerStatusSpringFalling {
    private double percentagePassedBeforeStop=0;

    public TutorialPlayerStatusSpringFalling(double oscPeriod, double fallPeriod, PlayerObject playerObject) {
        super(oscPeriod, fallPeriod, playerObject);
        EventBus.getDefault().register(this);//@// TODO: 4/19/2016 not todo
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > oscPeriod){
            EventBus.getDefault().unregister(this);
            return new TutorialPlayerStatusSpringRising(oscPeriod, fallPeriod, playerObject);
        }
        return this;
    }

    public void onEvent(FingerTouchingEvent event){
        long minusFactor = (long) (percentagePassedBeforeStop*oscPeriod);
        GamePanel.lastUpdateTime = System.nanoTime() - minusFactor;// - 0);
        TutorialPlayer.getTutorialPlayer().unPause(oscPeriod);
    }

    @Override
    public Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException, PlayerStatusDiedException {
        double elapsedSeconds = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        xPosition.dontMove();
        Height curHeight = new Height((int) (-(Math.sqrt(2 * mass * PlayerStatus.gravitaion * maxHeight / GamePanel.SPRINGCONST) *
                Math.sin(elapsedSeconds / Math.sqrt(mass / GamePanel.SPRINGCONST)))));
        playerObject.setRect(curHeight, xPosition);
        if(!trampolin.supportingPlayer(player)){
            throw new PlayerStatusDiedException(new PlayerStatusDead(oscPeriod, fallPeriod, playerObject));
        }
        return curHeight;
    }

    public void onEvent(FingerReleasedEvent event){
        double elapsedNanos = System.nanoTime() - GamePanel.lastUpdateTime;
        percentagePassedBeforeStop = elapsedNanos/oscPeriod;
        TutorialPlayer.getTutorialPlayer().pause();
        EventBus.getDefault().post(new StopPlayerTouchingTrampolinEvent(Constants.HOLD_DOWN_FULL));
    }

    public void onFingerReleased(PlayerPower playerPower, int maxHeight) {
        if(!TutorialGamePanel.gamePaused){
            playerPower.setAccelerator(maxHeight, oscPeriod);
        }
    }
}
