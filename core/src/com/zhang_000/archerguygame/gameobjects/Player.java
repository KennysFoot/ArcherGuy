package com.zhang_000.archerguygame.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class Player extends GameObject {

    private Animation standingAnimation;
    private Animation movingAnimation;
    private Animation upAnimation;

    private Vector2 leftEyePosition;
    private int groundLevel;
    private State state;
    private int score;

    private Polygon hitBox = new Polygon();

    private enum State {
        GOING_UP, GOING_DOWN, ON_GROUND, DEAD
    }

    public Player(Vector2 position, Vector2 velocity, Vector2 acceleration) {
        //Assign position, velocity, and acceleration
        super(position, velocity, acceleration);
        super.width = 30;
        super.height = 29;
        score = 0;

        //Store animation references
        standingAnimation = AssetLoader.AGFrontAnimation;
        movingAnimation = AssetLoader.AGMovingAni;
        upAnimation = AssetLoader.AGUpAni;

        //Set state and position of the left eye
        state = State.ON_GROUND;
        leftEyePosition = new Vector2(17, position.y + 7);

        //Set up the hit box
        hitBox.setPosition(position.x, position.y);
        hitBox.setOrigin(0, 0);
        float[] vertices = {0, 0, width, 0, width, height, 0, height};
        hitBox.setVertices(vertices);
    }

    @Override
    public void update(float delta) {
        //Update velocity
        deltaVel = acceleration.cpy().scl(delta);
        velocity.add(deltaVel);

        //Cap velocity
        if (velocity.y > 175) {
            velocity.y = 175;
        } else if (velocity.y < -175) {
            velocity.y = -175;
        }

        //Update position
        deltaPos = velocity.cpy().scl(delta);
        position.add(deltaPos);

        //Cap position (Don't let him fall through the ground or go off the top of the screen)
        if (position.y + height > groundLevel) {
            position.y = groundLevel - height;
            velocity.y = 0;
            state = State.ON_GROUND;
        } else if (position.y < 0) {
            position.y = 0;
            velocity.y = 0;
        } else {
            //Update left eye position and hit box only if player is not on the ground
            //or at the top of the screen
            leftEyePosition.add(deltaPos);
            hitBox.setPosition(position.x, position.y);
        }
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        if (state == State.ON_GROUND) {
            batch.draw(movingAnimation.getKeyFrame(runTime), position.x, position.y, width, height);
        } else if (state == State.GOING_UP) {
            batch.draw(upAnimation.getKeyFrame(runTime), position.x, position.y, width, height);
        } else if (state == State.GOING_DOWN){
            batch.draw(AssetLoader.archerGuyFront1, position.x, position.y, width, height);
        } else if (state == State.DEAD) {
            batch.draw(AssetLoader.archerGuyFront2, position.x, position.y, width / 2, height, width, height,
                    1, 1, 80f);
        }
    }

    public void setGroundLevel(int gLev) {
        groundLevel = gLev;
    }

    public void goUp() {
        acceleration.y = -150;
        state = State.GOING_UP;
    }

    public void goDown() {
        acceleration.y = 150;
        state = State.GOING_DOWN;
    }

    public Vector2 getLeftEyePosition() {
        return leftEyePosition;
    }

    public void incrementScore(int deltaScore) {
        score += deltaScore;
    }

    public int getScore() {
        return score;
    }

    public Polygon getHitBox() {
        return hitBox;
    }

    public void stop() {
        velocity.set(0, 0);
        acceleration.set(0, 0);
        state = State.DEAD;
    }

}
