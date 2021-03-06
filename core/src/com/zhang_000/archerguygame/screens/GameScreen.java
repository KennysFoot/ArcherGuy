package com.zhang_000.archerguygame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.helper_classes.InputHandlerGame;

public class GameScreen implements Screen {

    private Game game;
    private GameWorld world;
    private float runTime;

    public static final float GAME_WIDTH = 210; //GAME_WIDTH constant at 210 pixels; scale height accordingly
    public static float GAME_HEIGHT;
    public static float scaleFactorX;
    public static float scaleFactorY;

    public GameScreen(Game game) {
        this.game = game;

        float SCREEN_WIDTH = Gdx.graphics.getWidth();
        float SCREEN_HEIGHT = Gdx.graphics.getHeight();
        scaleFactorX = SCREEN_WIDTH / GAME_WIDTH;
        GAME_HEIGHT = SCREEN_HEIGHT / scaleFactorX;
        scaleFactorY = SCREEN_HEIGHT / GAME_HEIGHT;

        world = new GameWorld(game);
        Gdx.input.setInputProcessor(new InputHandlerGame(world, scaleFactorX, scaleFactorY));
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        world.update(delta);
        world.render(delta, runTime);
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
