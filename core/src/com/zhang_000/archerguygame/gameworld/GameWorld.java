package com.zhang_000.archerguygame.gameworld;

import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameobjects.Player;

public class GameWorld {

    private final float GAME_WIDTH;
    private final float GAME_HEIGHT;

    private final Vector2 ACCELERATION = new Vector2(0, 230);

    //GAME OBJECTS
    private Player player;

    public GameWorld(float gameWidth, float gameHeight) {
        GAME_WIDTH = gameWidth;
        GAME_HEIGHT = gameHeight;
        player = new Player(new Vector2(20, 0), new Vector2(0, 0), ACCELERATION);
    }

    public void update(float delta) {
        player.update(delta);
    }

    public Player getPlayer() {
        return player;
    }

}
