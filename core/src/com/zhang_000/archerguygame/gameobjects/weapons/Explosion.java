package com.zhang_000.archerguygame.gameobjects.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameobjects.GameObject;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class Explosion extends GameObject {

    public static final int FRAMES = 40;
    public static final int WIDTH = 96;

    private boolean animationFinished;
    private float animationRunTime;

    private Circle boundingCircle;

    public Explosion(Vector2 position) {
        this.position = position;
        animationFinished = false;
        animationRunTime = 0;
        boundingCircle = new Circle(position.x + 48, position.y + 48, 36);
    }

    @Override
    public void update(float delta) {
        if (!animationFinished) {
            animationRunTime += delta;
            if (AssetLoader.animationExplosion.isAnimationFinished(animationRunTime)) {
                animationFinished = true;
            }
        }

        int frameNum = AssetLoader.animationExplosion.getKeyFrameIndex(animationRunTime);
        if (frameNum > 17 && frameNum < 26) {
            boundingCircle.set(position.x + 50, position.y + 50, 25);
        } else if (frameNum > 26) {
            //No more hit detection
            //Make the radius super small
            boundingCircle.setRadius(0.00001f);
        }
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        if (!animationFinished) {
            batch.draw(AssetLoader.animationExplosion.getKeyFrame(animationRunTime), position.x, position.y);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    public boolean isAnimationFinished() {
        return animationFinished;
    }

    public Circle getBoundingCircle() {
        return boundingCircle;
    }

}
