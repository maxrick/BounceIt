package com.max.jumpingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by normal on 29.08.2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static int heightNill = 700;
    public static int screenHeight;
    public static int screenWidth;
    private static GamePanel meGamePanel;
    private MainThread thread;
    private Background bg;
    private Trampolin trampolin;
    //    private Rect rect;
    private Player player;
    private Blaetter blaetter;
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



        int trampXCenter = (int) (getWidth() * 0.5);
        int trampWidth = (int) (getWidth() * 0.8);
        int trampYPos = (int) (getHeight() * 0.8);
        trampolin = new Trampolin((int) (trampXCenter - 0.5 * trampWidth), trampYPos, trampWidth, 10);
        blaetter = new Blaetter();
        int rectWidth = (int) (getWidth() * 0.2);
        Bitmap playerImage = BitmapFactory.decodeResource(getResources(), R.drawable.playerimage);
        player = new Player((int) (trampXCenter - 0.5 * rectWidth), (int) (trampXCenter + 0.5 * rectWidth), 200, 300, trampolin, playerImage);
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
        //bg.draw(canvas);
        Drawable shape = ContextCompat.getDrawable(getContext(), R.drawable.gradient);
        int moveBy = trampolin.draw(canvas, player, getHeight(), getWidth(), shape);
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
        player.updatePosition(blaetter, trampolin);
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
            int xMoved = Math.abs((int) event.getX() - xTouchBeg);
            if (elapsed < 1000 && xMoved > 100) {
                swiped();
            }
            this.touching = false;
        }
        return true;
    }

    private void upgradeTrampolin() {
        trampolin = trampolin.upgrade(10);
        player.trampolinChanged(trampolin);
    }

    private void swiped() {
        player.removeBlaetter(blaetter);
    }

}
