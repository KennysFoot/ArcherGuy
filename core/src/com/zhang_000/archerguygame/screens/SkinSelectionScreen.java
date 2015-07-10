package com.zhang_000.archerguygame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class SkinSelectionScreen implements Screen {

    public static final String SKIN = "skin";
    public static final String SKIN_INDEX = "skin index";

    public static final String REGULAR = "REGULAR";          //0
    public static final String PINK_SKIN = "PINK SKIN";      //1
    public static final String SANTA = "SANTA";              //2
    public static final String ASIAN = "ASIAN";              //3
    public static final String BROWN = "BROWN";              //4
    public static final String METALLIC = "METALLIC";        //5

    public static final String[] SKIN_CHOICES = {REGULAR, PINK_SKIN, SANTA, ASIAN, BROWN, METALLIC};
    private int i;

    private Preferences prefs;

    private Game game;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Stage stage;
    private TextButton buttonMainMenu;
    private ImageButton buttonPrev, buttonNext;
    private Image selectedSkin;
    private Label label;

    public SkinSelectionScreen(Game game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(true, MenuScreen.GAME_WIDTH, MenuScreen.GAME_HEIGHT);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        prefs = Gdx.app.getPreferences(AssetLoader.ARCHER_GUY);

        stage = new Stage(new ScalingViewport(Scaling.fit, MenuScreen.GAME_WIDTH, MenuScreen.GAME_HEIGHT, camera),
                batch);

        setUpMainMenuButton(game);
        getIndexFromPrefs();
        setUpSkinViewer();
        setUpLabel();
        setUpNextAndPrevButtons();

        Gdx.input.setInputProcessor(stage);
    }

    private void setUpMainMenuButton(Game game) {
        final Game myGame = game;

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(AssetLoader.mainMenuBacking,
                AssetLoader.mainMenuBacking, AssetLoader.mainMenuBacking, AssetLoader.font);
        buttonMainMenu = new TextButton("MAIN MENU", style);
        buttonMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                myGame.setScreen(new MenuScreen(myGame));
            }
        });
        buttonMainMenu.setBounds((MenuScreen.GAME_WIDTH - buttonMainMenu.getWidth()) / 2,
                100, 100, 30);

        stage.addActor(buttonMainMenu);
    }

    private void getIndexFromPrefs() {
        i = prefs.getInteger(SKIN_INDEX, 0);
    }

    private void setUpSkinViewer() {
        //todo change selected skin initialization to be based on index from prefs
        selectedSkin = new Image(AssetLoader.regSkin);
        selectedSkin.setBounds(buttonMainMenu.getX() + (buttonMainMenu.getWidth() - selectedSkin.getWidth()) / 2,
                25, selectedSkin.getWidth(), selectedSkin.getHeight());

        stage.addActor(selectedSkin);
    }

    private void setUpLabel() {
        Label.LabelStyle style = new Label.LabelStyle(AssetLoader.font, Color.WHITE);
        label = new Label(SKIN_CHOICES[i], style);
        label.setBounds(buttonMainMenu.getX() + (buttonMainMenu.getWidth() - label.getWidth()) / 2,
                25, label.getWidth(), label.getHeight());

        stage.addActor(label);
    }

    private void setUpNextAndPrevButtons() {
        buttonPrev = new ImageButton(AssetLoader.prevArrow);
        buttonNext = new ImageButton(AssetLoader.nextArrow);

        //todo add ClickListener's

        //Set position on screen
        buttonPrev.setBounds(buttonMainMenu.getX() - buttonPrev.getWidth(),
                (MenuScreen.GAME_HEIGHT - buttonPrev.getHeight()) / 2.5f, buttonPrev.getWidth(),
                buttonPrev.getHeight());
        buttonNext.setBounds(buttonMainMenu.getX() + buttonMainMenu.getWidth(),
                (MenuScreen.GAME_HEIGHT - buttonNext.getHeight()) / 2.5f,
                buttonNext.getWidth(), buttonNext.getHeight());

        //Add to stage
        stage.addActor(buttonPrev);
        stage.addActor(buttonNext);
    }




    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Render background colour
        Gdx.gl.glClearColor(105 / 255f, 210 / 255f, 231 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }

}
