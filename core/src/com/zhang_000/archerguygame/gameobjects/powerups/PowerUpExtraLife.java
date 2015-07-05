package com.zhang_000.archerguygame.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.screens.GameScreen;

public class PowerUpExtraLife extends PowerUp {

    private GameWorld world;

    public PowerUpExtraLife(GameWorld world) {
        super();
        this.world = world;

        width = height = PowerUp.LENGTH;
        POWER_UP_LENGTH = 100;

        position =  new Vector2(GameScreen.GAME_WIDTH, MathUtils.random(0, GameWorld.GROUND_LEVEL - height));
        velocity = GameWorld.LATERAL_MOVE_SPEED.cpy().scl(7);
        acceleration = GameWorld.NO_ACCELERATION;

        setUpHitPolygon();
    }

    @Override
    public void update(float delta, float runTime) {
        if (!paused) {
            switch (state) {
                case ON_SCREEN:
                    updateOnScreen(delta);
                    break;

                case ACTIVE:
                    activate();

                    //We want to remove this power up from the manager after activate() is called once
                    //We do this by setting timeActive to be greater than the POWER_UP_LENGTH
                    //When the finished() method is called by the manager, it will return true and this power up
                    // will be removed from the Array
                    timeActive = POWER_UP_LENGTH + 1;

                    break;
            }
        }
    }

    //NOT USED
    @Override
    public void update(float delta) {

    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        batch.draw(AssetLoader.powUpExtraLife, position.x, position.y);
    }

    @Override
    protected void activate() {
        //If the player gets this power up, he gains an extra life
        world.getPlayer().incrementLives(1);
    }

    //NOT USED
    @Override
    protected void deactivate() {

    }
    @Override
    public void playActivationSound() {
        //Sound for this power up is taken care of in the player's increment lives method
    }

}
