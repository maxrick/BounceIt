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

import de.greenrobot.event.EventBus;

/**
 * Created by normal on 29.08.2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    protected static final double TRAMPOLIN_SPAN_OF_SCREEN = 0.8;
    public static final int SPRINGCONST = 10;
    public static final int HEIGHT_POS = 300;
    public static long lastUpdateTime=System.nanoTime();
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
    protected final int playerImage;
    protected final float leftOfImage;
    protected final float rightOfImage;
    protected MainThread thread;
    private MainActivity activity;
    protected int[] highScores;

    public Game game;

    protected boolean touching = false;
    protected int xTouchBeg = 0;
    protected long timeTouchBeg;


    protected GamePanel(Context context, int[] highScores, int playerImgage, float leftOfImage, float rightOfImage) {
        super(context);
        this.activity = (MainActivity) context;
        getHolder().addCallback(this);
        this.highScores = highScores;
        this.playerImage = playerImgage;
        this.leftOfImage = leftOfImage;
        this.rightOfImage=rightOfImage;
        readCustomColors(context);

        setFocusable(true);

    }

    private void readCustomColors(Context context) {
        GREEN1 = ContextCompat.getColor(context, R.color.green1);
        GREEN2 = ContextCompat.getColor(context, R.color.green2);
        GREEN3 = ContextCompat.getColor(context, R.color.green3);
        GREEN4 = ContextCompat.getColor(context, R.color.green4);
        GREEN5 = ContextCompat.getColor(context, R.color.green5);
        RED1 = ContextCompat.getColor(context, R.color.red1);
        RED2 = ContextCompat.getColor(context, R.color.red2);
        RED3 = ContextCompat.getColor(context, R.color.red3);
        RED4 = ContextCompat.getColor(context, R.color.red4);
        RED5 = ContextCompat.getColor(context, R.color.red5);
    }

    public static GamePanel create(MainActivity mainActivity, int[] highScores, int playerImgage, float leftOfImage, float rightOfImage) {
       return new GamePanel(mainActivity, highScores, playerImgage, leftOfImage, rightOfImage);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        calculateScreenDimensions();

        createGame();
        thread = new MainThread(getHolder(), this);
        thread.startRunning();
    }

    protected void createGame() {
        Trampolin trampolin = new Trampolin(new XCenter(screenWidth/2), new Width(screenWidth * TRAMPOLIN_SPAN_OF_SCREEN));

        Player player = GamePanel.createPlayer(createPlayerImage(), leftOfImage, rightOfImage);

        this.game = new Game(createBackground(), trampolin, player, highScores, getResources());
    }

    protected Bitmap createPlayerImage() {
        return BitmapFactory.decodeResource(getResources(), playerImage);
    }

    @NonNull
    protected Background createBackground() {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        return new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background4,options), BitmapFactory.decodeResource(getResources(), R.drawable.stars3));
    }

    protected void calculateScreenDimensions() {
        GamePanel.screenWidth = getWidth();
        GamePanel.screenHeight = getHeight();
    }

    @NonNull
    public static Player createPlayer(Bitmap playerImage, float leftOfImage, float rightOfImage) {
        return new Player(new XCenter(screenWidth/2), new Width(GamePanel.screenWidth * 0.2), playerImage, leftOfImage, rightOfImage);
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
        game.unregisterEventlisteners();
    }

    public void update(long timeMikro) {
//        lastUpdateTime = System.nanoTime();
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

    protected void actOnRelease(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            long elapsed = (System.nanoTime() - timeTouchBeg);
            float xTouchEnd = event.getX();
            int xSwipedToRight = (int) xTouchEnd - xTouchBeg;
            if (elapsed < secondInNanos && Math.abs(xSwipedToRight) > 100) {
                game.swiped(xSwipedToRight);
            }
            EventBus.getDefault().post(new FingerReleasedEvent());
            this.touching = false;
        }
    }

    protected void setTouchBeginning(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            xTouchBeg = (int) event.getX();
            timeTouchBeg = System.nanoTime();
            this.touching = true;
        }
    }

    public static void refreshUpdateTime() {
        lastUpdateTime = System.nanoTime();
    }
}
