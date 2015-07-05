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

    //For pausing the game
    private Vector2 savedAccel = new Vector2(0, 0);
    private Vector2 savedVel = new Vector2(0, 0);

    public GameObject() {

    }

    public GameObject(Vector2 position, Vector2 velocity, Vector2 acceleration) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public abstract void update(float delta);

    public abstract void render(float runTime, SpriteBatch batch);

    public void pause() {
        savedAccel.set(acceleration.cpy());
        savedVel.set(velocity.cpy());
        acceleration.set(0, 0);
        velocity.set(0, 0);
    }

    public void resume() {
        acceleration.set(savedAccel.cpy());
        velocity.set(savedVel.cpy());
    }

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
