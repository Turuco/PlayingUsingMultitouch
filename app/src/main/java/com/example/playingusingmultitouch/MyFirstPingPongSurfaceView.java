package com.example.playingusingmultitouch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyFirstPingPongSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // Initial values
    int puntsPaleta1 = 0, puntsPaleta2 = 0;

    // The thread
    private PingPongThread pingPongTread = null;
    // The ball
    private int x;
    private int y;
    // The speed and other attributes
    private int xDirection = 10;
    private int yDirection = 10;
    private static int radius = 20;
    private static int ballColor = Color.BLUE;
    // To two palettes
    public float meitatX;
    boolean clicPartEsquerre;
    private float distanciaX = 100, distanciaY = 10;
    private float ample = 10, alt = 150;
    private float paleta1X, paleta1Y, paleta2X, paleta2Y;
    private float mLastTouchX, mLastTouchY;
    // To manage palettes
// The Paint objects
    Paint table = new Paint();
    Paint marcador = new Paint();
    Paint ball = new Paint();
    Paint paleta1 = new Paint();
    Paint paleta2 = new Paint();


    public MyFirstPingPongSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        x = getWidth() / 2;
        y = getHeight() / 2;
        paleta1X = distanciaX;
        paleta1Y = distanciaY;
        paleta2X = getWidth() - distanciaX;
        paleta2Y = distanciaY;
        meitatX = getWidth() / 2;
        if (pingPongTread != null) return;
        pingPongTread = new PingPongThread(getHolder());
        pingPongTread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopThread();
    }

    public void stopThread() {
        if (pingPongTread != null) pingPongTread.stop = true;
    }

    // The inner thread class

    private class PingPongThread extends Thread {
        public boolean stop = false;
        private SurfaceHolder surfaceHolder;

        public PingPongThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void run() {

            while (!stop) {
                x += xDirection;
                y += yDirection; // The movement of the ball

                //si la bola ix per la esquerre
                if (x + radius < 0) {
                    xDirection = -xDirection;
                    x = (int) (2 * distanciaX); // TO avoid rebounds from behind
                    puntsPaleta2++;
                    System.out.println("Punts Paleta 2 : " + puntsPaleta2);
                }

                //Si la bola sen ix per la dreta
                if (x > getWidth() - radius) {
                    xDirection = -xDirection;
                    x = (int) (getWidth() - 2 * distanciaX);
                    puntsPaleta1++;
                    System.out.println("Punts Paleta 1 : " + puntsPaleta1);

                }
                if (y < 0) {
                    yDirection = -yDirection;
                    y = radius;


                }
                if (y > getHeight() - radius) {
                    yDirection = -yDirection;
                    y = getHeight() - radius - 1;


                }
// Left palette
                if ((y + radius > paleta1Y) && (y + radius < paleta1Y + alt)) {
                    if ((x + radius > paleta1X) && (x - radius < paleta1X + ample)) {
                        xDirection = -xDirection;
                        x += 10; //To avoid repeated rebounds


                    }

                }
// Right palette
                if ((y + radius > paleta2Y) && (y + radius < paleta2Y + alt)) {
                    if ((x + radius > paleta2X) && (x - radius < paleta2X + ample)) {
                        xDirection = -xDirection;
                        x -= 10; //To avoid repeated rebounds

                    }

                }
                Canvas c = null;
                try {
                    c = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        newDraw(c);
                    }
                } finally {
                    if (c != null) surfaceHolder.unlockCanvasAndPost(c);
                }
                if (puntsPaleta1 == 10 || puntsPaleta2 == 10) {
                    stop = true;

                }
            }
        }
    }


    // The newDraw method
    public void newDraw(Canvas canvas) {

// The table
        table.setColor(Color.WHITE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), table);
        marcador.setTextSize(45);
        canvas.drawText("Paleta 1: " + puntsPaleta1 + " Paleta 2: " + puntsPaleta2, 10, 50, marcador);
// The ball
        ball.setColor(ballColor);
        canvas.drawCircle(x, y, radius, ball);
// The palettes
        paleta1.setColor(Color.RED);
        paleta2.setColor(Color.BLUE);
        canvas.drawRect(paleta1X, paleta1Y, paleta1X + ample, paleta1Y + alt, paleta1);
        canvas.drawRect(paleta2X, paleta2Y, paleta2X + ample, paleta2Y + alt, paleta2);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mLastTouchX = ev.getX();
                mLastTouchY = ev.getY();
                if (mLastTouchX < meitatX) {
                    clicPartEsquerre = true;
                } else clicPartEsquerre = false;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
// Calculate the distance moved
                final float x = ev.getX();
                final float y = ev.getY();
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;
                if (clicPartEsquerre) { // Only vertical movement palette 1
                    paleta1Y += dy;
                    if (paleta1Y < 0) paleta1Y = 0;
                    if (paleta1Y + alt > getHeight()) paleta1Y = getHeight() - alt;
                } else { // Only vertical movement palette 2
                    paleta2Y += dy;
                    if (paleta2Y < 0) paleta2Y = 0;
                    if (paleta2Y + alt > getHeight()) paleta2Y = getHeight() - alt;
                }
                mLastTouchX = x;
                mLastTouchY = y; // Remember this touch position for the next move event
                invalidate();
                break;
            }
        }
        return true;
    }


}
