package com.zhang_000.archerguygame.helper_classes;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameobjects.weapons.Arrow;
import com.zhang_000.archerguygame.gameworld.GameWorld;

public class InputHandlerGame implements InputProcessor {

    private float scaleFactorX;
    private float scaleFactorY;

    private GameWorld world;

    public static float lastFire = 0.33f;

    //POWER UPS
    private static boolean infiniteArrowsActivated = false;

    public InputHandlerGame(GameWorld world, float scaleFactorX, float scaleFactorY) {
        this.world = world;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        int dx = screenX - (int) world.player.getLeftEyePosition().x;
        int dy = screenY - (int) world.player.getLeftEyePosition().y;
        float degrees = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;

        //Add a new arrow if screen is touched to the right of the boundary line and
        //if the last fire has been more than 1/3 of a second ago or if the infinite arrows
        //power up is activated
        if (touchingShootArrowRegion(screenX) && (lastFire > 0.33f || infiniteArrowsActivated)) {

            //New arrow is created from the players left eye
            //Initial velocity is in the direction of touch on the screen
            //Magnitude of velocity is always 300
            world.arrows.add(new Arrow(world.player.getLeftEyePosition().cpy(),
                    new Vector2(Arrow.ARROW_VELOCITY_MAGNITUDE * MathUtils.cosDeg(degrees),
                            Arrow.ARROW_VELOCITY_MAGNITUDE * MathUtils.sinDeg(degrees)),
                    world.ACCELERATION.cpy(), degrees));

            //Play the arrow firing sound effect
            AssetLoader.fireArrow.play();

            //Reset the last fire timer
            lastFire = 0;

        } else if (touchingMovementRegion(screenX)) {
            //Make Arrow guy go up
            world.player.goUp();
        }

        return false;
    }

    private boolean touchingShootArrowRegion(int screenX) {
        return screenX > 50;
    }

    private boolean touchingMovementRegion(int screenX) {
        return screenX < 50;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        if (touchingShootArrowRegion(screenX)) {

        } else {
            world.player.goDown();
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
