package com.zhang_000.archerguygame.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.screens.GameScreen;

public class Player extends GameObject {

    Animation standingAnimation;
    Animation movingAnimation;

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

        //Update position
        deltaPos = velocity.cpy().scl(delta);
        position.add(deltaPos);

        //Cap position (Don't let him fall through the ground)
        if (position.y + height > GameScreen.GAME_HEIGHT) {
            position.y = GameScreen.GAME_HEIGHT - height;
        }
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        batch.draw(standingAnimation.getKeyFrame(runTime), position.x, position.y, width, height);
    }

}
