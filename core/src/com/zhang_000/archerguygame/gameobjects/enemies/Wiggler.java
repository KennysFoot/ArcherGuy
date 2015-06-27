package com.zhang_000.archerguygame.gameobjects.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameobjects.GameObject;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class Wiggler extends GameObject {

    public static final int SCORE = 2;
    public static final int WIDTH = 20;
    public static final int HEIGHT = 18;

    private Polygon hitPolygon = new Polygon();

    public Wiggler(Vector2 position, Vector2 velocity, Vector2 acceleration) {
        super(position, velocity, acceleration);
        super.width = AssetLoader.wiggler1.getRegionWidth();
        super.height = AssetLoader.wiggler1.getRegionHeight();

        hitPolygon.setPosition(position.x, position.y);
        hitPolygon.setOrigin(0, 0);
        float[] vertices = new float[] {0, 0, 19, 0, 19, 17, 0, 17};
        hitPolygon.setVertices(vertices);
    }

    @Override
    public void update(float delta) {
        deltaPos = velocity.cpy().scl(delta);
        position.add(deltaPos);

        hitPolygon.translate(deltaPos.x, deltaPos.y);
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        batch.draw(AssetLoader.animationWiggler.getKeyFrame(runTime), position.x, position.y, width, height);
    }

    public Polygon getHitPolygon() {
        return hitPolygon;
    }

}
