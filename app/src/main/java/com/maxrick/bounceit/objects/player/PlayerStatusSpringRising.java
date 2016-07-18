package com.maxrick.bounceit.objects.player;

import com.maxrick.bounceit.events.DrawFingerTouchEvent;
import com.maxrick.bounceit.game.GamePanel;
import com.maxrick.bounceit.events.LeftTrampolinEvent;
import com.maxrick.bounceit.objects.visuals.PlayerObject;
import com.maxrick.bounceit.tutorial.ScreenMessage;
import com.maxrick.bounceit.types.Height;
import com.maxrick.bounceit.game.PlayerDiedException;
import com.maxrick.bounceit.objects.Trampolin;
import com.maxrick.bounceit.types.XPosition;

import de.greenrobot.event.EventBus;

/**
 * Created by normal on 25.10.2015.
 */
public class PlayerStatusSpringRising extends PlayerStatus {
    public PlayerStatusSpringRising(double oscPeriod, double fallPeriod, PlayerObject playerObject) {
        super(oscPeriod, fallPeriod, playerObject);
    }

    @Override
    public Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException {
        double elapsedSeconds = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        xPosition.dontMove();
        Height curHeight = new Height((int) (-(Math.sqrt(2 * mass * gravitaion * maxHeight / GamePanel.SPRINGCONST)
                * Math.sin((elapsedSeconds + this.oscPeriod) / Math.sqrt(mass / GamePanel.SPRINGCONST)))));
        playerObject.setRect(curHeight, xPosition);
        if(maxHeight < 2000 && !curHeight.isGreaterThan(-200)){
            //EventBus.getDefault().post(new HelpInstructionEvent(new ScreenMessage("Touch and Hold"), 2000000000/30));
            EventBus.getDefault().post(new DrawFingerTouchEvent(new ScreenMessage("Touch and Hold",-GamePanel.screenWidth/5,GamePanel.screenHeight*3/5), 2000000000/30));

        }
        if(maxHeight < 2000 && curHeight.isGreaterThan(-200)){
            //EventBus.getDefault().post(new HelpInstructionEvent(new ScreenMessage("Release"), 2000000000/30));
            //EventBus.getDefault().post(new DrawFingerReleaseEvent(new ScreenMessage("Touch and Hold",-GamePanel.screenWidth/5,GamePanel.screenHeight*3/5), 2000000000/30));

        }
        return curHeight;
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > oscPeriod){
            EventBus.getDefault().post(new LeftTrampolinEvent(player));
            return new PlayerStatusFreeRising(oscPeriod, fallPeriod, playerObject);
        }
        return this;
    }

    @Override
    public void updatePower(PlayerPower playerPower, boolean fingerTouching) {
        if(fingerTouching){
            playerPower.increasePower(oscPeriod);
        }
    }

    @Override
    public void onFingerReleased(PlayerPower playerPower, int maxHeight) {
        playerPower.setAccelerator(maxHeight, oscPeriod);
    }

    @Override
    public void animate(boolean touching) {
        playerObject.animate(touching);
    }


}
