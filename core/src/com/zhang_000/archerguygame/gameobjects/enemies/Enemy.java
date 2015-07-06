package com.zhang_000.archerguygame.gameobjects.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameobjects.GameObject;

public abstract class Enemy extends GameObject {

    protected int score;
    protected boolean paused = false;

    protected Polygon hitPolygon = new Polygon();

    public Enemy(Vector2 position, Vector2 velocity, Vector2 acceleration) {
        super(position, velocity, acceleration);
    }

    public abstract void update(float delta);

    public abstract void render(float runTime, SpriteBatch batch);

    @Override
    public void pause() {
        super.pause();
        paused = true;
    }

    @Override
    public void resume() {
        super.resume();
        paused = false;
    }

    public Polygon getHitPolygon() {
        return hitPolygon;
    }

    public int getScore() {
        return score;
    }

}
