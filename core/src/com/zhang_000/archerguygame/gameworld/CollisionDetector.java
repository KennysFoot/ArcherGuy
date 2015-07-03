package com.zhang_000.archerguygame.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.gameobjects.Ground;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameobjects.enemies.Enemy;
import com.zhang_000.archerguygame.gameobjects.enemies.EnemyManager;
import com.zhang_000.archerguygame.gameobjects.enemies.Wiggler;
import com.zhang_000.archerguygame.gameobjects.enemies.bosses.Boss;
import com.zhang_000.archerguygame.gameobjects.enemies.bosses.QueenWiggler;
import com.zhang_000.archerguygame.gameobjects.powerups.PowerUp;
import com.zhang_000.archerguygame.gameobjects.powerups.PowerUpManager;
import com.zhang_000.archerguygame.gameobjects.weapons.Arrow;
import com.zhang_000.archerguygame.gameobjects.weapons.ExplodingArrow;
import com.zhang_000.archerguygame.gameobjects.weapons.Explosion;
import com.zhang_000.archerguygame.gameobjects.weapons.WeaponManager;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.helper_classes.InputHandlerGameOver;
import com.zhang_000.archerguygame.screens.GameScreen;

public class CollisionDetector {

    private GameWorld world;
    private Array<Arrow> arrows;
    private Array<Explosion> explosions;
    private Array<Enemy> enemies;
    private Array<Boss> bosses;
    private Player player;
    private Ground ground;
    private PowerUpManager powerUpManager;
    private EnemyManager enemyManager;
    private WeaponManager weaponManager;

    public CollisionDetector(GameWorld world) {
        this.world = world;
        player = world.getPlayer();
        arrows = world.getArrows();
        ground = world.getGround();
        enemies = world.getEnemies();
        powerUpManager = world.getPowerUpManager();
        enemyManager = world.getEnemyManager();
        bosses = enemyManager.getBosses();
        weaponManager = world.getWeaponManager();
        explosions = weaponManager.getExplosions();
    }

    public void checkForCollisions() {
        //Arrow collisions with enemies
        checkArrowCollisionsExplodingArrowsEnabled();

        for (Enemy e : enemies) {
            for (Explosion p : explosions) {
                if (overlaps(e.getHitPolygon(), p.getBoundingCircle())) {
                    player.incrementKillScore(e.getScore());
                    enemies.removeValue(e, false);
                }
            }
        }

        //Player/Shield collisions with enemies
        if (player.isShieldActivated()) {
            checkCollisionsShieldAndEnemies();
        } else {
            checkCollisionsPlayerAndEnemies();
        }

        //PLAYER AND POWER UPS
        for (PowerUp pow : powerUpManager.getPowerUps()) {
            //Check if the power up is on the screen
            if (pow.getState() == PowerUp.PowerUpState.ON_SCREEN) {
                //Check if power up is touching the player
                if (Intersector.overlapConvexPolygons(pow.getHitPolygon(), player.getHitBox())) {
                    pow.setState(PowerUp.PowerUpState.ACTIVE);
                }
            }
        }
    }

    private void checkArrowCollisionsExplodingArrowsEnabled() {
        for (Arrow a : arrows) {
            //ARROWS AND WIGGLERS
            for (Enemy e : enemies) {
                //If the tip of the arrow is within the screen, check for a collision
                if (a.getX() < GameScreen.GAME_WIDTH - a.getWidth() && Intersector.overlapConvexPolygons(a.getHitPolygon(), e.getHitPolygon())) {
                    //If the tip of the arrow hits a wiggler, remove both, play sound, and increment score
                    if (a instanceof ExplodingArrow) {
                        explodingArrowHitRegularEnemy(a, e);
                    } else {
                        //REGULAR ARROW
                        arrows.removeValue(a, false);
                        enemies.removeValue(e, false);
                        AssetLoader.soundArrowHit.play(0.5f);
                        player.incrementKillScore(e.getScore());
                    }
                }
            }

            //BOSSES
            for (Boss boss : bosses) {
                if (Intersector.overlapConvexPolygons(a.getHitPolygon(), boss.getHitPolygon())) {
                    if (a instanceof ExplodingArrow) {
                        explodingArrowHitBoss(a, boss);
                    } else {
                        //Remove the arrow
                        arrows.removeValue(a, false);
                        //Do damage to the boss
                        boss.doDamage(1);
                    }

                    //If the boss is dead, remove it and increase the player's score
                    if (boss.getHP() <= 0) {
                        enemyManager.setQueenWigglerState(EnemyManager.QueenWigglerState.NOT_SPAWNED);
                        bosses.removeValue(boss, false);
                        player.incrementKillScore(QueenWiggler.SCORE);
                    }
                }
            }

            //GROUND
            if (Intersector.overlapConvexPolygons(a.getHitPolygon(), ground.getBoundingPolygon())) {
                if (a instanceof ExplodingArrow) {
                    arrows.removeValue(a, false);
                    AssetLoader.soundExplodingArrowHit.play();
                    weaponManager.addExplosion(a.getHitPolygon().getX(), a.getHitPolygon().getY() - 18);
                } else {
                    a.setOnGround(true, GameWorld.LATERAL_MOVE_SPEED);
                    if (a.getTimeOnGround() > 0.5f) {
                        arrows.removeValue(a, false);
                    }
                }
            }
        }
    }

