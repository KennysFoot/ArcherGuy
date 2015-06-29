package com.zhang_000.archerguygame.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.gameworld.GameWorld;

public class PowerUpManager {

    private GameWorld world;

    private Array<PowerUp> powerUps = new Array<PowerUp>();
    private float runTime;
    private int tickerNewPowerUp;

    private static final short LAST_POWER_UP_NUMBER = 3;
    private static final int INFINITE_ARROWS = 0;
    private static final int EXTRA_LIFE = 1;
    private static final int SHIELD = 2;
    private static final int EXPLODING_ARROWS = 3;

    public PowerUpManager(GameWorld world) {
        this.world = world;
        runTime = 0;
        tickerNewPowerUp = 0;

        powerUps.add(new Shield(world));
    }

    public void update(float delta) {
        //Update the ticker
        if ((int) runTime > tickerNewPowerUp) {
            ++tickerNewPowerUp;
        }

        //Create new power up on screen every 50 seconds
        if (tickerNewPowerUp % 51 == 0) {
            switch(MathUtils.random(LAST_POWER_UP_NUMBER)) {
                case INFINITE_ARROWS:
                    powerUps.add(new InfiniteArrows());
                    break;

                case EXTRA_LIFE:
                    powerUps.add(new ExtraLife(world));
                    break;

                case SHIELD:
                    powerUps.add(new Shield(world));
                    break;

                case EXPLODING_ARROWS:

                    break;

                default:
                    break;
            }
            //In the next update, the ticker will most likely not have changed values by itself
            //Increment the ticker so that only one power up is created every 50 seconds
            ++tickerNewPowerUp;
        }

        //Iterate through powerUps array, updating each power up
        for (PowerUp pow : powerUps) {
            pow.update(delta, runTime);

            //Deactivate and remove the power up if it is finished
            if (pow.finished()) {
                pow.deactivate();
                powerUps.removeValue(pow, false);
            }

            //Remove the power up if it is off the screen past the player
            if (pow.getX() < -pow.getWidth()) {
                powerUps.removeValue(pow, false);
            }
        }

        //Increment runTime variable
        runTime += delta;
    }

    public void render(float runTime, SpriteBatch batch) {
        for (PowerUp pow : powerUps) {
            if (pow.getState() == PowerUp.PowerUpState.ON_SCREEN) {
                pow.render(runTime, batch);
            }
        }
    }

    public Array<PowerUp> getPowerUps() {
        return powerUps;
    }

}
