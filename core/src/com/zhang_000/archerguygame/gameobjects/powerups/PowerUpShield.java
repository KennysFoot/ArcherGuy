package com.zhang_000.archerguygame.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.screens.GameScreen;

public class PowerUpShield extends PowerUp {

    //The shield power ups velocity will be either moving left and up || left and down
    private static final Vector2 VELOCITY_UP = new Vector2(-40, -25);
    private static final Vector2 VELOCITY_DOWN = new Vector2(-40, 25);

    private Player player;

    public PowerUpShield(GameWorld world) {
        super();
        player = world.getPlayer();
        width = height = PowerUp.LENGTH;
        POWER_UP_LENGTH = 10;

        position = new Vector2(GameScreen.GAME_WIDTH, MathUtils.random(0, GameWorld.GROUND_LEVEL - height));
        //0 = up and 1 = down
        int initialVelocityUpOrDown = MathUtils.random(0, 1);
        if (initialVelocityUpOrDown == 0) {
            velocity = VELOCITY_UP;
        } else {
            velocity = VELOCITY_DOWN;
        }
        acceleration = GameWorld.NO_ACCELERATION;

        setUpHitPolygon();
    }

    @Override
    public void update(float delta, float runTime) {
        switch(state) {
            case ON_SCREEN:
                //Update position
                deltaPos = velocity.cpy().scl(delta);
                position.add(deltaPos);

                //Check if top of screen or ground is hit
                if (position.y < 0) {
                    //If the top of the screen is hit, change the velocity so that the power up is
                    //going down
                    position.y = 0;
                    velocity = VELOCITY_DOWN;
                } else if (position.y > GameWorld.GROUND_LEVEL - height) {
                    //Do the opposite for the power up hitting the ground
                    position.y = GameWorld.GROUND_LEVEL - height;
                    velocity = VELOCITY_UP;
                }

                //Update hit polygon
                hitPolygon.setPosition(position.x, position.y);
                break;

            case ACTIVE:
                if (timeActive == 0) {
                    activate();
                    playActivationSound();
                }

                timeActive += delta;
                break;
        }
    }

    @Override
    public void update(float delta) {
        //OTHER UPDATE METHOD USED INSTEAD
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        batch.draw(AssetLoader.powUpShield, position.x, position.y);
    }

    @Override
    protected void activate() {
        player.setShieldActivated(true);
    }

    @Override
    protected void deactivate() {
        player.setShieldActivated(false);
    }

    @Override
    public void playActivationSound() {
        AssetLoader.soundShieldActivated.play();
    }

}
