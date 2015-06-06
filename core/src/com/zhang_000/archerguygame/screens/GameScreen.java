package com.zhang_000.archerguygame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.zhang_000.archerguygame.gameworld.GameRenderer;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.helper_classes.InputHandlerGame;

public class GameScreen implements Screen {

    private GameWorld world;
    private GameRenderer renderer;
    private float runTime;

    public GameScreen() {
        float SCREEN_WIDTH = Gdx.graphics.getWidth();
        float SCREEN_HEIGHT = Gdx.graphics.getHeight();
        float GAME_WIDTH = 210; //GAME_WIDTH constant at 210 pixels; scale height accordingly
        float scaleFactorX = SCREEN_WIDTH / GAME_WIDTH;
        float GAME_HEIGHT = SCREEN_HEIGHT / scaleFactorX;
        float scaleFactorY = SCREEN_HEIGHT / GAME_HEIGHT;

        world = new GameWorld(GAME_WIDTH, GAME_HEIGHT);
        Gdx.input.setInputProcessor(new InputHandlerGame(world, scaleFactorX, scaleFactorY));
        renderer = new GameRenderer(world, GAME_WIDTH, GAME_HEIGHT);
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        world.update(delta);
        renderer.render(delta, runTime);
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
