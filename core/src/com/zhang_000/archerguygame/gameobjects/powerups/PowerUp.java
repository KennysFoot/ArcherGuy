package com.zhang_000.archerguygame.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameobjects.GameObject;

public abstract class PowerUp extends GameObject {

    public static final int LENGTH = 12;

    protected float POWER_UP_LENGTH;
    protected float timeActive;
    protected TextureRegion image;
    protected PowerUpState state;
    protected Polygon hitPolygon = new Polygon();

    protected boolean paused;

    public enum PowerUpState {
        ON_SCREEN, ACTIVE
    }

    public PowerUp() {
        state = PowerUpState.ON_SCREEN;
        timeActive = 0;
        paused = false;
    }

    public PowerUp(Vector2 position, Vector2 velocity, Vector2 acceleration) {
        super(position, velocity, acceleration);
        state = PowerUpState.ON_SCREEN;
        timeActive = 0;
        paused = false;
    }

    public abstract void update(float delta, float runTime);

    protected void updateOnScreen(float delta) {
        deltaPos = velocity.cpy().scl(delta);
        position.add(deltaPos);
        hitPolygon.setPosition(position.x, position.y);
    }

    public abstract void render(float runTime, SpriteBatch batch);

    protected void setUpHitPolygon() {
        hitPolygon.setPosition(position.x, position.y);
        hitPolygon.setOrigin(0, 0);
                            //top       //right             //bottom             //left
        float[] vertices = {3, 0, 8, 0, width, 3, width, 8, 8, height, 3, height, 0, 8, 0, 3};
        hitPolygon.setVertices(vertices);
    }

    protected abstract void activate();

    protected abstract void deactivate();

    public abstract void playActivationSound();

    public boolean finished() {
        return timeActive > POWER_UP_LENGTH;
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    public void setState(PowerUpState state) {
        this.state = state;
    }

    public PowerUpState getState() {
        return state;
    }

    public Polygon getHitPolygon() {
        return hitPolygon;
    }

}
