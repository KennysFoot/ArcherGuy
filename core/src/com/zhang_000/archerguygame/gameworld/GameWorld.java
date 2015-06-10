package com.zhang_000.archerguygame.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.gameobjects.Ground;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameobjects.Wiggler;
import com.zhang_000.archerguygame.gameobjects.weapons.Arrow;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.screens.GameScreen;

public class GameWorld {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private final int GROUND_LEVEL;
    public final Vector2 LATERAL_MOVE_SPEED = new Vector2(-7, 0);
    public final Vector2 ACCELERATION = new Vector2(0, 100);


    //GAME OBJECTS
    public Player player;
    private Wiggler wiggler;
    private Ground ground;
    public Array<Arrow> arrows;

    public GameWorld() {
        camera = new OrthographicCamera();
        camera.setToOrtho(true, GameScreen.GAME_WIDTH, GameScreen.GAME_HEIGHT);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        GROUND_LEVEL = (int) GameScreen.GAME_HEIGHT - AssetLoader.tileGrass.getRegionHeight() + 1;

        //GAME OBJECTS
        player = new Player(new Vector2(10, GROUND_LEVEL - AssetLoader.archerGuyFront1.getRegionHeight()),
                new Vector2(0, 0), ACCELERATION);
        player.setGroundLevel(GROUND_LEVEL);
        ground = new Ground(new Vector2(0, GROUND_LEVEL), LATERAL_MOVE_SPEED, new Vector2(0, 0));
        wiggler = new Wiggler(new Vector2(GameScreen.GAME_WIDTH, 10), LATERAL_MOVE_SPEED, new Vector2(0, 0));
        arrows = new Array<Arrow>();
    }


    public void update(float delta) {
        ground.update(delta);
        player.update(delta);
        wiggler.update(delta);
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
        wiggler.render(runTime, batch);

        for (int i = 0; i < arrows.size; i++) {
            arrows.get(i).render(runTime, batch);
        }

        shapeRenderer.end();

        batch.end(); //END SPRITE BATCH
    }

}
