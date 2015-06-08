package com.zhang_000.archerguygame.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {

    protected Vector2 position;
    protected Vector2 velocity;
    protected Vector2 acceleration;

    protected int width;
    protected int height;

    protected Vector2 deltaPos;
    protected Vector2 deltaVel;

    public GameObject(Vector2 position, Vector2 velocity, Vector2 acceleration) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public abstract void update(float delta);

    public abstract void render(float runTime, SpriteBatch batch);

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

}
