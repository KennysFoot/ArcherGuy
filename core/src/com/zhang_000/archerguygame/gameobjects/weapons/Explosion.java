package com.zhang_000.archerguygame.gameobjects.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameobjects.GameObject;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class Explosion extends GameObject {

    private boolean animationFinished;
    private float animationRunTime;

    private Circle boundingCircle;

    public Explosion(Vector2 position) {
        this.position = position;
        animationFinished = false;
        animationRunTime = 0;
        boundingCircle = new Circle(position.x + 31, position.y + 31, 22);
    }

    @Override
    public void update(float delta) {
        if (!animationFinished) {
            animationRunTime += delta;
            if (AssetLoader.animationExplosion.isAnimationFinished(animationRunTime)) {
                animationFinished = true;
            }
        }
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        if (!animationFinished) {
            batch.draw(AssetLoader.animationExplosion.getKeyFrame(animationRunTime), position.x, position.y);
        }
    }

    public boolean isAnimationFinished() {
        return animationFinished;
    }

    public Circle getBoundingCircle() {
        return boundingCircle;
    }

}
