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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class SkinSelectionScreen implements Screen {

    public static final String SKIN_INDEX = "skin index";

    public static final String REGULAR = "REGULAR";       //0
    public static final String PINK_SKIN = "PINK";        //1
    public static final String SANTA = "SANTA";           //2
    public static final String ASIAN = "ASIAN";           //3
    public static final String BROWN = "BROWN";           //4
    public static final String METALLIC = "METALLIC";     //5

    public static final int LAST_INDEX = 5;

    public static final String[] SKIN_CHOICES = {REGULAR, PINK_SKIN, SANTA, ASIAN, BROWN, METALLIC};
    private Array<String> unlockedSkins = new Array<String>();
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
    Label.LabelStyle style = new Label.LabelStyle(AssetLoader.font, Color.WHITE);

    public SkinSelectionScreen(Game game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(true, MenuScreen.GAME_WIDTH, MenuScreen.GAME_HEIGHT);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        prefs = Gdx.app.getPreferences(AssetLoader.ARCHER_GUY);

        stage = new Stage(new ScalingViewport(Scaling.fit, MenuScreen.GAME_WIDTH, MenuScreen.GAME_HEIGHT, camera),
                batch);

        getUnlockedSkins();
        setUpMainMenuButton(game);
        getIndexFromPrefs();
        setUpSkinViewer();
        setUpLabel();
        setUpNextAndPrevButtons();

        Gdx.input.setInputProcessor(stage);
    }

    private void getUnlockedSkins() {
        unlockedSkins.add(REGULAR);
        //if (prefs.getBoolean(PINK_SKIN, false)) {
        unlockedSkins.add(PINK_SKIN);
        // }
        // if (prefs.getBoolean(SANTA, false)) {
        unlockedSkins.add(SANTA);
        // }
        //  if (prefs.getBoolean(ASIAN, false)) {
        unlockedSkins.add(ASIAN);
        //  }
        //  if (prefs.getBoolean(BROWN, false)) {
        unlockedSkins.add(BROWN);
        //  }
        //  if (prefs.getBoolean(METALLIC, false)) {
        unlockedSkins.add(METALLIC);
        //  }
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
        selectedSkin = new Image(getImage());
        selectedSkin.setBounds(buttonMainMenu.getX() + (buttonMainMenu.getWidth() - selectedSkin.getWidth()) / 2,
                25, selectedSkin.getWidth(), selectedSkin.getHeight());

        stage.addActor(selectedSkin);
    }

    private void setUpLabel() {
        label = new Label(unlockedSkins.get(i), style);
        label.setBounds(buttonMainMenu.getX() + (buttonMainMenu.getWidth() - label.getWidth()) / 2,
                25, label.getWidth(), label.getHeight());

        stage.addActor(label);
    }

    private TextureRegionDrawable getImage() {
        switch (i) {
            case 0: //REGULAR
                return AssetLoader.regSkin;

            case 1: //Pink
                return AssetLoader.pinkSkin;

            case 2: //Santa
                return AssetLoader.santaSkin;

            case 3: //Asian
                return AssetLoader.asianSkin;

            case 4: //Brown
                return AssetLoader.brownSkin;

            case 5: //Metallic
                return AssetLoader.metallicSkin;
        }
        return new TextureRegionDrawable();
    }

    private void setUpNextAndPrevButtons() {
        buttonPrev = new ImageButton(AssetLoader.prevArrow);
        buttonNext = new ImageButton(AssetLoader.nextArrow);

        //Add Click Listeners
        buttonPrev.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (i > 0) {
                    --i;
                } else {
                    i = unlockedSkins.size - 1;
                }

                prefs.putInteger(SKIN_INDEX, i);
                prefs.flush();

                updateSkinAndLabel();
            }
        });
        buttonNext.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (i < unlockedSkins.size - 1) {
                    ++i;
                } else {
                    i = 0;
                }

                prefs.putInteger(SKIN_INDEX, i);
                prefs.flush();

                updateSkinAndLabel();
            }
        });

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

    private void updateSkinAndLabel() {
        //Remove previous label
        label.remove();

        //Create new label
        label = new Label(unlockedSkins.get(i), style);
        label.setBounds(buttonMainMenu.getX() + (buttonMainMenu.getWidth() - label.getWidth()) / 2,
                25, label.getWidth(), label.getHeight());
        stage.addActor(label);

        //Update image
        selectedSkin.setDrawable(getImage());
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
