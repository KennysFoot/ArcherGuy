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
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class SettingsScreen implements Screen {

    public static final String RED_LINE = "red line";
    public static final String MUSIC = "music";
    public static String SFX = "sfx";

    private Preferences prefs;

    private Game game;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Stage stage;
    private CheckBox.CheckBoxStyle checkBoxStyle;
    private CheckBox checkBoxRedLine, checkBoxMusic, checkBoxSFX;
    private TextButton buttonMainMenu;

    public SettingsScreen(Game game) {
        this.game = game;
        final Game myGame = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(true, MenuScreen.GAME_WIDTH, MenuScreen.GAME_HEIGHT);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        prefs = Gdx.app.getPreferences(AssetLoader.ARCHER_GUY);
        createPrefsFirstLaunch();

        stage = new Stage(new ScalingViewport(Scaling.fit, MenuScreen.GAME_WIDTH, MenuScreen.GAME_HEIGHT, camera),
                batch);

        //Create buttons
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(AssetLoader.yes, AssetLoader.yes, AssetLoader.yes,
                AssetLoader.font);
        buttonMainMenu = new TextButton("main menu", style);
        buttonMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                myGame.setScreen(new MenuScreen(myGame));
            }
        });
        buttonMainMenu.setBounds((MenuScreen.GAME_WIDTH - buttonMainMenu.getWidth()) / 2,
                100, 100, 30);

        setUpCheckBoxes();

        //Add the buttons and check boxes to the stage
        stage.addActor(checkBoxRedLine);
        stage.addActor(checkBoxMusic);
        stage.addActor(checkBoxSFX);
        stage.addActor(buttonMainMenu);

        //Make the stage handle input
        Gdx.input.setInputProcessor(stage);
    }

    private void createPrefsFirstLaunch() {
        if (!prefs.contains(RED_LINE)) {
            prefs.putBoolean(RED_LINE, true);
        }
        if (!prefs.contains(MUSIC)) {
            prefs.putBoolean(MUSIC, true);
        }
        if (!prefs.contains(SFX)) {
            prefs.putBoolean(SFX, true);
        }
        prefs.flush();
    }

    private void setUpCheckBoxes() {
        final int PADDING = 10;
        final int BUTTON_HEIGHT = 30;

        //Create new check boxes
        checkBoxStyle = new CheckBox.CheckBoxStyle(AssetLoader.no, AssetLoader.yes, AssetLoader.font, Color.WHITE);

        checkBoxRedLine = new CheckBox("red boundary line", checkBoxStyle);
        checkBoxMusic = new CheckBox("music", checkBoxStyle);
        checkBoxSFX = new CheckBox("sfx", checkBoxStyle);

        //Retrieve preferences and apply them to the check boxes
        checkBoxRedLine.setChecked(prefs.getBoolean(RED_LINE));
        checkBoxMusic.setChecked(prefs.getBoolean(MUSIC));
        checkBoxSFX.setChecked(prefs.getBoolean(SFX));

        //Add the listeners to the check boxes
        checkBoxRedLine.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prefs.putBoolean(RED_LINE, checkBoxRedLine.isChecked());
                prefs.flush();
                System.out.println(checkBoxRedLine.isChecked());
            }
        });
        checkBoxMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prefs.putBoolean(MUSIC, checkBoxRedLine.isChecked());
                prefs.flush();
            }
        });
        checkBoxSFX.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prefs.putBoolean(SFX, checkBoxSFX.isChecked());
                prefs.flush();
            }
        });

        //Set the bounds on the checkboxes
        checkBoxRedLine.setBounds(PADDING, PADDING, checkBoxRedLine.getWidth(), BUTTON_HEIGHT);
        checkBoxMusic.setBounds(PADDING, PADDING + BUTTON_HEIGHT + 1, checkBoxMusic.getWidth(), BUTTON_HEIGHT);
        checkBoxSFX.setBounds(PADDING, PADDING + 2 * BUTTON_HEIGHT + 2, checkBoxSFX.getWidth(), BUTTON_HEIGHT);
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
