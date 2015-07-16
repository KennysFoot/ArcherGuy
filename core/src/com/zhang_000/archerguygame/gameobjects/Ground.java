package com.zhang_000.archerguygame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.screens.GameScreen;

public class Ground extends GameObject{

    private ShapeRenderer shapeRenderer;

    private final int NUM_TILES = 11; //10 tiles to fill screen + 1 extra needed for scrolling
    private Polygon boundingPolygon = new Polygon();
    private Player player;
    private boolean paused = false;

    public Ground(Vector2 position, Vector2 velocity, Vector2 acceleration, Player player,
                  ShapeRenderer shapeRenderer) {
        super(position, velocity, acceleration);
        this.player = player;
        this.shapeRenderer = shapeRenderer;
        width = AssetLoader.tileGrass.getRegionWidth();
        height = AssetLoader.tileGrass.getRegionHeight();

        boundingPolygon.setPosition(position.x, position.y);
        boundingPolygon.setOrigin(position.x, position.y);
        float[] vertices = new float[] {0, 0, GameScreen.GAME_WIDTH, 0, GameScreen.GAME_WIDTH,
        GameScreen.GAME_HEIGHT, 0, GameScreen.GAME_HEIGHT};
        boundingPolygon.setVertices(vertices);
    }

    @Override
    public void update(float delta) {
        if (!paused) {
            deltaPos = velocity.cpy().scl(delta);
            position.add(deltaPos);

            if (position.x < -AssetLoader.tileGrass.getRegionWidth()) {
                position.x = 0;
            }

            /*
            float speedFactor = 1;
            if (player.getScore() > 1) {
                speedFactor += player.getScore() / 10;

                if (speedFactor > 4) {
                    speedFactor = 4;
                }
            }

            velocity.set(GameWorld.LATERAL_MOVE_SPEED.cpy().scl(speedFactor));
            */
        }
    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(64 / 255f, 118 / 255f, 26 / 255f, 1);
        //Draw green background behind grass
        shapeRenderer.rect(0, position.y, width * 11, 3);
        //Draw brown background behind dirt
        shapeRenderer.setColor(140 / 255f, 89 / 255f, 20 / 255f, 1);
        shapeRenderer.rect(0, position.y + 3, width * 11, height - 3);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        //Draw the dirt tiles
        for (int i = 0; i < NUM_TILES; i++) {
            batch.draw(AssetLoader.tileGrass, position.x + i * width, position.y);
        }
    }

    public void stop() {
        velocity.set(0, 0);
        paused = true;
    }

    public void resume() {
        velocity.set(GameWorld.LATERAL_MOVE_SPEED.cpy());
        paused = false;
    }

    public Polygon getBoundingPolygon() {
        return boundingPolygon;
    }
}
