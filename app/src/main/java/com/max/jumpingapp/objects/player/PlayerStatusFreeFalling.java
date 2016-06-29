package com.max.jumpingapp.objects.player;

import com.max.jumpingapp.events.DrawFingerSwipeLeftEvent;
import com.max.jumpingapp.events.DrawFingerSwipeRightEvent;
import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.events.ResetPowerDisplayEvent;
import com.max.jumpingapp.objects.visuals.PlayerObject;
import com.max.jumpingapp.tutorial.ScreenMessage;
import com.max.jumpingapp.types.Height;
import com.max.jumpingapp.game.PlayerDiedException;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.types.XPosition;

import de.greenrobot.event.EventBus;

/**
 * Created by normal on 25.10.2015.
 */
public class PlayerStatusFreeFalling extends PlayerStatus {

    public PlayerStatusFreeFalling(double oscPeriod, double fallPeriod, PlayerObject playerObject) {
        super(oscPeriod, fallPeriod, playerObject);
    }
    public PlayerStatusFreeFalling(double maxHeight, PlayerObject playerObject){
        super(maxHeight, playerObject);
    }

    @Override
    public Height calculatePos(PlayerPower playerPower, int maxHeight, XPosition xPosition, Player player, Trampolin trampolin) throws PlayerDiedException {
        double elapsedSeconds = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        xPosition.move();
        testDieSet = false;
        Height curHeight = new Height((int) (-0.5 * PlayerStatus.gravitaion * Math.pow(elapsedSeconds, 2) + maxHeight));
        playerObject.setRect(curHeight, xPosition);
        if(player.getRight() < GamePanel.screenWidth/2 && maxHeight < 2000){
            //EventBus.getDefault().post(new HelpInstructionEvent(new ScreenMessage("Swipe right"), 2000000000/30));
            EventBus.getDefault().post(new DrawFingerSwipeRightEvent(new ScreenMessage("Swipe right",-GamePanel.screenWidth/5,GamePanel.screenHeight*3/5), 1200000000/2));
        }else if(player.getLeft() > GamePanel.screenWidth/2 && maxHeight < 2000){
            //EventBus.getDefault().post(new HelpInstructionEvent(new ScreenMessage("Swipe left"), 2000000000/30));
            EventBus.getDefault().post(new DrawFingerSwipeLeftEvent(new ScreenMessage("Swipe left",-GamePanel.screenWidth/5,GamePanel.screenHeight*3/5), 1200000000/2));
        }
        return curHeight;
    }

    @Override
    public PlayerStatus getCurrentPlayerStatus(Player player) {
        double elapsed = (System.nanoTime() - GamePanel.lastUpdateTime) / GamePanel.secondInNanos;
        if(elapsed > fallPeriod){
            return new PlayerStatusSpringFalling(oscPeriod, fallPeriod, this.playerObject);
        }
        return this;
    }

    @Override
    public void updatePower(PlayerPower playerPower, boolean fingerTouching) {
        if(fingerTouching){
            playerPower.decreasePower(oscPeriod);
        }
    }

    @Override
    public void onFingerReleased(PlayerPower playerPower, int maxHeight) {
        playerPower.resetPower();
        EventBus.getDefault().post(new ResetPowerDisplayEvent());
    }

    @Override
    public void animate(boolean touching) {
        playerObject.animate(false);
    }


}
