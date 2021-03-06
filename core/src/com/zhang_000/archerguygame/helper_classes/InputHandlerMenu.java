package com.zhang_000.archerguygame.helper_classes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.zhang_000.archerguygame.screens.GameScreen;
import com.zhang_000.archerguygame.screens.SettingsScreen;
import com.zhang_000.archerguygame.screens.SkinSelectionScreen;

public class InputHandlerMenu implements InputProcessor {

    private Game game;

    private float scaleFactorX;
    private float scaleFactorY;
    private Rectangle playButton, settingsButton, skinSelectionButton;

    public InputHandlerMenu(Game game, float scaleFactorX, float scaleFactorY,
                            Rectangle playButton, Rectangle settingsButton, Rectangle skinSelectionButton) {
        this.game = game;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
        this.playButton = playButton;
        this.settingsButton = settingsButton;
        this.skinSelectionButton = skinSelectionButton;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        if (isTouchingPlay(screenX, screenY)) {
            game.setScreen(new GameScreen(game));
            return true;
        } else if (isTouchingSettings(screenX, screenY)) { //TO DO
            game.setScreen(new SettingsScreen(game));
            return true;
        } else if (isTouchingSkinSelection(screenX, screenY)) {
            game.setScreen(new SkinSelectionScreen(game));
            return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    private boolean isTouchingPlay(int screenX, int screenY) {
        return playButton.x < screenX && screenX < (playButton.x + playButton.width) &&
                playButton.y > screenY && screenY > (playButton.y + playButton.height);
    }

    private boolean isTouchingSettings(int screenX, int screenY) {
        return settingsButton.x < screenX && screenX < (settingsButton.x + settingsButton.width) &&
                settingsButton.y > screenY && screenY > (settingsButton.y + settingsButton.height);
    }

    private boolean isTouchingSkinSelection(int screenX, int screenY) {
        return skinSelectionButton.x < screenX && screenX < (skinSelectionButton.x + skinSelectionButton.width) &&
                skinSelectionButton.y > screenY && screenY > (skinSelectionButton.y + skinSelectionButton.height);
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }

    //UNUSED
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
