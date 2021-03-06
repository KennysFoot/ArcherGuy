package com.zhang_000.archerguygame.gameworld;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.gameobjects.Ground;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameobjects.enemies.Enemy;
import com.zhang_000.archerguygame.gameobjects.enemies.EnemyManager;
import com.zhang_000.archerguygame.gameobjects.enemies.bosses.Boss;
import com.zhang_000.archerguygame.gameobjects.powerups.PowerUpManager;
import com.zhang_000.archerguygame.gameobjects.weapons.Arrow;
import com.zhang_000.archerguygame.gameobjects.weapons.WeaponManager;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.helper_classes.InputHandlerGame;
import com.zhang_000.archerguygame.screens.GameScreen;
import com.zhang_000.archerguygame.screens.MenuScreen;
import com.zhang_000.archerguygame.screens.SettingsScreen;

public class GameWorld {

    private Game game;

    //CAMERA AND RENDERING
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    //UTIL
    private GlyphLayout layout;
    private CollisionDetector collisionDetector;
    private SkinUnlockDetector skinUnlockDetector;

    //VALUES
    private GameState gameState;
    private String score;
    public int SCORE_POSITION_X;
    public final int SCORE_POSITION_Y = 2;
    public static int GROUND_LEVEL;
    public static final Vector2 LATERAL_MOVE_SPEED = new Vector2(-13, 0);
    public static final Vector2 NO_ACCELERATION = new Vector2(0, 0);
    public static final Vector2 ACCELERATION = new Vector2(0, 150);

    //GAME OVER BUTTONS
    private static final String PLAY_AGAIN = "Play Again";
    private static final String MAIN_MENU = "Main Menu";
    public static float HEIGHT_BUTTON;

    public static float POS_X_PLAY_AGAIN;
    public static final int POS_Y_PLAY_AGAIN = 70;
    public static float WIDTH_PLAY_AGAIN;

    public static float POS_X_MAIN_MENU;
    public static final int POS_Y_MAIN_MENU = 95;
    public static float WIDTH_MAIN_MENU;

    //GAME OBJECTS
    private Player player;
    private Ground ground;
    private PowerUpManager powerUpManager;
    private EnemyManager enemyManager;
    private WeaponManager weaponManager;

    private Preferences prefs;
    private boolean redLineOn;

    //PLAY AND PAUSE
    public static final int POS_X_PAUSE = 2;
    public static final int POS_Y_PAUSE = 2;
    private boolean paused = false;

    //UNLOCK MESSAGE
    private boolean displayMessage = true;
    private String message;
    private float unlockX;
    private final float unlockY = 50;
    private float timer = 0;

    public enum GameState {
        RUNNING, GAME_OVER
    }

    public GameWorld(Game game) {
        this.game = game;
        layout = new GlyphLayout();

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

        layout.setText(AssetLoader.font, PLAY_AGAIN);
        POS_X_PLAY_AGAIN = (GameScreen.GAME_WIDTH - layout.width) / 2;
        WIDTH_PLAY_AGAIN = layout.width;

        layout.setText(AssetLoader.font, MAIN_MENU);
        POS_X_MAIN_MENU = (GameScreen.GAME_WIDTH - layout.width) / 2;
        WIDTH_MAIN_MENU = layout.width;
        HEIGHT_BUTTON = -layout.height;

        //GAME OBJECTS
        player = new Player(new Vector2(10, GROUND_LEVEL - AssetLoader.archerGuyFront1.getRegionHeight()),
                new Vector2(0, 0), ACCELERATION.cpy(), shapeRenderer);
        player.setGroundLevel(GROUND_LEVEL);
        ground = new Ground(new Vector2(0, GROUND_LEVEL), LATERAL_MOVE_SPEED.cpy(), new Vector2(0, 0), player,
                shapeRenderer);
        powerUpManager = new PowerUpManager(this);
        weaponManager = new WeaponManager(this);

        //UTIL
        enemyManager = new EnemyManager(this);
        //collision detector must be instantiated last as it needs a reference to everything else
        collisionDetector = new CollisionDetector(this);

        getSettings();

        skinUnlockDetector = new SkinUnlockDetector(prefs, this);
    }

    private void getSettings() {
        prefs = Gdx.app.getPreferences(AssetLoader.ARCHER_GUY);
        redLineOn = prefs.getBoolean(SettingsScreen.RED_LINE, true);
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

        skinUnlockDetector.update();
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

        //Render any bosses
        for (Boss boss : enemyManager.getBosses()) {
            boss.render(runTime, batch);
        }

        //Render enemies
        for (Enemy e : enemyManager.getEnemies()) {
            e.render(runTime, batch);
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
            renderButtonsOnGameOver();
        }

        renderPausePlayButton();

        if (displayMessage && message != null) {
            AssetLoader.font.draw(batch, message, unlockX, unlockY);
            timer += delta;

            if (timer > 2.5f) {
                timer = 0;
                displayMessage = false;
                message = null;
            }
        }

        batch.end(); //END SPRITE BATCH

        //*********************************************************************************************

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); //BEGIN SHAPE RENDERER
        shapeRenderer.setColor(Color.RED);

