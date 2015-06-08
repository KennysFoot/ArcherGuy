package com.zhang_000.archerguygame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.helper_classes.InputHandlerMenu;

public class MenuScreen implements Screen {

    private Game myGame;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private final float GAME_WIDTH;
    private final float GAME_HEIGHT;

    //BUTTONS
    private Rectangle playGameRect;
    private final String PLAY_GAME = "Play Game";
    private final float PLAY_GAME_X;
    private final float PLAY_GAME_Y;

    private Rectangle settingsRect;
    private final float SETTINGS_X;
    private final float SETTINGS_Y;

    //ARCHER GUY ANIMATION
    private Animation AGAnimation;
    private float runTime = 0;

    public MenuScreen(Game game) {
        this.myGame = game;

        //Calculate game height
        float SCREEN_WIDTH = Gdx.graphics.getWidth();
        float SCREEN_HEIGHT = Gdx.graphics.getHeight();
        GAME_WIDTH = 210; //GAME_WIDTH constant at 210 pixels; scale height accordingly
        float scaleFactorX = SCREEN_WIDTH / GAME_WIDTH;
        GAME_HEIGHT = SCREEN_HEIGHT / scaleFactorX;
        float scaleFactorY = SCREEN_HEIGHT / GAME_HEIGHT;

        camera = new OrthographicCamera();
        camera.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        //BUTTONS
        GlyphLayout layout = new GlyphLayout();

        layout.setText(AssetLoader.font, PLAY_GAME);
        PLAY_GAME_X = (GAME_WIDTH - layout.width) / 2;
        PLAY_GAME_Y = GAME_HEIGHT / 8;
        playGameRect = new Rectangle(PLAY_GAME_X, PLAY_GAME_Y - layout.height, layout.width, layout.height);

        layout.setText(AssetLoader.font, "Settings");
        SETTINGS_X = (GAME_WIDTH - layout.width) / 2;
        SETTINGS_Y = PLAY_GAME_Y + 25;
        settingsRect = new Rectangle(SETTINGS_X, SETTINGS_Y - layout.height, layout.width, layout.height);

        //SET INPUT PROCESSOR
        Gdx.input.setInputProcessor(new InputHandlerMenu(myGame, scaleFactorX, scaleFactorY, playGameRect, settingsRect));

        AGAnimation = AssetLoader.AGFrontAnimation;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        drawMenu();
        runTime += delta;
        batch.draw(AGAnimation.getKeyFrame(runTime), 162, 8, 29, 30);

        batch.end();
    }

    private void drawMenu() {
        //BACKGROUND
        batch.draw(AssetLoader.backgroundMainMenu, 0, 0, GAME_WIDTH, GAME_HEIGHT);

        //PLAY GAME
        AssetLoader.shadow.draw(batch, PLAY_GAME, PLAY_GAME_X, PLAY_GAME_Y);
        AssetLoader.font.draw(batch, PLAY_GAME, PLAY_GAME_X, PLAY_GAME_Y + 1);

        //SETTINGS
        AssetLoader.shadow.draw(batch, "Settings", SETTINGS_X, SETTINGS_Y);
        AssetLoader.font.draw(batch, "Settings", SETTINGS_X, SETTINGS_Y + 1);
    }

    @Override
    public void show() {

    }
    @Override
    public void resize(int width, int height) {

    }
    @Override
    public void pause() {

    }
    @Override
    public void resume() {

    }
    @Override
    public void hide() {

    }
    @Override
    public void dispose() {

    }

}
