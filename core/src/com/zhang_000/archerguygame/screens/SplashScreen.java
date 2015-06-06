package com.zhang_000.archerguygame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

public class SplashScreen implements Screen {

    private SpriteBatch batch;
    private Game myGame;
    private Texture logoTex;
    private TextureRegion logo;
    private OrthographicCamera camera;
    private long startTime;

    public SplashScreen(Game game) {
        this.myGame = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(true, 512, 210);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void show() {
        startTime = TimeUtils.millis();
        logoTex = new Texture(Gdx.files.internal("logo.png"));
        logo = new TextureRegion(logoTex);
        logo.flip(false, true);
    }

    @Override
    public void dispose() {
        logoTex.dispose();
        batch.dispose();
    }

    @Override

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(logo, 0, 0);
        batch.end();

        if (TimeUtils.millis() > (startTime + 1500)) {
            myGame.setScreen(new GameScreen());
        }
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

}
