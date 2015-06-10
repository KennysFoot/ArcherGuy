package com.zhang_000.archerguygame.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class Wiggler extends GameObject {

    private Rectangle hitBox;

    public Wiggler(Vector2 position, Vector2 velocity, Vector2 acceleration) {
        super(position, velocity, acceleration);
        super.width = AssetLoader.wiggler1.getRegionWidth();
        super.height = AssetLoader.wiggler1.getRegionHeight();
        hitBox = new Rectangle(position.x, position.y + height, width, height);
    }

    @Override
    public void update(float delta) {
        deltaPos = velocity.cpy().scl(delta);
        position.add(deltaPos);

    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        batch.draw(AssetLoader.wigglerAni.getKeyFrame(runTime), position.x, position.y, width, height);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

}
