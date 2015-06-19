package com.zhang_000.archerguygame.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.gameobjects.Ground;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameobjects.Wiggler;
import com.zhang_000.archerguygame.gameobjects.weapons.Arrow;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.helper_classes.InputHandlerGame;
import com.zhang_000.archerguygame.helper_classes.InputHandlerGameOver;
import com.zhang_000.archerguygame.screens.GameScreen;

public class GameWorld {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private GlyphLayout layout;
    private GameState gameState;
    private String score;
    private int SCORE_POSITION_X;
    private int SCORE_POSITION_Y = 5;

    private final int GROUND_LEVEL;
    public final Vector2 LATERAL_MOVE_SPEED = new Vector2(-10, 0);
    public final Vector2 NO_ACCELERATION = new Vector2(0, 0);
    public final Vector2 ACCELERATION = new Vector2(0, 150);

    //GAME OBJECTS
    public Player player;
    private Ground ground;
    public Array<Arrow> arrows = new Array<Arrow>();
    private Array<Wiggler> wigglers = new Array<Wiggler>();
    private float wigglerTimer = 0;

    private enum GameState {
        RUNNING, GAME_OVER
    }

    public GameWorld() {
        camera = new OrthographicCamera();
        camera.setToOrtho(true, GameScreen.GAME_WIDTH, GameScreen.GAME_HEIGHT);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        layout = new GlyphLayout();

        GROUND_LEVEL = (int) GameScreen.GAME_HEIGHT - AssetLoader.tileGrass.getRegionHeight() + 1;

        gameState = GameState.RUNNING;

        //GAME OBJECTS
        player = new Player(new Vector2(10, GROUND_LEVEL - AssetLoader.archerGuyFront1.getRegionHeight()),
                new Vector2(0, 0), ACCELERATION.cpy());
        player.setGroundLevel(GROUND_LEVEL);
        ground = new Ground(new Vector2(0, GROUND_LEVEL), LATERAL_MOVE_SPEED, new Vector2(0, 0));
    }

    public void update(float delta) {
        switch (gameState) {
            case RUNNING:
                updateRunning(delta);
                break;
            case GAME_OVER:
                updateGameOver(delta);
                break;
        }
    }

    private void updateRunning(float delta) {
        ground.update(delta);
        player.update(delta);
        updateEnemies(delta);
        updateWeapons(delta);

        checkForCollisions();

        //Change the position at which the score will be rendered depending on how long the string is
        score = Integer.toString(player.getScore());
        layout.setText(AssetLoader.font, score);
        SCORE_POSITION_X = (int) ((GameScreen.GAME_WIDTH - layout.width) / 2);

        //Increment lastFire variable in the input handler
        InputHandlerGame.lastFire += delta;
    }

    private void updateGameOver(float delta) {
        player.update(delta);
    }

    private void updateEnemies(float delta) {
        wigglerTimer += delta;
        //Spawn a new wiggler every 3 seconds
        if (wigglerTimer > 3) {
            float speedFactor = 1;
            if (player.getScore() > 1) {
                speedFactor += player.getScore() / 6;
                if (speedFactor > 5) {
                    speedFactor = 5;
                }
            }
            wigglers.add(new Wiggler(new Vector2(GameScreen.GAME_WIDTH,
                    MathUtils.random(GROUND_LEVEL - AssetLoader.wiggler1.getRegionHeight())),
                    new Vector2(LATERAL_MOVE_SPEED.x * speedFactor, 0), NO_ACCELERATION));
            //Reset the timer to 0
            wigglerTimer = 0;
        }
        for (Wiggler w : wigglers) {
            w.update(delta);
        }
    }

    private void updateWeapons(float delta) {
        for (Arrow a : arrows) {
            a.update(delta);
            //Remove the arrow if it's off screen
            if (a.getX() > GameScreen.GAME_WIDTH) {
                arrows.removeValue(a, false);
            }
        }
    }

