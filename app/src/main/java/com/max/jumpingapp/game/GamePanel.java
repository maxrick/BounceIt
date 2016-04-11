package com.max.jumpingapp.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.max.jumpingapp.R;
import com.max.jumpingapp.objects.visuals.Background;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.Trampolin;
import com.max.jumpingapp.types.Width;
import com.max.jumpingapp.types.XCenter;
import com.max.jumpingapp.views.MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by normal on 29.08.2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private static final double TRAMPOLIN_SPAN_OF_SCREEN = 0.8;
    public static final int SPRINGCONST = 10;
    public static final int HEIGHT_POS = 300;
    public static int GREEN1;
    public static int GREEN2;
    public static int GREEN3;
    public static int GREEN4;
    public static int GREEN5;
    public static int RED1;
    public static int RED2;
    public static int RED3;
    public static int RED4;
    public static int RED5;
    public static final int HEIGHT_NILL = 700;
    public static int screenHeight;
    public static int screenWidth;
    public static double secondInNanos = 1000000000.d;
    public static GamePanel meGamePanel;//todo only for test
    private MainThread thread;
    private MainActivity activity;
    private int[] highScores;

    public Game game;

    private boolean touching = false;
    private int xTouchBeg = 0;
    private long timeTouchBeg;


    private GamePanel(Context context, int[] highScores) {
        super(context);
        this.activity = (MainActivity) context;
        getHolder().addCallback(this);
        this.highScores = highScores;
        readCustomColors(context);

        setFocusable(true);

    }

    private void readCustomColors(Context context) {
        this.GREEN1 = ContextCompat.getColor(context, R.color.green1);
        this.GREEN2 = ContextCompat.getColor(context, R.color.green2);
        this.GREEN3 = ContextCompat.getColor(context, R.color.green3);
        this.GREEN4 = ContextCompat.getColor(context, R.color.green4);
        this.GREEN5 = ContextCompat.getColor(context, R.color.green5);
        this.RED1 = ContextCompat.getColor(context, R.color.red1);
        this.RED2 = ContextCompat.getColor(context, R.color.red2);
        this.RED3 = ContextCompat.getColor(context, R.color.red3);
        this.RED4 = ContextCompat.getColor(context, R.color.red4);
        this.RED5 = ContextCompat.getColor(context, R.color.red5);
    }

    public static GamePanel create(MainActivity mainActivity, int[] highScores) {
        if (meGamePanel == null) {
            meGamePanel = new GamePanel(mainActivity, highScores);
        }
        return meGamePanel;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        calculateScreenDimensions();

        createGame();
        thread = new MainThread(getHolder(), this);
        thread.startRunning();
    }

    private void createGame() {
        Trampolin trampolin = new Trampolin(new XCenter(screenWidth/2), new Width(screenWidth * TRAMPOLIN_SPAN_OF_SCREEN), SPRINGCONST);

        Player player = GamePanel.createPlayer(createPlayerImage());//@// TODO: 4/10/2016 highscores belong to game, not player

        this.game = new Game(createBackground(), trampolin, player, highScores);
    }

    private Bitmap createPlayerImage() {
        return BitmapFactory.decodeResource(getResources(), R.drawable.playerimage);
    }

    @NonNull
    private Background createBackground() {
        return new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background3));
    }

    private void calculateScreenDimensions() {
        GamePanel.screenWidth = getWidth();
        GamePanel.screenHeight = getHeight();
    }

    @NonNull
    public static Player createPlayer(Bitmap playerImage) {
        return new Player(new XCenter(screenWidth/2), new Width(GamePanel.screenWidth * 0.2), HEIGHT_POS, playerImage);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        game.draw(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.stopRunning();
        meGamePanel = null;
    }

    public void update(long timeMikro) {
        try {
            game.update(timeMikro, touching);
        } catch (PlayerDiedException e) {
            surfaceDestroyed(getHolder());
            activity.changeToDiedScreen(e.score);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setTouchBeginning(event);
        actOnRelease(event);
        return true;
    }

    private void actOnRelease(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            long elapsed = (System.nanoTime() - timeTouchBeg) / 1000000;//millisec
            float xTouchEnd = event.getX();
            int xSwipedToRight = (int) xTouchEnd - xTouchBeg;
            if (elapsed < 1000 && Math.abs(xSwipedToRight) > 100) {
                game.swiped(xSwipedToRight);
            }
            this.touching = false;
        }
    }

    private void setTouchBeginning(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            xTouchBeg = (int) event.getX();
            timeTouchBeg = System.nanoTime();
            this.touching = true;
        }
    }

}
