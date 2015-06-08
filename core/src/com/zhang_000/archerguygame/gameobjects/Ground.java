package com.zhang_000.archerguygame.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class Ground extends GameObject{

    private TextureRegion tileGrass = AssetLoader.tileGrass;
    private final int NUM_TILES = 11; //10 tiles to fill screen + 1 extra needed for scrolling

    public Ground(Vector2 position, Vector2 velocity, Vector2 acceleration) {
        super(position, velocity, acceleration);
    }

    @Override
    public void update(float delta) {
        deltaPos = velocity.cpy().scl(delta);
        position.add(deltaPos);

        if (position.x < -tileGrass.getRegionWidth()) {
            position.x = 0;
        }
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        for (int i = 0; i < NUM_TILES; i++) {
            batch.draw(tileGrass, position.x + i * tileGrass.getRegionWidth(), position.y);
        }
    }
}