    private void checkForCollisions() {
        //ARROWS AND WIGGLERS
        for (Arrow a : arrows) {
            for (Wiggler w : wigglers) {
                //If the tip of the arrow is within the screen, check for a collision
                if (a.getX() < GameScreen.GAME_WIDTH - a.getWidth()) {
                    //If the tip of the arrow hits a wiggler, remove both, play sound, and increment score
                    if (Intersector.overlapConvexPolygons(a.getHitPolygon(), w.getHitPolygon())) {
                        arrows.removeValue(a, false);
                        wigglers.removeValue(w, false);
                        AssetLoader.arrowHit.play(0.5f);
                        player.incrementScore(1);
                        break;
                    }
                }
            }
        }

        //ARROWS AND GROUND
        for (Arrow a : arrows) {
            if (Intersector.overlapConvexPolygons(a.getHitPolygon(), ground.getBoundingPolygon())) {
                a.setOnGround(true, LATERAL_MOVE_SPEED);
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
    }

    private void gameOver() {
        Gdx.input.setInputProcessor(new InputHandlerGameOver());
        player.stop();
        ground.stop();
        gameState = GameState.GAME_OVER;
        SCORE_POSITION_Y = 15;

        if (player.getScore() > AssetLoader.getHighScore()) {
            AssetLoader.setHighScore(player.getScore());
        }
    }

    public void render(float delta, float runTime) {
        //Set the background color (light blue for the sky)
        Gdx.gl.glClearColor(105 / 255f, 210 / 255f, 231 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin(); //BEGIN SPRITE BATCH

        //Render the ground and the player
        ground.render(runTime, batch);
        player.render(runTime, batch);

        //Render the arrows
        for (int i = 0; i < arrows.size; i++) {
            arrows.get(i).render(runTime, batch);
        }

        //Render the wigglers
        for (Wiggler w : wigglers) {
            w.render(runTime, batch);
        }

        //Render either the current score or the game over score board depending on the game state
        if (gameState == GameState.RUNNING) {
            renderScore();
        } else if (gameState == GameState.GAME_OVER) {
            renderScoreBoard();
        }

        batch.end(); //END SPRITE BATCH

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); //BEGIN SHAPE RENDERER
        shapeRenderer.setColor(Color.RED);

        //Render the arrow boundary line
        shapeRenderer.line(50, 0, 50, GameScreen.GAME_HEIGHT);

        shapeRenderer.end(); //END SHAPE RENDERER
    }

    private void renderScore() {
        AssetLoader.shadow.draw(batch, score, SCORE_POSITION_X, SCORE_POSITION_Y);
        AssetLoader.font.draw(batch, score, SCORE_POSITION_X, SCORE_POSITION_Y + 1);
    }

    private void renderScoreBoard() {
        layout.setText(AssetLoader.greenFont, "Score");
        AssetLoader.shadow.draw(batch, "Score", (GameScreen.GAME_WIDTH - layout.width) / 2, SCORE_POSITION_Y - 1);
        AssetLoader.greenFont.draw(batch, "Score", (GameScreen.GAME_WIDTH - layout.width) / 2, SCORE_POSITION_Y);

        layout.setText(AssetLoader.greenFont, score);
        AssetLoader.shadow.draw(batch, score, (GameScreen.GAME_WIDTH - layout.width) / 2, SCORE_POSITION_Y + 19);
        AssetLoader.greenFont.draw(batch, score, (GameScreen.GAME_WIDTH - layout.width) / 2, SCORE_POSITION_Y + 20);

        layout.setText(AssetLoader.greenFont, "HighScore");
        AssetLoader.shadow.draw(batch, "High Score", (GameScreen.GAME_WIDTH - layout.width) / 2, SCORE_POSITION_Y + 49);
        AssetLoader.greenFont.draw(batch, "High Score", (GameScreen.GAME_WIDTH - layout.width) / 2, SCORE_POSITION_Y + 50);

        layout.setText(AssetLoader.greenFont, Integer.toString(AssetLoader.getHighScore()));
        AssetLoader.shadow.draw(batch, Integer.toString(AssetLoader.getHighScore()),
                (GameScreen.GAME_WIDTH - layout.width) / 2, SCORE_POSITION_Y + 69);
        AssetLoader.greenFont.draw(batch, Integer.toString(AssetLoader.getHighScore()),
                (GameScreen.GAME_WIDTH - layout.width) / 2, SCORE_POSITION_Y + 70);

    }

}
