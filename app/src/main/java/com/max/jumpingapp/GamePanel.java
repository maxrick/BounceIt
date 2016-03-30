package com.max.jumpingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.max.jumpingapp.objects.Background;
import com.max.jumpingapp.objects.Blaetter;
import com.max.jumpingapp.objects.Player;
import com.max.jumpingapp.objects.Trampolin;

/**
 * Created by normal on 29.08.2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static int heightNill = 700;
    public static int screenHeight;
    public static int screenWidth;
    public static double secondInNanos= 1000000000.d;
    //    public static double secondInNanos= 1000000000.d;
    private static GamePanel meGamePanel;
    private MainThread thread;
    private Background bg;
    private Trampolin trampolin;
    //    private Rect rect;
    private Player player;
    private Blaetter blaetter;
    private Wind wind;
    private boolean touching = false;
    private int dy;
    private int xTouchBeg = 0;
    private long timeTouchBeg;


    private GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

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



        int trampXCenter = (int) (GamePanel.screenWidth * 0.5);
        int trampWidth = (int) (GamePanel.screenWidth * 1.2);
        int trampYPos = (int) (GamePanel.screenHeight * 0.8);
        trampolin = new Trampolin((int) (trampXCenter - 0.5 * trampWidth), trampYPos, trampWidth, 10);
        blaetter = new Blaetter();
        wind = new Wind();
        int rectWidth = (int) (GamePanel.screenWidth * 0.2);
        Bitmap playerImage = BitmapFactory.decodeResource(getResources(), R.drawable.playerimage);
        player = new Player((int) (trampXCenter - 0.5*rectWidth), (int) (trampXCenter+ 0.5*rectWidth ), 200, 300, trampolin, playerImage);//(trampXCenter - 0.5 * rectWidth), (int) (trampXCenter + 0.5 * rectWidth), 200, 300, trampolin, playerImage);
        Bitmap backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background3);
        bg = new Background(backgroundImage);
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
        //bg.draw(canvas);
        //Drawable shape = ContextCompat.getDrawable(getContext(), R.drawable.gradient);
        int moveBy = trampolin.draw(canvas, player, bg);
        blaetter.draw(canvas, moveBy);
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
//        playerStatus = playerStatus.getCurrentPlayerStatus();
        player.updatePosition(blaetter, trampolin, wind);
        player.updatePower(touching, trampolin, timeMikro);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(event.getX()<150 && event.getY() < 120){
                upgradeTrampolin();
            }
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
                swiped(xSwipedToRight);
            }
            this.touching = false;
        }
        return true;
    }

    private void upgradeTrampolin() {
        trampolin = trampolin.upgrade(10);
        player.trampolinChanged(trampolin);
    }

    private void swiped(int xSwipedToRight) {
        player.removeBlaetter(blaetter);
        player.xAccel(xSwipedToRight);
    }

}
