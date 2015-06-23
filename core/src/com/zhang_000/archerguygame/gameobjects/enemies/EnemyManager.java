package com.zhang_000.archerguygame.gameobjects.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.screens.GameScreen;

public class EnemyManager {

    private GameWorld world;

    private Array<Wiggler> wigglers = new Array<Wiggler>();
    private float wigglerTimer = 0;

    private Player player;

    public EnemyManager(GameWorld world) {
        this.world  = world;
        player = world.getPlayer();
    }

    public void updateEnemies(float delta) {
        wigglerTimer += delta;

        //Spawn a new wiggler every 3 seconds
        if (wigglerTimer > 3) {
            float speedFactor = 1;

            //Increase the wigglers speed as the players score increases
            if (player.getScore() > 1) {
                speedFactor += player.getScore() / 6;
                //Cap speed increase to 5 times the initial speed
                if (speedFactor > 7) {
                    speedFactor = 7;
                }
            }

            wigglers.add(new Wiggler(new Vector2(GameScreen.GAME_WIDTH,
                    MathUtils.random(GameWorld.GROUND_LEVEL - AssetLoader.wiggler1.getRegionHeight())),
                    new Vector2(GameWorld.LATERAL_MOVE_SPEED.x * speedFactor, 0), GameWorld.NO_ACCELERATION));

            //Reset the timer to 0 after a new wiggler is spawned
            wigglerTimer = 0;
        }

        //Update positions of each wiggler
        for (Wiggler w : wigglers) {
            w.update(delta);
        }

    }

    public Array<Wiggler> getWigglers() {
        return wigglers;
    }

}
