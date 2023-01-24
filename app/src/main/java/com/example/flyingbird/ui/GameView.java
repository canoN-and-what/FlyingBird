package com.example.flyingbird.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.flyingbird.R;
import com.example.flyingbird.model.Bird;
import com.example.flyingbird.model.Pipe;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final Bitmap background;
    private final Bitmap button;
    private static final int FPS = 6000;
    public static int score = -1;
    private SurfaceHolder surfaceHolder;
    private DrawThread drawThread;
    private Bird bird;
    private Pipe pipe;
    private final Paint gameTextColor = new Paint();
    private final Rect rect = new Rect(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight());

    {
        gameTextColor.setTextSize(60);
        gameTextColor.setColor(Color.WHITE);
    }

    private final Paint pauseTextColor = new Paint();

    {
        pauseTextColor.setTextSize(160);
        pauseTextColor.setColor(Color.WHITE);
    }

    public GameView(Context context) {
        super(context);
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.back);
        button = BitmapFactory.decodeResource(context.getResources(), R.drawable.button);
        drawThread = new DrawThread();
        getHolder().addCallback(this);
    }

    private void init() {
        bird = new Bird(getContext(), 200, getHeight() / 2f);
        pipe = new Pipe(getContext(), getHeight(), getWidth());
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        init();
        drawThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    private void drawFrames(Canvas canvas) {
        Rect backgroundRect = new Rect(0, 0, getWidth(), getHeight());
        canvas.drawBitmap(background, null, backgroundRect, null);
        canvas.drawBitmap(bird.getSprite(), bird.x, bird.y, null);
        canvas.drawBitmap(pipe.getTopPipe(), pipe.x, 0, null);
        canvas.drawBitmap(pipe.getBottomPipe(), pipe.x, getHeight() - pipe.getBottomPipe().getHeight(), null);
        canvas.drawText("Score: " + score, 70, 200, gameTextColor);
    }

    private void update() {
        bird.update();
        pipe.update();
        if (pipe.isCollision(bird) || bird.y <= 0 || bird.y >= getHeight()) {
            drawThread.running = false;
        }
    }
    private void pause(){
        Canvas canvas = new Canvas();
        canvas = surfaceHolder.lockCanvas();
        canvas.drawText("Retry", getWidth() /2f, getHeight() / 2f, pauseTextColor);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        bird.fly();
        if (!drawThread.running) {
            score = -1;
            drawThread = new DrawThread();
//            pause();
//            if(rect.contains((int)(event.getX()), (int) (event.getY()))){
//                Log.i("RRR", "onTouchEvent: contains! ");
//                init();
//                drawThread.start();
//            }
            init();
            drawThread.start();
        }
        return super.onTouchEvent(event);

    }

    private class DrawThread extends Thread {
        private volatile boolean running = true;

        @Override
        public void run() {
            Canvas canvas;
            while (running) {
                canvas = surfaceHolder.lockCanvas();
                try {
                    Thread.sleep(1000 / FPS);
                    drawFrames(canvas);
                    update();
                } catch (Exception e) {
                    Log.e("RRR", "run: ", e);
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
