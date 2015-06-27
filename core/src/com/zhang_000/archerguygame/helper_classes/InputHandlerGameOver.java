package com.zhang_000.archerguygame.helper_classes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.zhang_000.archerguygame.screens.MenuScreen;

public class InputHandlerGameOver implements InputProcessor{

    private Game game;

    public InputHandlerGameOver(Game game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        game.setScreen(new MenuScreen(game));
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