        if (redLineOn) {
            shapeRenderer.line(50, 0, 50, GameScreen.GAME_HEIGHT);
        }

        /*
        shapeRenderer.polygon(player.getHitBox().getTransformedVertices());
        for (int i = 0; i < weaponManager.getExplosions().size; i++) {
            Circle c = weaponManager.getExplosions().get(i).getBoundingCircle();
            shapeRenderer.circle(c.x, c.y, c.radius);
        }
        for (Enemy e : getEnemies()) {
            shapeRenderer.polygon(e.getHitPolygon().getTransformedVertices());
        }
        */

        shapeRenderer.end(); //END SHAPE RENDERER
    }

    private void renderScore() {
        AssetLoader.shadow.draw(batch, score, SCORE_POSITION_X, SCORE_POSITION_Y);
        AssetLoader.font.draw(batch, score, SCORE_POSITION_X, SCORE_POSITION_Y + 1);
    }
    private void renderScoreBoard() {
        int POSITION_Y = 15;
        layout.setText(AssetLoader.greenFont, "Score: " + score);
        float POSITION_X = (GameScreen.GAME_WIDTH - layout.width) / 2;

        AssetLoader.shadow.draw(batch, "Score: " + score, POSITION_X, POSITION_Y - 1);
        AssetLoader.greenFont.draw(batch, "Score: " + score, POSITION_X, POSITION_Y);

        final String HIGH_SCORE = Integer.toString(AssetLoader.getHighScore());
        layout.setText(AssetLoader.greenFont, "HighScore:  " + HIGH_SCORE);
        POSITION_X = (GameScreen.GAME_WIDTH - layout.width) / 2;

        AssetLoader.shadow.draw(batch, "High Score: " + HIGH_SCORE, POSITION_X, POSITION_Y + 19);
        AssetLoader.greenFont.draw(batch, "High Score: " + HIGH_SCORE, POSITION_X, POSITION_Y + 20);
    }
    private void renderPausePlayButton() {
        if (paused) {
            //Render play button
            batch.draw(AssetLoader.play, POS_X_PAUSE - 1, POS_Y_PAUSE);

            //Render options on pause screen
            layout.setText(AssetLoader.font, MAIN_MENU);
            AssetLoader.font.draw(batch, MAIN_MENU, POS_X_MAIN_MENU, (GameScreen.GAME_HEIGHT + layout.height) / 2);


        } else if (gameState != GameState.GAME_OVER){
            //Render pause button if the game is NOT currently paused and the game is not over
            batch.draw(AssetLoader.pause, POS_X_PAUSE, POS_Y_PAUSE);
        }
    }

    private void renderButtonsOnGameOver() {
        AssetLoader.shadow.draw(batch, PLAY_AGAIN, POS_X_PLAY_AGAIN, POS_Y_PLAY_AGAIN - 1);
        AssetLoader.font.draw(batch, PLAY_AGAIN, POS_X_PLAY_AGAIN, POS_Y_PLAY_AGAIN);

        AssetLoader.shadow.draw(batch, MAIN_MENU, POS_X_MAIN_MENU, POS_Y_MAIN_MENU);
        AssetLoader.font.draw(batch, MAIN_MENU, POS_X_MAIN_MENU, POS_Y_MAIN_MENU);
    }

    public void pause() {
        paused = true;
        player.pause();
        enemyManager.pause();
        weaponManager.pause();
        powerUpManager.pause();
        ground.stop();
        AssetLoader.pauseSounds();
    }

    public void resume() {
        paused = false;
        player.resume();
        enemyManager.resume();
        weaponManager.resume();
        powerUpManager.resume();
        ground.resume();
        AssetLoader.resumeSounds();
    }

    public void gameOver() {
        collisionDetector.gameOver();
    }

    public void createArrow(float degrees) {
        weaponManager.addArrow(degrees);
    }

    public void backToMainMenu() {
        game.setScreen(new MenuScreen(game));
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

    public Array<Enemy> getEnemies() {
        return enemyManager.getEnemies();
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

    public boolean isPaused() {
        return paused;
    }

    public void setDisplayMessage(String message) {
        displayMessage = true;
        this.message = message;

        layout.setText(AssetLoader.font, message);
        unlockX = (GameScreen.GAME_WIDTH - layout.width) / 2;
    }
}
