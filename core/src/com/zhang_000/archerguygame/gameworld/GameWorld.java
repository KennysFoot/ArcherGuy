package com.zhang_000.archerguygame.gameworld;

import com.badlogic.gdx.math.Rectangle;

public class GameWorld {

    private final float GAME_WIDTH;
    private final float GAME_HEIGHT;

    private Rectangle rect;

    public GameWorld(float gameWidth, float gameHeight) {
        GAME_WIDTH = gameWidth;
        GAME_HEIGHT = gameHeight;

        rect = new Rectangle(0, 0, 50, 50);
    }

    public void update(float delta) {
        if (rect.x > GAME_WIDTH) {
            rect.x = 0;
        }
        rect.x++;
    }

    public Rectangle getRect() {
        return rect;
    }

}
