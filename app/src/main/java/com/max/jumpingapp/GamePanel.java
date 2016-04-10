package com.max.jumpingapp;

import android.content.Context;
import android.content.SharedPreferences;
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
    public static final int FORM_HEIGHT = 200;
    public static final String NORMAL = "normal";
    public static final String GREY = "grey";
    public static final String ERROR_1 = "error1";
    public static final String ERROR_2 = "error2";
    public static final String ERROR_3 = "error3";
    public static final String ERROR_4 = "error4";
    public static final String ERROR_5 = "error5";
    public static final String SUCCESS_1 = "success1";
    public static final String SUCCESS_2 = "success2";
    public static final String SUCCESS_3 = "success3";
    public static final String SUCCESS_4 = "success4";
    public static final String SUCCESS_5 = "success5";
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
    private double[] highScores;

    public Game game;


    private boolean touching = false;
    private int dy;
    private int xTouchBeg = 0;
    private long timeTouchBeg;


    private GamePanel(Context context, double[] highScores) {
        super(context);
        this.activity = (MainActivity) context;
        getHolder().addCallback(this);
        this.highScores = highScores;
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

    public static GamePanel create(MainActivity mainActivity, double[] highScores) {
        if (meGamePanel == null) {
            meGamePanel = new GamePanel(mainActivity, highScores);
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

        Map<String, Bitmap> backgroundImages = new HashMap<>();
        backgroundImages.put(NORMAL,BitmapFactory.decodeResource(getResources(), R.drawable.background3));
//        backgroundImages.put(GREY, BitmapFactory.decodeResource(getResources(), R.drawable.background3_grey));
//        backgroundImages.put(ERROR_1, BitmapFactory.decodeResource(getResources(), R.drawable.background3_error1));
//        backgroundImages.put(ERROR_2, BitmapFactory.decodeResource(getResources(), R.drawable.background3_error2));
//        backgroundImages.put(ERROR_3, BitmapFactory.decodeResource(getResources(), R.drawable.background3_error3));
//        backgroundImages.put(ERROR_4, BitmapFactory.decodeResource(getResources(), R.drawable.background3_error4));
//        backgroundImages.put(ERROR_5, BitmapFactory.decodeResource(getResources(), R.drawable.background3_error5));
//        backgroundImages.put(SUCCESS_1, BitmapFactory.decodeResource(getResources(), R.drawable.background3_success1));
//        backgroundImages.put(SUCCESS_2, BitmapFactory.decodeResource(getResources(), R.drawable.background3_success2));
//        backgroundImages.put(SUCCESS_3, BitmapFactory.decodeResource(getResources(), R.drawable.background3_success3));
//        backgroundImages.put(SUCCESS_4, BitmapFactory.decodeResource(getResources(), R.drawable.background3_success4));
//        backgroundImages.put(SUCCESS_5, BitmapFactory.decodeResource(getResources(), R.drawable.background3_success5));
        Background background = new Background(backgroundImages);

        Bitmap playerImage = BitmapFactory.decodeResource(getResources(), R.drawable.playerimage);
        Player player = GamePanel.createPlayer(playerImage, background, highScores);//@// TODO: 4/10/2016 highscores belong to game, not player

        this.game = new Game(background, trampolin, player);
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @NonNull
    public static Player createPlayer(Bitmap playerImage, Background background, double[] highScores) {
        int xCenter = GamePanel.screenWidth/2;
        int playerWidth = (int) (GamePanel.screenWidth * 0.2);
        return new Player(xCenter, playerWidth, FORM_HEIGHT, HEIGHT_POS, playerImage, background, highScores);
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
        stopThread();
        meGamePanel = null;
    }

    private void stopThread() {
        boolean retry = true;
        int count=0;
        while(retry && count<1000){
            count++;
//            try {
                System.out.println("interrupting");
                thread.setRunning(false);
//                thread.join();
                System.out.println("interrupt successful");
                retry=false;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void update(long timeMikro) {
        try {
            game.update(timeMikro, touching);
        } catch (PlayerDiedException e) {
            activity.setHighScore(e.height);
            surfaceDestroyed(getHolder());
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
