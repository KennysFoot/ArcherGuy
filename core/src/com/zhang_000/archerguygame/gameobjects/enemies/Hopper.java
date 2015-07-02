package com.zhang_000.archerguygame.gameobjects.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class Hopper extends Enemy {

    public static final int WIDTH = 22;
    public static final int HEIGHT_AIR = 23;
    public static final int HEIGHT_GROUND = 21;

    public static final int SCORE = 3;

    private HopperState state;

    public enum HopperState {
        ON_GROUND, IN_AIR
    }

    public Hopper(Vector2 position, Vector2 velocity, Vector2 acceleration) {
        super(position, velocity, acceleration);

        state = HopperState.ON_GROUND;
        score = SCORE;

        hitPolygon.setPosition(position.x, position.y);
        hitPolygon.setOrigin(0, 0);
        float[] vertices = {3, 6, 9, 0, 12, 0, 19, 7, 19, 13, WIDTH, 13, WIDTH, 16, 19, 16,
                            19, 18, 16, 18, 16, HEIGHT_AIR, 14, HEIGHT_AIR, 15, 18, 7, 18,
                            8, HEIGHT_AIR, 5, HEIGHT_AIR, 5, 18, 3, 18, 3, 16, 0, 16, 0, 12, 3, 12};
        hitPolygon.setVertices(vertices);
    }

    @Override
    public void update(float delta) {
        //If in the air, calculate vectors normally
        if (state == HopperState.IN_AIR) {
            deltaVel = acceleration.cpy().scl(delta);
            velocity.add(deltaVel);

            deltaPos = velocity.cpy().scl(delta);
            position.add(deltaPos);

            //Check if the hopper has now reached the ground
            if (position.y + HEIGHT_AIR > GameWorld.GROUND_LEVEL) {
                //Set the position's y value so that the hopper is just on the ground
                position.y = GameWorld.GROUND_LEVEL - HEIGHT_GROUND;
                state = HopperState.ON_GROUND;
            }
        } else {
            velocity.y = -150;
            state = HopperState.IN_AIR;
        }

        hitPolygon.setPosition(position.x, position.y);
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        if (state == HopperState.ON_GROUND) {
            batch.draw(AssetLoader.hopperOnGround, position.x, position.y);
        } else if (state == HopperState.IN_AIR) {
            batch.draw(AssetLoader.hopperInAir, position.x, position.y);
        }
    }

    public HopperState getState() {
        return state;
    }

}
