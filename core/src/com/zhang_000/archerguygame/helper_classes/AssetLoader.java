package com.zhang_000.archerguygame.helper_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {

    public final static int TILE_WIDTH = 22;
    public final static int TILE_HEIGHT = 14;

    //ARCHER GUY
    public static Texture archerGuyFrontTex;
    public static TextureRegion archerGuyFront1, archerGuyFront2, archerGuyFront3, AGMoving, AGUp;
    public static Animation AGFrontAnimation, AGMovingAni, AGUpAni;

    //ENEMIES
    public static Texture wiggler;
    public static TextureRegion wiggler1;
    public static TextureRegion wiggler2;
    public static Animation wigglerAni;

    //WEAPONS
    public static Texture weapons;
    public static TextureRegion arrow;

    //Tiles
    public static Texture tilesGround;
    public static TextureRegion tileDirt;
    public static TextureRegion tileDirtRight;
    public static TextureRegion tileDirtLeft;
    public static TextureRegion tileGrass;

    //FONT
    public static BitmapFont shadow, font;

    //SOUNDS
    public static Sound fireArrow, arrowHit;

    public static void load() {
        //GAME OBJECTS
        loadArcherGuy();
        loadWeapons();
        loadEnemies();

        //TILES
        tilesGround = new Texture(Gdx.files.internal("tiles_dirt.png"));
        tileDirt = new TextureRegion(tilesGround, 52, 32, TILE_WIDTH, TILE_HEIGHT);
        tileDirt.flip(false, true);
        tileDirtRight = new TextureRegion(tilesGround, 52, 6, TILE_WIDTH, TILE_HEIGHT);
        tileDirtRight.flip(false, true);
        tileDirtLeft = new TextureRegion(tilesGround, 10, 6, TILE_WIDTH, TILE_HEIGHT);
        tileDirtLeft.flip(false, true);
        tileGrass = new TextureRegion(tilesGround, 10, 32, TILE_WIDTH, TILE_HEIGHT);
        tileGrass.flip(false, true);

        //FONTS
        font = new BitmapFont(Gdx.files.internal("whitetext.fnt"));
        font.getData().setScale(0.25f, -0.25f);
        shadow = new BitmapFont(Gdx.files.internal("shadow.fnt"));
        shadow.getData().setScale(0.25f, -0.25f);

        loadSounds();
    }

    private static void loadArcherGuy() {
        archerGuyFrontTex =  new Texture(Gdx.files.internal("archer_guy_front_belt.png"));
        archerGuyFrontTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        archerGuyFront1 = new TextureRegion(archerGuyFrontTex, 2, 1, 29, 30);
        archerGuyFront1.flip(false, true);
        archerGuyFront2 = new TextureRegion(archerGuyFrontTex, 34, 1, 29, 30);
        archerGuyFront2.flip(false, true);
        archerGuyFront3 = new TextureRegion(archerGuyFrontTex, 66, 1, 29, 30);
        archerGuyFront3.flip(false, true);

        TextureRegion[] guys = {archerGuyFront1, archerGuyFront2, archerGuyFront3};
        AGFrontAnimation = new Animation(0.25f, guys);
        AGFrontAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        AGMoving = new TextureRegion(archerGuyFrontTex, 98, 1, 29, 30);
        AGMoving.flip(false, true);
        TextureRegion[] moving = {archerGuyFront2, AGMoving};
        AGMovingAni = new Animation(0.15f, moving);
        AGMovingAni.setPlayMode(Animation.PlayMode.LOOP);

        AGUp = new TextureRegion(archerGuyFrontTex, 130, 1, 29, 30);
        AGUp.flip(false, true);
        TextureRegion[] goingUp = {archerGuyFront2, archerGuyFront3, AGUp};
        AGUpAni = new Animation(0.10f, goingUp);
        AGUpAni.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    private static void loadWeapons() {
        weapons = new Texture(Gdx.files.internal("arrow.png"));
        arrow = new TextureRegion(weapons, 6, 12, 21, 9);
        arrow.flip(false, true);
    }

    private static void loadEnemies() {
        wiggler = new Texture(Gdx.files.internal("wiggler.png"));
        wiggler1 = new TextureRegion(wiggler, 0, 0, 20, 18);
        wiggler1.flip(false, true);
        wiggler2 = new TextureRegion(wiggler, 20, 0, 20, 18);
        wiggler2.flip(false, true);
        TextureRegion[] wigglerFrames = {wiggler1, wiggler2};
        wigglerAni = new Animation(0.25f, wigglerFrames);
        wigglerAni.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    private static void loadSounds() {
        fireArrow = Gdx.audio.newSound(Gdx.files.internal("sounds/cloth3.ogg"));
        arrowHit = Gdx.audio.newSound(Gdx.files.internal("sounds/arrow_impact.wav"));
    }

    public static void dispose() {
        //Dispose textures
        archerGuyFrontTex.dispose();
        tilesGround.dispose();
        weapons.dispose();

        //Dispose fonts
        font.dispose();
        shadow.dispose();

        //Dispose sounds
        fireArrow.dispose();
    }
}
