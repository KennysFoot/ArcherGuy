package com.zhang_000.archerguygame.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class GameRenderer {

    private GameWorld world;
    private final float GAME_WIDTH;
    private final float GAME_HEIGHT;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    //ASSETS
    private TextureRegion archerGuy;

    public GameRenderer(GameWorld world, float gameWidth, float gameHeight) {
        this.world = world;
        GAME_WIDTH = gameWidth;
        GAME_HEIGHT = gameHeight;

        camera = new OrthographicCamera();
        camera.setToOrtho(true, gameWidth, gameHeight);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        storeAssetReferences();
    }

    private void storeAssetReferences() {
        this.archerGuy = AssetLoader.archerGuy;
    }

    public void render(float delta, float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Rectangle rect = world.getRect();
        shapeRenderer.setColor(0, 0, 1, 1);
        shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        shapeRenderer.end();

        batch.begin();
        batch.draw(archerGuy, (GAME_WIDTH - archerGuy.getRegionWidth()) / 2,
                (GAME_HEIGHT - archerGuy.getRegionHeight()) / 2,
                archerGuy.getRegionWidth(), archerGuy.getRegionHeight());
        batch.end();
    }
}
