package com.zhang_000.archerguygame.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameobjects.GameObject;

public abstract class PowerUp extends GameObject {

    protected float POWER_UP_LENGTH;
    protected float timeActive;
    protected TextureRegion image;
    protected PowerUpState state;
    protected Polygon hitPolygon = new Polygon();

    public enum PowerUpState {
        ON_SCREEN, ACTIVE
    }

    public PowerUp() {
        state = PowerUpState.ON_SCREEN;
        timeActive = 0;
    }

    public PowerUp(Vector2 position, Vector2 velocity, Vector2 acceleration) {
        super(position, velocity, acceleration);
        state = PowerUpState.ON_SCREEN;
        timeActive = 0;
    }

    public abstract void update(float delta, float runTime);

    public abstract void render(float runTime, SpriteBatch batch);

    protected abstract void activate();

    protected abstract void deactivate();

    public abstract void playActivationSound();

    public boolean finished() {
        return timeActive > POWER_UP_LENGTH;
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
