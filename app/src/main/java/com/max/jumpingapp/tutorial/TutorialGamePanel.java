package com.max.jumpingapp.tutorial;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.max.jumpingapp.game.Game;
import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.game.MainThread;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.tutorial.tutorialPlayer.TutorialPlayer;
import com.max.jumpingapp.types.Width;
import com.max.jumpingapp.types.XCenter;
import com.max.jumpingapp.views.MainActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialGamePanel extends GamePanel {
    private TutorialMainThread thread;

    private TutorialGamePanel(Context context, int[] highScores) {
        super(context, highScores);
    }

    public static GamePanel create(MainActivity mainActivity, int[] highScores) {
        return new TutorialGamePanel(mainActivity, highScores);
    }

    @Override
    protected void createGame() {
        Trampolin trampolin = new Trampolin(new XCenter(screenWidth / 2), new Width(screenWidth * TRAMPOLIN_SPAN_OF_SCREEN));

        Player player = new TutorialPlayer(new XCenter(screenWidth / 2), new Width(GamePanel.screenWidth * 0.2), createPlayerImage());

        this.game = new Game(createBackground(), trampolin, player, highScores);
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

    protected void setTouchBeginning(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            restartThreadIfNecessary();
            xTouchBeg = (int) event.getX();
            timeTouchBeg = System.nanoTime();
            this.touching = true;
        }
    }

    private void restartThreadIfNecessary() {
        if (!thread.isRunning()) {
            EventBus.getDefault().unregister(thread);
            thread = new TutorialMainThread(getHolder(), this);
            EventBus.getDefault().register(thread);
            thread.startRunning();
            EventBus.getDefault().post(new GameContinuedEvent());
        }
    }
}
