package com.zhang_000.archerguygame.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.screens.GameScreen;

public class Ground extends GameObject{

    private final int NUM_TILES = 11; //10 tiles to fill screen + 1 extra needed for scrolling
    private Polygon boundingPolygon = new Polygon();

    public Ground(Vector2 position, Vector2 velocity, Vector2 acceleration) {
        super(position, velocity, acceleration);

        boundingPolygon.setPosition(position.x, position.y);
        boundingPolygon.setOrigin(position.x, position.y);
        float[] vertices = new float[] {0, 0, GameScreen.GAME_WIDTH, 0, GameScreen.GAME_WIDTH,
        GameScreen.GAME_HEIGHT, 0, GameScreen.GAME_HEIGHT};
        boundingPolygon.setVertices(vertices);
    }

    @Override
    public void update(float delta) {
        deltaPos = velocity.cpy().scl(delta);
        position.add(deltaPos);

        if (position.x < -AssetLoader.tileGrass.getRegionWidth()) {
            position.x = 0;
        }
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        for (int i = 0; i < NUM_TILES; i++) {
            batch.draw(AssetLoader.tileGrass, position.x + i * AssetLoader.tileGrass.getRegionWidth(), position.y);
        }
    }

    public void stop() {
        velocity.set(0, 0);
    }

    public Polygon getBoundingPolygon() {
        return boundingPolygon;
    }
}
