package com.zhang_000.archerguygame.gameobjects.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameobjects.enemies.bosses.Boss;
import com.zhang_000.archerguygame.gameobjects.enemies.bosses.QueenWiggler;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.screens.GameScreen;

public class EnemyManager {

    //SPAWN OPTIONS
    public static final int SPAWN_ONE_WIGGLER_RANDOM_PLACEMENT = 1;
    public static final int SPAWN_THREE_WIGGLERS = 2;
    public static final int SPAWN_TWO_WIGGLERS = 3;

    private GameWorld world;

    //WIGGLERS
    private Array<Enemy> enemies = new Array<Enemy>();
    private float hopperTimer = 0;
    private int timeBetweenSpawns = 5;
    private float wigglerTimer = 0;
    private float lastTimeSpawningThree;
    private float lastTimeSpawningTwo;

    //BOSSES
    private Array<Boss> bosses = new Array<Boss>();
    private QueenWigglerState queenWigglerState;
    private int timesQueenSpawned;

    private Player player;

    public enum QueenWigglerState {
        NOT_SPAWNED, ON_SCREEN
    }

    public EnemyManager(GameWorld world) {
        this.world = world;
        player = world.getPlayer();
        lastTimeSpawningTwo = lastTimeSpawningThree = 0;
        queenWigglerState = QueenWigglerState.NOT_SPAWNED;
        timesQueenSpawned = 0;
    }

    public void updateEnemies(float delta) {
        updateWigglers(delta);
        updateHoppers(delta);
        updateBosses(delta);
    }

    private void updateWigglers(float delta) {
        wigglerTimer += delta;
        lastTimeSpawningThree += delta;
        lastTimeSpawningTwo += delta;

        //Spawn a new wiggler every 3 seconds
        if (wigglerTimer > 2.75f) {

            //Spawn a Queen Wiggler once a score benchmark has been reached
            if (player.getScore() > 24 + 65 * timesQueenSpawned) {
                spawnQueenWiggler();
                queenWigglerState = QueenWigglerState.ON_SCREEN;
                ++timesQueenSpawned;
            } else {
                spawnWiggler();
            }

            //Reset the timer to 0 after a new wiggler is spawned
            wigglerTimer = 0;
        }

        //Update positions of each wiggler
        for (Enemy e : enemies) {
            if (e instanceof Wiggler) {
                e.update(delta);
                checkIfEnemyIsPastPlayer(e);
            }
        }
    }

    private void updateHoppers(float delta) {
        hopperTimer += delta;

        if (hopperTimer > timeBetweenSpawns) {
            spawnHopper();
            hopperTimer = 0;
            timeBetweenSpawns = MathUtils.random(3, 15);
        }

        for (Enemy e : enemies) {
            if (e instanceof Hopper) {
                e.update(delta);
                checkIfEnemyIsPastPlayer(e);
            }
        }
    }

    private void updateBosses(float delta) {
        for (Boss boss : bosses) {
            boss.update(delta);
        }
    }

    private void checkIfEnemyIsPastPlayer(Enemy e) {
        //Check if any enemies have passed the end of the screen (left side)
        //If one has, remove it from the enemies array and decrease the player's lives by 1
        if (e.getX() < -e.getWidth()) {
            enemies.removeValue(e, false);
            player.incrementLives(-1);

            //After taking away a life, check if the game is done
            checkIfGameIsOver();
        }
    }

    //WIGGLER SPAWNING METHODS
    private void spawnWiggler() {
        float speedFactor = 1;

        //Increase the enemies speed as the players score increases
        if (player.getScore() > 1) {
            speedFactor += player.getScore() / 3;

            //Cap speed increase to 7 times the initial speed
            if (speedFactor > 10) {
                speedFactor = 10;
            }
        }

        //Only spawn enemies is the queen wiggler is not on the screen
        if (queenWigglerState != QueenWigglerState.ON_SCREEN) {
            //Randomly choose one of the spawning options
            int spawnOption = MathUtils.random(0, 3);

            //Only spawn 3 enemies at one time once every 9 seconds
            if (spawnOption == SPAWN_THREE_WIGGLERS && lastTimeSpawningThree > 9) { //2
                spawnThreeWigglers(speedFactor);
            } else if (spawnOption == SPAWN_TWO_WIGGLERS && lastTimeSpawningTwo > 6) { //3
                spawnTwoWigglers(speedFactor);
            } else { //0 or 1
                spawnOneWiggler(speedFactor);
            }
        }
    }
    private void spawnOneWiggler(float speedFactor) {
        enemies.add(new Wiggler(new Vector2(GameScreen.GAME_WIDTH,
                MathUtils.random(GameWorld.GROUND_LEVEL - AssetLoader.wiggler1.getRegionHeight())),
                new Vector2(GameWorld.LATERAL_MOVE_SPEED.x * speedFactor, 0), GameWorld.NO_ACCELERATION));
    }
    private void spawnTwoWigglers(float speedFactor) {
        int spaceBetween = MathUtils.random(Wiggler.HEIGHT, 80);
        int maxPadding = GameWorld.GROUND_LEVEL - 2 * Wiggler.HEIGHT - spaceBetween;
        if (maxPadding < 0) {
            maxPadding = 0;
        }
        int topPadding = MathUtils.random(0, maxPadding);

        for (int i = 0; i < 2; i++) {
            enemies.add(new Wiggler(new Vector2(GameScreen.GAME_WIDTH, topPadding + i * spaceBetween),
                    new Vector2(GameWorld.LATERAL_MOVE_SPEED.x * speedFactor, 0),
                    GameWorld.NO_ACCELERATION));
        }

        //Reset timer
        lastTimeSpawningTwo = 0;
    }
    private void spawnThreeWigglers(float speedFactor) {
        speedFactor *= 0.95f;
        int spaceBetweenWigglers = MathUtils.random(25, 45);
        int topPadding = MathUtils.random(0, 13);

        for (int i = 0; i < 3; i++) {
            enemies.add(new Wiggler(new Vector2(GameScreen.GAME_WIDTH, topPadding + i * spaceBetweenWigglers),
                    new Vector2(GameWorld.LATERAL_MOVE_SPEED.x * speedFactor, 0),
                    GameWorld.NO_ACCELERATION));
        }

        //Reset timer
        lastTimeSpawningThree = 0;
    }

    //HOPPER SPAWNING METHODS
    private void spawnHopper() {
        enemies.add(new Hopper(
                new Vector2(GameScreen.GAME_WIDTH, GameWorld.GROUND_LEVEL - AssetLoader.hopperOnGround.getRegionHeight()),
                GameWorld.LATERAL_MOVE_SPEED.cpy().scl(2), GameWorld.ACCELERATION.cpy()));
    }


    private void spawnQueenWiggler() {
        bosses.add(new QueenWiggler(player));
    }

    private void checkIfGameIsOver() {
        //If the player runs out of lives, the game is over
        if (player.getLives() < 1) {
            world.gameOver();
        }
    }

    //SETTER AND GETTER METHODS
    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public Array<Boss> getBosses() {
        return bosses;
    }

    public void setQueenWigglerState(QueenWigglerState state) {
        queenWigglerState = state;
    }

}
