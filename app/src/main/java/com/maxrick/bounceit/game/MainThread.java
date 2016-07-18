package com.maxrick.bounceit.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by normal on 29.08.2015.
 */
public class MainThread extends Thread {
    public static int FPS = 30;
    protected final SurfaceHolder surfaceholder;
    protected GamePanel gamepanel;
    protected boolean running;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceholder = surfaceHolder;
        this.gamepanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMikro = 0;
        long waitTime;
        long targetTime = 1000 / FPS;
        while (running) {
            startTime = System.nanoTime();
            Canvas canvas = null;

            try {
                canvas = surfaceholder.lockCanvas();
                synchronized (surfaceholder) {
                    this.gamepanel.update(timeMikro);
                    this.gamepanel.draw(canvas);
                }
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                if (canvas != null) {
                    surfaceholder.unlockCanvasAndPost(canvas);
                }
            }
            timeMikro = (System.nanoTime() - startTime) / 1000;
            waitTime = targetTime - timeMikro / 1000;
            if (waitTime < 0) {
                waitTime = 0;
            }
            try {
                sleep(waitTime);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                setRunning(false);
            }

        }
    }


    private void setRunning(boolean b) {
        running = b;
    }

    public void startRunning() {
        this.setRunning(true);
        this.start();
    }

    public void stopRunning() {
        this.setRunning(false);
    }

    public boolean isRunning() {
        return running;
    }
}
