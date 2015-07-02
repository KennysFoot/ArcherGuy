package com.zhang_000.archerguygame.helper_classes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.screens.GameScreen;
import com.zhang_000.archerguygame.screens.MenuScreen;

public class InputHandlerGameOver implements InputProcessor {

    private Game game;

    public InputHandlerGameOver(Game game) {
        this.game = game;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        if (touchingPlayAgain(screenX, screenY)) {
            game.setScreen(new GameScreen(game));
            return true;
        } else if (touchingMainMenu(screenX, screenY)) {
            game.setScreen(new MenuScreen(game));
            return true;
        }

        return false;
    }

    private boolean touchingPlayAgain(int screenX, int screenY) {
        return GameWorld.POS_X_PLAY_AGAIN < screenX && screenX < GameWorld.POS_X_PLAY_AGAIN + GameWorld.WIDTH_PLAY_AGAIN &&
                GameWorld.POS_Y_PLAY_AGAIN < screenY && screenY < GameWorld.POS_Y_PLAY_AGAIN + GameWorld.HEIGHT_BUTTON;
    }

    private boolean touchingMainMenu(int screenX, int screenY) {
        return GameWorld.POS_X_MAIN_MENU < screenX && screenX < GameWorld.POS_X_MAIN_MENU + GameWorld.WIDTH_MAIN_MENU
                && GameWorld.POS_Y_MAIN_MENU < screenY && screenY < GameWorld.POS_Y_MAIN_MENU + GameWorld.HEIGHT_BUTTON;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / GameScreen.scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY / GameScreen.scaleFactorY);
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
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
