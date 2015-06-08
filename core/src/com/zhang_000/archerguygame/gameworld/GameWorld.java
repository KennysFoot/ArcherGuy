package com.zhang_000.archerguygame.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.gameobjects.Ground;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameobjects.weapons.Arrow;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.screens.GameScreen;

public class GameWorld {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private final int GROUND_LEVEL;
    private int moveSpeed = -7;

    public final Vector2 ACCELERATION = new Vector2(0, 100);
    public final Vector2 LEFT_EYE_POSITION;

    //GAME OBJECTS
    private Player player;
    private Ground ground;
    public Array<Arrow> arrows;

    //ASSETS
    private TextureRegion tileDirt;
    private TextureRegion tileDirtRight;
    private TextureRegion tileDirtLeft;
    private TextureRegion tileGrass;

    public GameWorld() {
        camera = new OrthographicCamera();
        camera.setToOrtho(true, GameScreen.GAME_WIDTH, GameScreen.GAME_HEIGHT);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        storeAssetReferences();

        GROUND_LEVEL = (int) GameScreen.GAME_HEIGHT - tileGrass.getRegionHeight() + 1;

        //GAME OBJECTS
        player = new Player(new Vector2(10, GROUND_LEVEL - AssetLoader.archerGuyFront1.getRegionHeight()),
                new Vector2(0, 0), ACCELERATION);
        player.setGroundLevel(GROUND_LEVEL);
        ground = new Ground(new Vector2(0, GROUND_LEVEL), new Vector2(moveSpeed, 0), new Vector2(0, 0));
        arrows = new Array<Arrow>();

        LEFT_EYE_POSITION = new Vector2(18, GROUND_LEVEL - player.getHeight() + 7);
    }

    private void storeAssetReferences() {
        this.tileDirt = AssetLoader.tileDirt;
        this.tileDirtRight = AssetLoader.tileDirtRight;
        this.tileDirtLeft = AssetLoader.tileDirtLeft;
        this.tileGrass = AssetLoader.tileGrass;
    }

    public void update(float delta) {
        ground.update(delta);
        player.update(delta);
        updateWeapons(delta);
    }

    private void updateWeapons(float delta) {
        for (Arrow a : arrows) {
            a.update(delta);

            //Remove is off screen
            if (a.getY() > GROUND_LEVEL || a.getX() > GameScreen.GAME_WIDTH || a.getX() < -20) {
                arrows.removeValue(a, false);
            }
        }

    }

    public void render(float delta, float runTime) {
        Gdx.gl.glClearColor(105 / 255f, 210 / 255f, 231 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin(); //BEGIN SPRITE BATCH

        ground.render(runTime, batch);
        player.render(runTime, batch);

        for (int i = 0; i < arrows.size; i++) {
            arrows.get(i).render(runTime, batch);
        }

        batch.end(); //END SPRITE BATCH
    }

}
