package com.example.flyingbird.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.flyingbird.R;
import com.example.flyingbird.ui.GameView;

public class Pipe extends GameObject {

    private Bitmap topPipe;
    private Bitmap bottomPipe;

    private static final float X_SPEED = 50;
    private static final float SPACER_SIZE = 300;

    private final float height;
    private final float width;

    public Pipe(Context context, float height, float width) {
        super(width, 0);
        this.height = height;
        this.width = width;
        topPipe = BitmapFactory.decodeResource(context.getResources(), R.drawable.pipe_rotated);
        bottomPipe = BitmapFactory.decodeResource(context.getResources(), R.drawable.pipe);
        generatePipes();
    }

    private void generatePipes() {
        y = random(height / 4f, height * 3 / 4f);

        topPipe = Bitmap.createScaledBitmap(topPipe, 200, (int) (y - SPACER_SIZE), false);
        bottomPipe = Bitmap.createScaledBitmap(bottomPipe, 200, (int) (height - y - SPACER_SIZE), false);
        GameView.score +=1;
    }

    @Override
    public void update() {
        x -= X_SPEED;
        if (x <= -bottomPipe.getWidth()) {
            x = width;
            generatePipes();
        }
    }

    public boolean isCollision(GameObject gameObject) {
        if (x - 50 < gameObject.x && x + bottomPipe.getWidth() > gameObject.x) {

            if (gameObject.y < topPipe.getHeight()) return true;
            return gameObject.y - 10 > height - bottomPipe.getHeight();
        }
        return false;
    }

    private float random(float min, float max) {
        return (float) (min + (Math.random() * (max - min)));
    }

    public Bitmap getTopPipe() {
        return topPipe;
    }

    public Bitmap getBottomPipe() {
        return bottomPipe;
    }
}
