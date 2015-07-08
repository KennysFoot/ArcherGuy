package com.zhang_000.archerguygame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class Player extends GameObject {

    private static float SCALE_LIVES = 0.5f;
    private ShapeRenderer shapeRenderer;

    private Animation standingAnimation;
    private Animation movingAnimation;
    private Animation upAnimation;

    private Vector2 leftEyePosition;
    private int groundLevel;
    private State state;
    private int lives;
    private int killScore;
    private float timeScore;

    private Polygon hitBox = new Polygon();

    private boolean paused = false;

    //POWER UPS
    private boolean shieldActivated; //Shield lasts 10 seconds
    private Circle shield;           //Start flickering at 7 seconds
    private boolean flicker = false; //Shield won't be rendered when flicker is set to true

    private enum State {
        GOING_UP, GOING_DOWN, ON_GROUND, DEAD
    }

    public Player(Vector2 position, Vector2 velocity, Vector2 acceleration, ShapeRenderer shapeRenderer) {
        //Assign position, velocity, and acceleration
        super(position, velocity, acceleration);
        super.width = 28;
        super.height = 30;
        this.shapeRenderer = shapeRenderer;
        killScore = 0;
        timeScore = 0;
        lives = 3;

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
        float[] vertices = {0, 7, 12, 0, 18, 0, width, 7, //hat
                23, 7, 23, 22, 20, height, 9, height, 5, 22, 5, 7}; //body
        hitBox.setVertices(vertices);

        shieldActivated = false;

        shield = new Circle(position.x + width / 2, position.y + height / 2, 21);
    }

    @Override
    public void update(float delta) {
        if (!paused) {
            //Increment timeScore
            timeScore += delta / 2;

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

                //Make sure state isn't set to GOING_UP before changing it to ON_GROUND
                if (state == State.GOING_DOWN) {
                    state = State.ON_GROUND;
                }
            } else if (position.y < 0) {
                position.y = 0;
                velocity.y = 0;
            }

            leftEyePosition.set(17, position.y + 7);
            hitBox.setPosition(position.x, position.y);
            if (shieldActivated) {
                shield.setPosition(position.x + width / 2, position.y + height / 2);
            }
        }
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        if (state == State.ON_GROUND) {
            batch.draw(movingAnimation.getKeyFrame(timeScore), position.x, position.y, width, height);
        } else if (state == State.GOING_UP) {
            batch.draw(upAnimation.getKeyFrame(timeScore), position.x, position.y, width, height);
        } else if (state == State.GOING_DOWN) {
            batch.draw(AssetLoader.archerGuyFront1, position.x, position.y, width, height);
        } else if (state == State.DEAD) {
            batch.draw(AssetLoader.archerGuyFront2, position.x, position.y, width / 2, height, width, height,
                    1, 1, 80f);
        }

        //Draw the lives remaining onto the screen
        for (int i = 0; i < lives; i++) {
            batch.draw(AssetLoader.archerGuyFront2, 2 + i * 7.5f, 2, 0, 0,
                    width * SCALE_LIVES, height * SCALE_LIVES, SCALE_LIVES, SCALE_LIVES, 0);
        }

        //Render the shield if it is activated and flicker is set to false
        if (shieldActivated && !flicker) {
            //TO ENABLE BLENDING SPRITE BATCH MUST BE STOPPED
            batch.end();

            //Enable blending
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            //Render the shield (a translucent circle around the player)
            shapeRenderer.setAutoShapeType(true);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(156 / 255f, 120 / 255f, 88 / 255f, 0.4f);
            shapeRenderer.circle(shield.x, shield.y, shield.radius);

            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            //RE-ENABLE THE SPRITE BATCH
            batch.begin();
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

    public void incrementKillScore(int deltaScore) {
        killScore += deltaScore;
    }

    public void incrementLives(int deltaLives) {
        lives += deltaLives;

        //Play the lose_life sound if not dead yet but a life is lost
        //If 0 lives reached, different sound will play
        if (lives > 0 && deltaLives < 0) {
            AssetLoader.playSound(AssetLoader.soundLoseLife, 1);
        } else if (deltaLives > 0) {
            AssetLoader.playSound(AssetLoader.soundGainLife, 0.65f);
        }
    }

    public int getScore() {
        return killScore + (int) timeScore;
    }

    public Polygon getHitBox() {
        return hitBox;
    }

    public void stop() {
        velocity.set(0, 0);
        acceleration.y = 150;
        state = State.DEAD;
    }

    public int getLives() {
        return lives;
    }

    public void setShieldActivated(boolean b) {
        shieldActivated = b;
    }

    public Circle getShield() {
        return shield;
    }

    public boolean isShieldActivated() {
        return shieldActivated;
    }

    public void setFlicker(boolean b) {
        flicker = b;
    }

}
