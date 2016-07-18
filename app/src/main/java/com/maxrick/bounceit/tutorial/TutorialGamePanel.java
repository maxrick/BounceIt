package com.maxrick.bounceit.tutorial;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.maxrick.bounceit.events.FingerReleasedEvent;
import com.maxrick.bounceit.game.Game;
import com.maxrick.bounceit.game.GamePanel;
import com.maxrick.bounceit.objects.Trampolin;
import com.maxrick.bounceit.objects.player.Player;
import com.maxrick.bounceit.tutorial.tutorialPlayer.TutorialPlayer;
import com.maxrick.bounceit.types.Width;
import com.maxrick.bounceit.types.XCenter;
import com.maxrick.bounceit.views.MainActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialGamePanel extends GamePanel {
    public static boolean onlyRestartWhenSwiped=false;
    private TutorialMainThread thread;
    public static boolean eventPleaseReleasePosted= false;
    public static boolean gamePaused = false;

    private TutorialGamePanel(Context context, int[] highScores, int playerImgage,  float leftOfImage, float rightOfImage) {
        super(context, highScores, playerImgage, leftOfImage, rightOfImage);
    }

    public static GamePanel create(MainActivity mainActivity, int[] highScores, int playerImage, float leftOfImage, float rightOfImage) {
        return new TutorialGamePanel(mainActivity, highScores, playerImage,leftOfImage, rightOfImage);
    }

    @Override
    protected void createGame() {
        Trampolin trampolin = new Trampolin(new XCenter(screenWidth / 2), new Width(screenWidth * TRAMPOLIN_SPAN_OF_SCREEN));

        Player player = TutorialPlayer.createTutorialPlayer(new XCenter(screenWidth / 2), new Width(GamePanel.screenWidth * 0.2), createPlayerImage(), leftOfImage, rightOfImage);

        this.game = new Game(createBackground(), trampolin, player, highScores, getResources());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        EventBus.getDefault().unregister(thread);
        thread.stopRunning();
        game.unregisterEventlisteners();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        calculateScreenDimensions();

        createGame();
        thread = new TutorialMainThread(getHolder(), this);
        EventBus.getDefault().register(thread);
        thread.startRunning();
    }

    protected void actOnRelease(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if(TutorialGamePanel.eventPleaseReleasePosted){
                restartThreadIfNecessary();
            }
            long elapsed = (System.nanoTime() - timeTouchBeg);
            float xTouchEnd = event.getX();
            int xSwipedToRight = (int) xTouchEnd - xTouchBeg;
            if (elapsed < secondInNanos && Math.abs(xSwipedToRight) > 100) {
                game.swiped(xSwipedToRight);
                onlyRestartWhenSwiped=false;
                restartThreadIfNecessary();
            }
            EventBus.getDefault().post(new FingerReleasedEvent());
            this.touching = false;
        }
    }

    protected void setTouchBeginning(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            restartThreadIfNecessary();

            xTouchBeg = (int) event.getX();
            timeTouchBeg = System.nanoTime();
            this.touching = true;
        }
    }

    private synchronized void restartThreadIfNecessary() {
        if (!thread.isRunning() && !onlyRestartWhenSwiped) {
            EventBus.getDefault().unregister(thread);
            thread = new TutorialMainThread(getHolder(), this);
            EventBus.getDefault().register(thread);
            thread.startRunning();
            EventBus.getDefault().post(new FingerTouchingEvent());
        }
    }
}
