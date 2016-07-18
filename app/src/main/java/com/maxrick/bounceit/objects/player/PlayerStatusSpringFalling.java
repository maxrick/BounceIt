package com.maxrick.bounceit.objects.player;

import com.maxrick.bounceit.events.DrawFingerTouchEvent;
import com.maxrick.bounceit.game.GamePanel;
import com.maxrick.bounceit.game.PlayerStatusDiedException;
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
public class PlayerStatusSpringFalling extends PlayerStatus {//@// TODO: 4/12/2016 make subclass Spring and Rising

    public PlayerStatusSpringFalling(double oscPeriod, double fallPeriod, PlayerObject playerObject) {
        super(oscPeriod, fallPeriod, playerObject);
    }

    @Override
    public Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException, PlayerStatusDiedException {
        double elapsedSeconds = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        xPosition.dontMove();
        Height curHeight = new Height((int) (-(Math.sqrt(2 * mass * gravitaion * maxHeight / GamePanel.SPRINGCONST) *
                Math.sin(elapsedSeconds / Math.sqrt(mass / GamePanel.SPRINGCONST)))));
        playerObject.setRect(curHeight, xPosition);
        if(!trampolin.supportingPlayer(player)){
            throw new PlayerStatusDiedException(new PlayerStatusDead(oscPeriod, fallPeriod, playerObject));
        }
        if(maxHeight < 2000){
            //EventBus.getDefault().post(new HelpInstructionEvent(new ScreenMessage("Touch and Hold"), 2000000000/30));
            EventBus.getDefault().post(new DrawFingerTouchEvent(new ScreenMessage("Touch and Hold",-GamePanel.screenWidth/5,GamePanel.screenHeight*3/5), 2000000000/30));
        }
        return curHeight;
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > oscPeriod){
            return new PlayerStatusSpringRising(oscPeriod, fallPeriod, playerObject);
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
