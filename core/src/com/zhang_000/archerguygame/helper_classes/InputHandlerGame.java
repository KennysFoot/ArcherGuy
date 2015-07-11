package com.zhang_000.archerguygame.helper_classes;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameobjects.weapons.Arrow;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.screens.GameScreen;

public class InputHandlerGame implements InputProcessor {

    private float scaleFactorX;
    private float scaleFactorY;

    private GameWorld world;
    private Player player;

    public static float lastFire = Arrow.RELOAD_TIME;

    //POWER UPS
    private static boolean infiniteArrowsActivated;

    public InputHandlerGame(GameWorld world, float scaleFactorX, float scaleFactorY) {
        this.world = world;
        this.player = world.getPlayer();
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
        infiniteArrowsActivated = false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        if (touchingPauseRegion(screenX, screenY)) {
            if (world.isPaused()) {
                world.resume();
            } else {
                world.pause();
            }
            //NOT PAUSED
        } else if (!world.isPaused()) {
            if (touchingShootArrowRegion(screenX) && (lastFire > Arrow.RELOAD_TIME || infiniteArrowsActivated)) {
                //Calculate the distance between the finger touch position and Archer Guy
                int dx = screenX - (int) player.getLeftEyePosition().x;
                int dy = screenY - (int) player.getLeftEyePosition().y;
                //Calculate angle on inclination from Archer Guy to finger touch position
                float degrees = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;

                world.createArrow(degrees);
                //Reset the last fire timer
                lastFire = 0;
                return true;

            } else if (touchingMovementRegion(screenX)) {
                //Make Arrow guy go up
                player.goUp();
                return true;
            }
        }
        //PAUSED
        else {
            //todo paused touch input handling
            if (touchingMainMenuRegion(screenX, screenY)) {
                world.backToMainMenu();
            }
        }

        return false;
    }

    private boolean touchingShootArrowRegion(int screenX) {
        return screenX > 50;
    }
    private boolean touchingMovementRegion(int screenX) {
        return screenX < 50;
    }
    private boolean touchingPauseRegion(int screenX, int screenY) {
        return GameWorld.POS_X_PAUSE < screenX && screenY < GameWorld.POS_Y_PAUSE + AssetLoader.pause.getRegionHeight();
    }
   private boolean touchingMainMenuRegion(int screenX, int screenY) {
        return GameWorld.POS_X_MAIN_MENU < screenX && screenX < GameWorld.POS_X_MAIN_MENU + GameWorld.WIDTH_MAIN_MENU &&
                (GameScreen.GAME_HEIGHT - GameWorld.HEIGHT_BUTTON) / 2 < screenY &&
                screenY < (GameScreen.GAME_HEIGHT - GameWorld.HEIGHT_BUTTON) / 2 + GameWorld.HEIGHT_BUTTON;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);

        if (touchingShootArrowRegion(screenX)) {

        } else {
            player.goDown();
            return true;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //HANDLE DRAGGING FINGER FROM PLAYER ZONE TO ARROW ZONE HERE
        return false;
    }

    public static void setInfiniteArrowsActivated(boolean bool) {
        infiniteArrowsActivated = bool;
    }

    public static boolean isInfiniteArrowsActivated() {
        return infiniteArrowsActivated;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }

    //UNUSED METHODS
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
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
