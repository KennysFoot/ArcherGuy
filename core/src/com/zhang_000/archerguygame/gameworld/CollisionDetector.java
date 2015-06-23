package com.zhang_000.archerguygame.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.gameobjects.Ground;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameobjects.enemies.Wiggler;
import com.zhang_000.archerguygame.gameobjects.powerups.PowerUp;
import com.zhang_000.archerguygame.gameobjects.powerups.PowerUpManager;
import com.zhang_000.archerguygame.gameobjects.weapons.Arrow;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.helper_classes.InputHandlerGameOver;
import com.zhang_000.archerguygame.screens.GameScreen;

public class CollisionDetector {

    private GameWorld world;
    private Array<Arrow> arrows;
    private Array<Wiggler> wigglers;
    private Player player;
    private Ground ground;
    private PowerUpManager powerUpManager;

    public CollisionDetector(GameWorld world) {
        this.world = world;
        player = world.getPlayer();
        arrows = world.getArrows();
        ground = world.getGround();
        wigglers = world.getWigglers();
        powerUpManager = world.getPowerUpManager();
    }

    public void checkForCollisions() {
        //ARROWS AND WIGGLERS
        for (Arrow a : arrows) {
            for (Wiggler w : wigglers) {
                //If the tip of the arrow is within the screen, check for a collision
                if (a.getX() < GameScreen.GAME_WIDTH - a.getWidth()) {
                    //If the tip of the arrow hits a wiggler, remove both, play sound, and increment score
                    if (Intersector.overlapConvexPolygons(a.getHitPolygon(), w.getHitPolygon())) {
                        arrows.removeValue(a, false);
                        wigglers.removeValue(w, false);
                        AssetLoader.soundArrowHit.play(0.5f);
                        player.incrementScore(1);
                        break;
                    }
                }
            }
        }

        //ARROWS AND GROUND
        for (Arrow a : arrows) {
            if (Intersector.overlapConvexPolygons(a.getHitPolygon(), ground.getBoundingPolygon())) {
                a.setOnGround(true, GameWorld.LATERAL_MOVE_SPEED);
                if (a.getTimeOnGround() > 0.5f) {
                    arrows.removeValue(a, false);
                }
            }
        }

        //WIGGLERS AND PLAYER
        for (Wiggler w : wigglers) {
            if (Intersector.overlapConvexPolygons(w.getHitPolygon(), player.getHitBox())) {
                //Game over if player touches wiggler; stop everything
                gameOver();
            }
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

    private void gameOver() {
        Gdx.input.setInputProcessor(new InputHandlerGameOver());
        player.stop();
        ground.stop();
        world.setGameState(GameWorld.GameState.GAME_OVER);
        world.SCORE_POSITION_Y = 15;

        AssetLoader.soundDeath.play(0.75f);

        if (player.getScore() > AssetLoader.getHighScore()) {
            AssetLoader.setHighScore(player.getScore());
        }
    }

}
