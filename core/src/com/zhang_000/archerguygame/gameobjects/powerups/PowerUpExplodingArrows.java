package com.zhang_000.archerguygame.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.screens.GameScreen;

public class PowerUpExplodingArrows extends PowerUp {

    private GameWorld world;
    private Player player;

    public PowerUpExplodingArrows(GameWorld world) {
        super();
        this.world = world;
        player = world.getPlayer();
        width = height = PowerUp.LENGTH;
        POWER_UP_LENGTH = 10;

        position = new Vector2(GameScreen.GAME_WIDTH, MathUtils.random(0, GameWorld.GROUND_LEVEL - height));

        //TEMP VELOCITY
        velocity = GameWorld.LATERAL_MOVE_SPEED.cpy().scl(8);

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
                    if (timeActive == 0) {
                        activate();
                        playActivationSound();
                    }

                    timeActive += delta;

                    if (timeActive > 8 && ((int) (timeActive * 10)) % 3 == 0) {
                        player.setExplodingArrowsFlicker(true);
                    } else {
                        player.setExplodingArrowsFlicker(false);
                    }

                    break;
            }
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
        player.setExplodingArrowsActivated(true);
    }

    @Override
    protected void deactivate() {
        world.getWeaponManager().setExplodingArrowsActivated(false);
        player.setExplodingArrowsActivated(false);
        player.setExplodingArrowsFlicker(false);
    }

    @Override
    public void playActivationSound() {
        AssetLoader.playSound(AssetLoader.soundExplodingArrowHit, 1);
    }

}
