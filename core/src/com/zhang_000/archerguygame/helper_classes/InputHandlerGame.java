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

    public InputHandlerGame(GameWorld world, float scaleFactorX, float scaleFactorY) {
        this.world = world;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        int dx = screenX - (int) world.LEFT_EYE_POSITION.x;
        int dy = screenY - (int) world.LEFT_EYE_POSITION.y;
        float degrees = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;

        world.arrows.add(new Arrow(world.LEFT_EYE_POSITION.cpy(), new Vector2(dx * 1.5f, dy * 1.5f), world.ACCELERATION,
                degrees));
        System.out.println(dx + ", " + dy + ", " + degrees + ", " + world.arrows.size);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        return false;
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
