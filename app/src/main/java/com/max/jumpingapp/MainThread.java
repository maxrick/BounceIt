package com.max.jumpingapp;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by normal on 29.08.2015.
 */
public class MainThread extends Thread {
    public static int FPS = 30;
    private static Canvas canvas;
    private double averageFPS;
    private final SurfaceHolder surfaceholder;
    private GamePanel gamepanel;
    private boolean running;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceholder = surfaceHolder;
        this.gamepanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMikro=0;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / FPS;
        while (running) {
            startTime = System.nanoTime();
            canvas = null;

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
            waitTime = targetTime - timeMikro/1000;
            if (waitTime < 0) {
                waitTime = 0;
            }
            try {
                sleep(waitTime);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == FPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
            }

        }
    }

    public void setRunning(boolean b) {
        running = b;
    }

}
