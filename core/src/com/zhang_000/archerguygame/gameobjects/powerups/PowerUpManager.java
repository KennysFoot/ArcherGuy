package com.zhang_000.archerguygame.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.gameworld.GameWorld;

public class PowerUpManager {

    private GameWorld world;

    private Array<PowerUp> powerUps = new Array<PowerUp>();
    private float runTime;

    private static final short TIME_BETWEEN_POWER_UPS = 35;
    private static final short LAST_POWER_UP_NUMBER = 3;
    private static final int INFINITE_ARROWS = 0;
    private static final int EXTRA_LIFE = 1;
    private static final int SHIELD = 2;
    private static final int EXPLODING_ARROWS = 3;

    public PowerUpManager(GameWorld world) {
        this.world = world;
        runTime = 0;

        //TEST
       // powerUps.add(new PowerUpExplodingArrows(world));
        powerUps.add(new PowerUpShield(world));
        powerUps.add(new PowerUpExplodingArrows(world));
    }

    public void update(float delta) {
        //Create new power up on screen every TIME_BETWEEN_POWER_UPS seconds
        if ((int) runTime > TIME_BETWEEN_POWER_UPS) {
            switch(MathUtils.random(LAST_POWER_UP_NUMBER)) {
                case INFINITE_ARROWS:
                    powerUps.add(new PowerUpInfiniteArrows());
                    break;

                case EXTRA_LIFE:
                    powerUps.add(new PowerUpExtraLife(world));
                    break;

                case SHIELD:
                    powerUps.add(new PowerUpShield(world));
                    break;

                case EXPLODING_ARROWS:
                    powerUps.add(new PowerUpExplodingArrows(world));
                    break;

                default:
                    break;
            }
            runTime = 0;
        }

        //Iterate through powerUps array, updating each power up
        for (PowerUp pow : powerUps) {
            pow.update(delta, runTime);

            //Deactivate and remove the power up if it has passed its use time
            if (pow.finished()) {
                pow.deactivate();
                powerUps.removeValue(pow, false);
            }

            //Remove the power up if it is off the screen past the player
            if (pow.getX() < -PowerUp.LENGTH) {
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
