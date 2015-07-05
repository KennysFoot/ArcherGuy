package com.zhang_000.archerguygame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.helper_classes.InputHandlerMenu;

public class MenuScreen implements Screen {

    private Game myGame;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private final Texture backgroundMainMenuTex;
    private final TextureRegion backgroundMainMenu;

    public static float GAME_WIDTH;
    public static float GAME_HEIGHT;
    public static float SCALE_X;
    public static float SCALE_Y;

    //BUTTONS
    private Rectangle playGameRect;
    private final String PLAY_GAME = "Play Game";
    private final float PLAY_GAME_X;
    private final float PLAY_GAME_Y;

    private Rectangle settingsRect;
    private final float SETTINGS_X;
    private final float SETTINGS_Y;

    //ARCHER GUY ANIMATION
    private float runTime = 0;

    public MenuScreen(Game game) {
        this.myGame = game;

        //Calculate game height
        float SCREEN_WIDTH = Gdx.graphics.getWidth();
        float SCREEN_HEIGHT = Gdx.graphics.getHeight();
        GAME_WIDTH = 210; //GAME_WIDTH constant at 210 pixels; scale height accordingly
        SCALE_X = SCREEN_WIDTH / GAME_WIDTH;
        GAME_HEIGHT = SCREEN_HEIGHT / SCALE_X;
        SCALE_Y = SCREEN_HEIGHT / GAME_HEIGHT;

        camera = new OrthographicCamera();
        camera.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        //BUTTONS
        GlyphLayout layout = new GlyphLayout();

        //Play Game Button
        layout.setText(AssetLoader.font, PLAY_GAME);
        PLAY_GAME_X = (GAME_WIDTH - layout.width) / 2;
        PLAY_GAME_Y = GAME_HEIGHT / 8;
        playGameRect = new Rectangle(PLAY_GAME_X, PLAY_GAME_Y - layout.height, layout.width, layout.height);

        //Settings button
        layout.setText(AssetLoader.font, "Settings");
        SETTINGS_X = (GAME_WIDTH - layout.width) / 2;
        SETTINGS_Y = PLAY_GAME_Y + 25;
        settingsRect = new Rectangle(SETTINGS_X, SETTINGS_Y - layout.height, layout.width, layout.height);

        //SET INPUT PROCESSOR
        Gdx.input.setInputProcessor(new InputHandlerMenu(myGame, SCALE_X, SCALE_Y, playGameRect, settingsRect));

        //MAIN MENU BACKGROUND
        backgroundMainMenuTex = new Texture(Gdx.files.internal("archer_guy_background_mainmenu.png"));
        backgroundMainMenuTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        backgroundMainMenu = new TextureRegion(backgroundMainMenuTex);
        backgroundMainMenu.flip(false, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        drawMenu();
        runTime += delta;
        batch.draw(AssetLoader.AGFrontAnimation.getKeyFrame(runTime), 162, 7, 29, 30);

        batch.end();
    }

    private void drawMenu() {
        //BACKGROUND
        batch.draw(backgroundMainMenu, 0, 0, GAME_WIDTH, GAME_HEIGHT);

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
        backgroundMainMenuTex.dispose();
    }

}
