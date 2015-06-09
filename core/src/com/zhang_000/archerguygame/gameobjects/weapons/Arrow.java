package com.zhang_000.archerguygame.gameobjects.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class Arrow extends Weapon {

    public Arrow(Vector2 position, Vector2 velocity, Vector2 acceleration, float rotation) {
        super(position, velocity, acceleration);
        super.rotation = rotation;
        super.width = AssetLoader.arrow.getRegionWidth();
        super.height = AssetLoader.arrow.getRegionHeight();
    }

    @Override
    public void update(float delta) {
        //Update velocity and position
        deltaVel = acceleration.cpy().scl(delta);
        velocity.add(deltaVel);

        //Cap velocity
        if (velocity.y > 175) {
            velocity.y = 175;
        }

        deltaPos = velocity.cpy().scl(delta);
        position.add(deltaPos);

        //Update roation
        rotation += 6000 * delta / velocity.x;
        //Don't let arrow rotate so much so that the tip faces backwards
        if (rotation > 90) {
            rotation = 90;
        }
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        batch.draw(AssetLoader.arrow, position.x, position.y, 0, 4, width, height, 1, 1, rotation);
    }

}