    private void explodingArrowHitRegularEnemy(Arrow a, Enemy e) {
        //Create the explosion
        weaponManager.addExplosion(e.getX() - 48, e.getY() - 48);

        //PLAY EXPLOSION SOUND
        AssetLoader.soundExplodingArrowHit.play();

        player.incrementKillScore(e.getScore());

        arrows.removeValue(a, false);
        enemies.removeValue(e, false);
    }
    private void explodingArrowHitBoss(Arrow a, Boss b) {
        arrows.removeValue(a, false);
        b.doDamage(2);
        AssetLoader.soundExplodingArrowHit.play();
        weaponManager.addExplosion(a.getHitPolygon().getX() - 40, a.getHitPolygon().getY() - 40);
    }

    private void checkCollisionsShieldAndEnemies() {
        //WIGGLERS AND SHIELD
        for (Enemy e : enemies) {
            if (overlaps(e.getHitPolygon(), player.getShield())) {
                //Enemies dies if it touches the shield
                enemies.removeValue(e, false);
                player.incrementKillScore(Wiggler.SCORE);
                AssetLoader.soundShieldHit.play(0.8f);
            }
        }

        //SHIELD AND BOSS WEAPON
        for (Boss b : bosses) {
            if (b.getWeapon() != null) {
                //Check if the weapon hits the shield
                if (overlaps(b.getWeapon().getHitPolygon(), player.getShield())) {
                    //Remove the weapon if it hits the shield
                    b.removeWeapon();
                }
            }
        }
    }
    private void checkCollisionsPlayerAndEnemies() {
        //WIGGLERS AND PLAYER
        for (Enemy e : enemies) {
            if (Intersector.overlapConvexPolygons(e.getHitPolygon(), player.getHitBox())) {
                //Game over if player touches wiggler; stop everything and set the player's lives to zero
                player.incrementLives(-player.getLives());
                gameOver();
            }
        }

        //PLAYER AND BOSS WEAPON
        for (Boss b : bosses) {
            //PLAYER AND BOSS WEAPON
            //Make sure the boss has a weapon being used
            if (b.getWeapon() != null) {
                //Check if the weapon hits the player
                if (Intersector.overlapConvexPolygons(player.getHitBox(), b.getWeapon().getHitPolygon())) {
                    player.incrementLives(-1);
                    b.removeWeapon();

                    if (isGameOver()) {
                        gameOver();
                    }
                }
            }
        }
    }

    private boolean isGameOver() {
        return player.getLives() <= 0;
    }

    public void gameOver() {
        //Switch over to the game over input handler
        Gdx.input.setInputProcessor(new InputHandlerGameOver(world.getGame()));

        player.stop();
        ground.stop();
        world.setGameState(GameWorld.GameState.GAME_OVER);

        AssetLoader.soundDeath.play(0.70f);

        //If a new high score has been reached, save it
        if (player.getScore() > AssetLoader.getHighScore()) {
            AssetLoader.setHighScore(player.getScore());
        }
    }

    public static boolean overlaps(Polygon polygon, Circle circle) {
        float[] vertices = polygon.getTransformedVertices();
        Vector2 center = new Vector2(circle.x, circle.y);
        float squareRadius = circle.radius * circle.radius;
        for (int i = 0; i < vertices.length; i += 2) {
            if (i == 0) {
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length - 2], vertices[vertices.length - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
                    return true;
            } else {
                if (Intersector.intersectSegmentCircle(new Vector2(vertices[i - 2], vertices[i - 1]), new Vector2(vertices[i], vertices[i + 1]), center, squareRadius))
                    return true;
            }
        }
        return circle.contains(polygon.getX(), polygon.getY()) || polygon.contains(circle.x, circle.y);
    }
}
