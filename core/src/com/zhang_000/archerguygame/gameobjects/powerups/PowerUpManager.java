package com.zhang_000.archerguygame.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class PowerUpManager {

    private Array<PowerUp> powerUps = new Array<PowerUp>();
    private float runTime;
    private int tickerNewPowerUp;

    private static final short TOTAL_NUMBER_POWER_UPS = 1;
    private static final int INFINITE_ARROWS = 0;

    public PowerUpManager() {
        runTime = 0;
        tickerNewPowerUp = 0;
    }

    public void update(float delta) {
        //Increment runTime variable
        runTime += delta;

        //Update the ticker
        if ((int) runTime > tickerNewPowerUp) {
            ++tickerNewPowerUp;
        }

        //Create new power up on screen every 50 seconds
        if (tickerNewPowerUp % 51 == 0) {
            switch(MathUtils.random(TOTAL_NUMBER_POWER_UPS)) {
                case INFINITE_ARROWS:
                    powerUps.add(new InfiniteArrows());
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

            //Remove the power up if its time has finished
            if (pow.finished()) {
                pow.deactivate();
                powerUps.removeValue(pow, false);
            }

            //Remove the power up if it is off the screen past the player
            if (pow.getX() < -pow.getWidth()) {
                powerUps.removeValue(pow, false);
            }
        }

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
