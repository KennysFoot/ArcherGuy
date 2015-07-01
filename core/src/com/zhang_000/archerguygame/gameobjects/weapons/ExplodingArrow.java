package com.zhang_000.archerguygame.gameobjects.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class ExplodingArrow extends Arrow {

    public ExplodingArrow(Vector2 position, Vector2 velocity, Vector2 acceleration, float rotation) {
        super(position, velocity, acceleration, rotation);

        width = AssetLoader.explodingArrow.getRegionWidth();
        height = AssetLoader.explodingArrow.getRegionHeight();
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        batch.draw(AssetLoader.explodingArrow, position.x, position.y, 0, 3, width, height, 1, 1, rotation);
    }

}
