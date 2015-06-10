package com.zhang_000.archerguygame.gameobjects.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class Arrow extends Weapon {

    private Polygon hitPolygon = new Polygon();

    public Arrow(Vector2 position, Vector2 velocity, Vector2 acceleration, float rotation) {
        super(position, velocity, acceleration);
        super.rotation = rotation;
        super.width = AssetLoader.arrow.getRegionWidth();
        super.height = AssetLoader.arrow.getRegionHeight();

        hitPolygon.setPosition(position.x, position.y);
        hitPolygon.setOrigin(0, 5);
        float[] vertices = new float[] {17, 0, 21, 4, 17, 9};
        hitPolygon.setVertices(vertices);
        hitPolygon.setRotation(rotation);
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
        float deltaRotation;
        if (-90 <= rotation && rotation <= 90) {
            deltaRotation = 6000 * delta / velocity.x;
        } else {
            deltaRotation = -6000 * delta / velocity.x;
        }
        rotation += deltaRotation;

        System.out.println(rotation); //TEST

        //Don't let arrow rotate so much so that the tip faces backwards
        if (rotation > 90 || rotation < -270) {
            rotation = 90;
        } else {
            hitPolygon.rotate(deltaRotation);
        }

        hitPolygon.translate(deltaPos.x, deltaPos.y);
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        batch.draw(AssetLoader.arrow, position.x, position.y, 0, 5, width, height, 1, 1, rotation);
    }

    public Polygon getHitPolygon() {
        return hitPolygon;
    }

}
