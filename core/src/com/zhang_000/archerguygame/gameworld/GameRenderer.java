package com.zhang_000.archerguygame.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;

public class GameRenderer {

    private GameWorld world;
    private final float GAME_WIDTH;
    private final float GAME_HEIGHT;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    //ASSETS
    private TextureRegion tileDirt;
    private TextureRegion tileDirtRight;
    private TextureRegion tileDirtLeft;
    private TextureRegion tileGrass;

    //GAME OBJECTS
    private Player player;

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
        storeGameObjectReferences();
    }

    private void storeAssetReferences() {
        this.tileDirt = AssetLoader.tileDirt;
        this.tileDirtRight = AssetLoader.tileDirtRight;
        this.tileDirtLeft = AssetLoader.tileDirtLeft;
        this.tileGrass = AssetLoader.tileGrass;
    }

    private void storeGameObjectReferences() {
        this.player = world.getPlayer();
    }

    public void render(float delta, float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin(); //BEGIN SPRITE BATCH

        batch.draw(tileDirtLeft, 0, 50, 22, 14);
        batch.draw(tileDirt, 22, 50, tileDirt.getRegionWidth(), tileDirt.getRegionHeight());
        batch.draw(tileDirtRight, 44, 50, 22, 14);
        batch.draw(tileGrass, 22, 36, 22, 14);


        player.render(runTime, batch);

        batch.end(); //END SPRITE BATCH
    }
}
