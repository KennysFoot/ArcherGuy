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
    private TextureRegion tileDirt;
    private TextureRegion tileDirtRight;
    private TextureRegion tileDirtLeft;
    private TextureRegion tileGrass;

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
        this.tileDirt = AssetLoader.tileDirt;
        this.tileDirtRight = AssetLoader.tileDirtRight;
        this.tileDirtLeft = AssetLoader.tileDirtLeft;
        this.tileGrass = AssetLoader.tileGrass;
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
        batch.draw(archerGuy, (66 - archerGuy.getRegionWidth()) / 2,
                36 - archerGuy.getRegionHeight() + 2,
                archerGuy.getRegionWidth(), archerGuy.getRegionHeight());

        batch.draw(tileDirtLeft, 0, 50, 22, 14);
        batch.draw(tileDirt, 22, 50, tileDirt.getRegionWidth(), tileDirt.getRegionHeight());
        batch.draw(tileDirtRight, 44, 50, 22, 14);
        batch.draw(tileGrass, 22, 36, 22, 14);
        batch.end();
    }
}
