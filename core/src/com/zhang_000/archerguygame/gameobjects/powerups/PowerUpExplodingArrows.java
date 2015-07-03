package com.zhang_000.archerguygame.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.screens.GameScreen;

public class PowerUpExplodingArrows extends PowerUp {

    private GameWorld world;

    public PowerUpExplodingArrows(GameWorld world) {
        super();
        this.world = world;
        width = height = PowerUp.LENGTH;
        POWER_UP_LENGTH = 15;

        position = new Vector2(GameScreen.GAME_WIDTH, MathUtils.random(0, GameWorld.GROUND_LEVEL - height));

        //TEMP VELOCITY
        velocity = GameWorld.LATERAL_MOVE_SPEED.cpy().scl(7);

        acceleration = GameWorld.NO_ACCELERATION;

        setUpHitPolygon();
    }

    @Override
    public void update(float delta, float runTime) {
        switch(state) {
            case ON_SCREEN:
                deltaPos = velocity.cpy().scl(delta);
                position.add(deltaPos);
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
        batch.draw(AssetLoader.powUpExplodingArrows, position.x, position.y);
    }

    @Override
    protected void activate() {
        world.getWeaponManager().setExplodingArrowsActivated(true);
    }

    @Override
    protected void deactivate() {
        world.getWeaponManager().setExplodingArrowsActivated(false);
    }

    @Override
    public void playActivationSound() {
        AssetLoader.soundExplodingArrowHit.play();
    }

}