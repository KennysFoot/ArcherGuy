package com.zhang_000.archerguygame.gameobjects.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class Arrow extends Weapon {

    public static final int VELOCITY_MAGNITUDE = 325;
    public static final float RELOAD_TIME  = 0.3f;

    protected boolean isOnGround = false;
    protected float timeOnGround = 0;

    private boolean paused = false;

    public Arrow(Vector2 position, Vector2 velocity, Vector2 acceleration, float rotation) {
        super(position, velocity, acceleration);
        super.rotation = rotation;
        super.width = AssetLoader.arrow.getRegionWidth();
        super.height = AssetLoader.arrow.getRegionHeight();

        setUpHitPolygon();
    }

    protected void setUpHitPolygon() {
        hitPolygon.setPosition(position.x, position.y);
        hitPolygon.setOrigin(0, 5);
        float[] vertices = new float[]{17, 0, 21, 4, 17, 9};
        hitPolygon.setVertices(vertices);
        hitPolygon.setRotation(rotation);
    }

    @Override
    public void update(float delta) {
        if (!paused) {
            if (!isOnGround) {
                updateVelocity(delta);
                updatePosition(delta);
                updateRotation(delta);
            } else {
                //Update timeOnGround variable if the arrow is on the ground
                timeOnGround += delta;
                updatePosition(delta);
            }
        }
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        batch.draw(AssetLoader.arrow, position.x, position.y, 0, 5, width, height, 1, 1, rotation);
    }

    protected void updateVelocity(float delta) {
        //Update velocity and position
        deltaVel = acceleration.cpy().scl(delta);
        velocity.add(deltaVel);

        //Cap velocity
        if (velocity.y > 250) {
            velocity.y = 250;
        }
    }

    protected void updatePosition(float delta) {
        deltaPos = velocity.cpy().scl(delta);
        position.add(deltaPos);

        //Move hit polygon to match arrow tip
        hitPolygon.setPosition(position.x, position.y);
    }

    protected void updateRotation(float delta) {
        //Update rotation
        float deltaRotation = 6000 * delta / velocity.x;
        rotation += deltaRotation;

        //Don't let arrow rotate so much so that the tip faces backwards
        if (rotation > 90 || rotation < -270) {
            rotation = 90;
        } else {
            //Only rotate polygon further if arrow has not reached the max rotation
            hitPolygon.rotate(deltaRotation);
        }
    }

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

    public void setOnGround(boolean onGround, Vector2 newVelocity) {
        isOnGround = onGround;
        acceleration.y = 0;
        velocity.set(newVelocity);
    }

    public float getTimeOnGround() {
        return timeOnGround;
    }

}
