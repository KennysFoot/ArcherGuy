package com.zhang_000.archerguygame.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class Player extends GameObject {

    private Animation standingAnimation;
    private Animation movingAnimation;

    private int groundLevel;

    public Player(Vector2 position, Vector2 velocity, Vector2 acceleration) {
        //Assign position, velocity, and acceleration
        super(position, velocity, acceleration);

        //Store animation references
        standingAnimation = AssetLoader.AGFrontAnimation;
        movingAnimation = AssetLoader.AGMovingAni;

        super.width = 30;
        super.height = 29;
    }

    @Override
    public void update(float delta) {
        //Update velocity
        deltaVel = acceleration.cpy().scl(delta);
        velocity.add(deltaVel);

        //Cap velocity
        if (velocity.y > 175) {
            velocity.y = 175;
        }

        //Update position
        deltaPos = velocity.cpy().scl(delta);
        position.add(deltaPos);

        //Cap position (Don't let him fall through the ground)
        if (position.y + height > groundLevel) {
            position.y = groundLevel - height;
        }
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        batch.draw(movingAnimation.getKeyFrame(runTime), position.x, position.y, width, height);
    }

    public void setGroundLevel(int gLev) {
        groundLevel = gLev;
    }

}
