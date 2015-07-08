package com.zhang_000.archerguygame.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.helper_classes.InputHandlerGame;
import com.zhang_000.archerguygame.screens.GameScreen;

public class PowerUpInfiniteArrows extends PowerUp {

    private final float MEDIAN;

    public PowerUpInfiniteArrows(Vector2 position, Vector2 velocity, Vector2 acceleration) {
        super(position, velocity, acceleration);
        POWER_UP_LENGTH = 5.0f;
        image = AssetLoader.powUpInfiniteArrows;
        width = AssetLoader.powUpInfiniteArrows.getRegionWidth();
        height = AssetLoader.powUpInfiniteArrows.getRegionHeight();
        MEDIAN = super.position.y;

        hitPolygon.setPosition(position.x, position.y);
        hitPolygon.setOrigin(0, 0);
                            //top       //right             //bottom             //left
        float[] vertices = {3, 0, 8, 0, width, 3, width, 8, 8, height, 3, height, 0, 8, 0, 3};
        hitPolygon.setVertices(vertices);
    }

    public PowerUpInfiniteArrows() {
        super();

        super.position =  new Vector2(GameScreen.GAME_WIDTH, MathUtils.random(30, GameWorld.GROUND_LEVEL - 50));
        super.velocity = GameWorld.LATERAL_MOVE_SPEED.cpy().scl(5);
        super.acceleration = GameWorld.NO_ACCELERATION;
        POWER_UP_LENGTH = 5.0f;

        image = AssetLoader.powUpInfiniteArrows;
        width = AssetLoader.powUpInfiniteArrows.getRegionWidth();
        height = AssetLoader.powUpInfiniteArrows.getRegionHeight();

        MEDIAN = super.position.y;

        setUpHitPolygon();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void update(float delta, float runTime) {
        if (!paused) {
            switch (state) {
                case ON_SCREEN:
                    updateOnScreen(delta);

                    //UP AND DOWN MOVEMENT USING COSINE AND RUNTIME
                    position.y = MEDIAN + 20 * MathUtils.cos(2 * runTime);

                    break;

                case ACTIVE:
                    //Call execute method to activate the power up
                    //Deactivate is called by the power up manager after timeActive has exceeded POWER_UP_LENGTH
                    if (timeActive == 0) {
                        activate();
                        playActivationSound();
                    }

                    timeActive += delta;

                    break;
            }
        }
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        batch.draw(AssetLoader.powUpInfiniteArrows, position.x, position.y);
    }

    @Override
    protected void activate() {
        InputHandlerGame.setInfiniteArrowsActivated(true);
    }

    @Override
    protected void deactivate() {
        InputHandlerGame.setInfiniteArrowsActivated(false);
    }

    @Override
    public void playActivationSound() {
        AssetLoader.playSound(AssetLoader.soundInfArrows, 1);
    }

}
