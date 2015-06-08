package com.zhang_000.archerguygame.helper_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {

    public static int TILE_WIDTH = 22;
    public static int TILE_HEIGHT = 14;

    public static Texture backgroundMainMenuTex;
    public static TextureRegion backgroundMainMenu;

    //ARCHER GUY
    public static Texture archerGuyFrontTex;
    public static TextureRegion archerGuyFront1, archerGuyFront2, archerGuyFront3;
    public static Animation AGFrontAnimation;

    public static TextureRegion AGMoving;
    public static Animation AGMovingAni;

    //Tiles
    public static Texture tilesGround;
    public static TextureRegion tileDirt;
    public static TextureRegion tileDirtRight;
    public static TextureRegion tileDirtLeft;
    public static TextureRegion tileGrass;

    //FONT
    public static BitmapFont shadow, font;

    public static void load() {
        //MAIN MENU BACKGROUND
        backgroundMainMenuTex = new Texture(Gdx.files.internal("archer_guy_background_mainmenu.png"));
        backgroundMainMenuTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        backgroundMainMenu = new TextureRegion(backgroundMainMenuTex);
        backgroundMainMenu.flip(false, true);

        //GAME OBJECTS
        loadArcherGuy();

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
        TextureRegion[] moving = {archerGuyFront2, AGMoving};
        AGMovingAni = new Animation(0.3f, moving);
        AGMovingAni.setPlayMode(Animation.PlayMode.LOOP);
    }

    public static void dispose() {
        //Dispose textures
        archerGuyFrontTex.dispose();
        tilesGround.dispose();
        backgroundMainMenuTex.dispose();

        //Dispose fonts
        font.dispose();
        shadow.dispose();
    }
}
