package com.max.jumpingapp.tutorial;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.view.SurfaceHolder;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.game.MainThread;

/**
 * Created by max on 4/19/2016.
 */
public class TutorialMainThread extends MainThread {
    protected ScreenMessage message;
    protected boolean undrawnMessage = false;

    public TutorialMainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super(surfaceHolder, gamePanel);
    }

    @Override
    public void run() {
        long startTime;
        long timeMikro = 0;
        long waitTime;
        long targetTime = 1000 / FPS;
        while (running || undrawnMessage) {
            startTime = System.nanoTime();
            Canvas canvas = null;

            try {
                canvas = surfaceholder.lockCanvas();
                synchronized (surfaceholder) {
                    this.gamepanel.update(timeMikro);
                    this.gamepanel.draw(canvas);
                    drawMessage(canvas);
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

    public void onEvent(StopPlayerTouchingTrampolinEvent event) {
        stopRunning();
        setMessageToDraw(new ScreenMessage(event.getMessage(), event.getxPos(), event.getyPos()));
    }

    private void setMessageToDraw(ScreenMessage message) {
        this.message = message;
        undrawnMessage = true;
    }

    private void drawMessage(Canvas canvas) {
        if (undrawnMessage) {
            undrawnMessage = false;
            Paint paint = new Paint();
            paint.setTextSize(40);
//            canvas.drawText(message, 100, GamePanel.screenHeight / 5, paint);
            drawMultiline(message.getMessage(), message.getxPos(), message.getyPos(), paint, canvas);
        }
    }

    public void drawMultiline(String str, int x, int y, Paint paint, Canvas canvas)
    {
        for (String line: str.split("\n"))
        {
            canvas.drawText(line, x, y, paint);
            y += -paint.ascent() + paint.descent();
        }
    }

}
