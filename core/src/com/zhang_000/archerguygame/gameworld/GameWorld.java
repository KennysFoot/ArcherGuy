package com.zhang_000.archerguygame.gameworld;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.gameobjects.Ground;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameobjects.enemies.EnemyManager;
import com.zhang_000.archerguygame.gameobjects.enemies.Wiggler;
import com.zhang_000.archerguygame.gameobjects.enemies.bosses.Boss;
import com.zhang_000.archerguygame.gameobjects.powerups.PowerUpManager;
import com.zhang_000.archerguygame.gameobjects.weapons.Arrow;
import com.zhang_000.archerguygame.gameobjects.weapons.WeaponManager;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.helper_classes.InputHandlerGame;
import com.zhang_000.archerguygame.screens.GameScreen;

public class GameWorld {

    private Game game;

    //CAMERA AND RENDERING
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    //UTIL
    private GlyphLayout layout;
    private CollisionDetector collisionDetector;

    //VALUES
    private GameState gameState;
    private String score;
    public int SCORE_POSITION_X;
    public final int SCORE_POSITION_Y = 5;
    public static int GROUND_LEVEL;
    public static final Vector2 LATERAL_MOVE_SPEED = new Vector2(-10, 0);
    public static final Vector2 NO_ACCELERATION = new Vector2(0, 0);
    public final Vector2 ACCELERATION = new Vector2(0, 150);

    //GAME OBJECTS
    private Player player;
    private Ground ground;
    private PowerUpManager powerUpManager;
    private EnemyManager enemyManager;
    private WeaponManager weaponManager;

    public enum GameState {
        RUNNING, GAME_OVER
    }

    public GameWorld(Game game) {
        this.game = game;

        //CAMERA AND RENDERING STUFF
        camera = new OrthographicCamera();
        camera.setToOrtho(true, GameScreen.GAME_WIDTH, GameScreen.GAME_HEIGHT);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        //VALUES
        GROUND_LEVEL = (int) GameScreen.GAME_HEIGHT - AssetLoader.tileGrass.getRegionHeight() + 1;
        gameState = GameState.RUNNING;

        //GAME OBJECTS
        player = new Player(new Vector2(10, GROUND_LEVEL - AssetLoader.archerGuyFront1.getRegionHeight()),
                new Vector2(0, 0), ACCELERATION.cpy(), shapeRenderer);
        player.setGroundLevel(GROUND_LEVEL);
        ground = new Ground(new Vector2(0, GROUND_LEVEL), LATERAL_MOVE_SPEED.cpy(), new Vector2(0, 0));
        powerUpManager = new PowerUpManager(this);
        weaponManager = new WeaponManager(this);

        //UTIL
        layout = new GlyphLayout();
        enemyManager = new EnemyManager(this);
        //collision detector must be instantiated last as it needs a reference to everything else
        collisionDetector = new CollisionDetector(this);
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
        //Update all game objects
        ground.update(delta);
        player.update(delta);
        enemyManager.updateEnemies(delta);
        weaponManager.updateWeapons(delta);
        powerUpManager.update(delta);

        collisionDetector.checkForCollisions();

        //Change the position at which the score will be rendered depending on how long the string is
        score = Integer.toString(player.getScore());
        layout.setText(AssetLoader.font, score);
        SCORE_POSITION_X = (int) ((GameScreen.GAME_WIDTH - layout.width) / 2);

        //Increment lastFire variable in the input handler
        InputHandlerGame.lastFire += delta;
    }

    private void updateGameOver(float delta) {
        //If the game is over, freeze everything except the player
        //Let the player fall to the ground
        player.update(delta);
    }

    public void render(float delta, float runTime) {
        //Set the background color (light blue for the sky)
        Gdx.gl.glClearColor(105 / 255f, 210 / 255f, 231 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin(); //BEGIN SPRITE BATCH

        //Render the ground and the player
        ground.render(runTime, batch);
        player.render(runTime, batch);

        //Render the wigglers
        for (Wiggler w : enemyManager.getWigglers()) {
            w.render(runTime, batch);
        }

        //Render any bosses
        for (Boss boss : enemyManager.getBosses()) {
            boss.render(runTime, batch);
        }

        //Render any power ups currently on screen
        powerUpManager.render(runTime, batch);

        //Render the arrows
        weaponManager.render(runTime, batch);

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

        //TEST
        for (int i = 0; i < weaponManager.getExplosions().size; i++) {
            Circle c = weaponManager.getExplosions().get(i).getBoundingCircle();
            shapeRenderer.circle(c.x, c.y, c.radius);
        }

        shapeRenderer.end(); //END SHAPE RENDERER

    }

    private void renderScore() {
        AssetLoader.shadow.draw(batch, score, SCORE_POSITION_X, SCORE_POSITION_Y);
        AssetLoader.font.draw(batch, score, SCORE_POSITION_X, SCORE_POSITION_Y + 1);
    }

    private void renderScoreBoard() {
        int POSITION_Y = 15;

        layout.setText(AssetLoader.greenFont, "Score");
        AssetLoader.shadow.draw(batch, "Score", (GameScreen.GAME_WIDTH - layout.width) / 2, POSITION_Y - 1);
        AssetLoader.greenFont.draw(batch, "Score", (GameScreen.GAME_WIDTH - layout.width) / 2, POSITION_Y);

        layout.setText(AssetLoader.greenFont, score);
        AssetLoader.shadow.draw(batch, score, (GameScreen.GAME_WIDTH - layout.width) / 2, POSITION_Y + 19);
        AssetLoader.greenFont.draw(batch, score, (GameScreen.GAME_WIDTH - layout.width) / 2, POSITION_Y + 20);

        layout.setText(AssetLoader.greenFont, "HighScore");
        AssetLoader.shadow.draw(batch, "High Score", (GameScreen.GAME_WIDTH - layout.width) / 2, POSITION_Y + 49);
        AssetLoader.greenFont.draw(batch, "High Score", (GameScreen.GAME_WIDTH - layout.width) / 2, POSITION_Y + 50);

        layout.setText(AssetLoader.greenFont, Integer.toString(AssetLoader.getHighScore()));
        AssetLoader.shadow.draw(batch, Integer.toString(AssetLoader.getHighScore()),
                (GameScreen.GAME_WIDTH - layout.width) / 2, POSITION_Y + 69);
        AssetLoader.greenFont.draw(batch, Integer.toString(AssetLoader.getHighScore()),
                (GameScreen.GAME_WIDTH - layout.width) / 2, POSITION_Y + 70);

    }

    public void gameOver() {
        collisionDetector.gameOver();
    }

    public void createArrow(float degrees) {
        weaponManager.addArrow(degrees);
    }

    //SETTER AND GETTER METHODS
    public void setGameState(GameState state) {
        gameState = state;
    }

    public Array<Arrow> getArrows() {
        return weaponManager.getArrows();
    }

    public Ground getGround() {
        return ground;
    }

    public Array<Wiggler> getWigglers() {
        return enemyManager.getWigglers();
    }

    public PowerUpManager getPowerUpManager() {
        return powerUpManager;
    }

    public Player getPlayer() {
        return player;
    }

    public WeaponManager getWeaponManager() {
        return weaponManager;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public Game getGame() {
        return game;
    }
}
