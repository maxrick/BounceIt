package com.max.jumpingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.max.jumpingapp.objects.visuals.Background;
import com.max.jumpingapp.objects.Game;
import com.max.jumpingapp.objects.player.Player;
import com.max.jumpingapp.objects.Trampolin;

/**
 * Created by normal on 29.08.2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private static final double TRAMPOLIN_SPAN_OF_SCREEN = 0.8;
    public static final int SPRINGCONST = 10;
    public static final int HEIGHT_POS = 300;
    public static final int FORM_HEIGHT = 200;
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
    public static int heightNill = 700;
    public static int screenHeight;
    public static int screenWidth;
    public static double secondInNanos= 1000000000.d;
    public static GamePanel meGamePanel;//todo only for test
    private MainThread thread;
    private MainActivity activity;

    public Game game;


    private boolean touching = false;
    private int dy;
    private int xTouchBeg = 0;
    private long timeTouchBeg;


    private GamePanel(Context context) {
        super(context);
        this.activity = (MainActivity) context;
        getHolder().addCallback(this);
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

        setFocusable(true);

    }

    public static GamePanel create(MainActivity mainActivity) {
        if (meGamePanel == null) {
            meGamePanel = new GamePanel(mainActivity);
        }
        return meGamePanel;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        GamePanel.screenWidth = getWidth();
        GamePanel.screenHeight = getHeight();

        int trampXCenter = GamePanel.screenWidth/2;
        int trampWidth = (int) (GamePanel.screenWidth * TRAMPOLIN_SPAN_OF_SCREEN);
        Trampolin trampolin = new Trampolin(trampXCenter, trampWidth, SPRINGCONST);

        Bitmap backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background3);
        Background background = new Background(backgroundImage);

        Bitmap playerImage = BitmapFactory.decodeResource(getResources(), R.drawable.playerimage);
        Player player = GamePanel.createPlayer(playerImage);

        this.game = new Game(background, trampolin, player);
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @NonNull
    public static Player createPlayer(Bitmap playerImage) {
        int xCenter = GamePanel.screenWidth/2;
        int playerWidth = (int) (GamePanel.screenWidth * 0.2);
        return new Player(xCenter, playerWidth, FORM_HEIGHT, HEIGHT_POS, playerImage);
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
        boolean retry = true;
        int count=0;
        while(retry && count<1000){
            count++;
            try {
                thread.setRunning(false);
                thread.join();
                retry=false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        meGamePanel = null;
    }

    public void update(long timeMikro) {
        try {
            game.update(timeMikro, touching);
        } catch (PlayerDiedException e) {
            activity.changeToDiedScreen((int) e.height);
//            surfaceDestroyed(getHolder());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if(event.getX()<150 && event.getY() < 120){
//                game.upgradeTrampolin();
//            }
            xTouchBeg = (int) event.getX();
            timeTouchBeg = System.nanoTime();
            this.touching = true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            long elapsed = (System.nanoTime() - timeTouchBeg) / 1000000;//millisec
            float xTouchEnd = event.getX();
            int xSwipedToRight = (int) xTouchEnd - xTouchBeg;
            if (elapsed < 1000 && Math.abs(xSwipedToRight) > 100) {
                boolean toRight = xTouchEnd - xTouchBeg > 0;
                game.swiped(xSwipedToRight);
            }
            this.touching = false;
        }
        return true;
    }
}
