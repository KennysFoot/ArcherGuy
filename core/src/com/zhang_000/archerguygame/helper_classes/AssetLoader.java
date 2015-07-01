package com.zhang_000.archerguygame.helper_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zhang_000.archerguygame.gameobjects.enemies.Wiggler;
import com.zhang_000.archerguygame.gameobjects.enemies.bosses.QueenWiggler;
import com.zhang_000.archerguygame.gameobjects.powerups.PowerUp;

public class AssetLoader {

    public final static int TILE_WIDTH = 22;
    public final static int TILE_HEIGHT = 14;

    //ARCHER GUY
    public static Texture archerGuyFrontTex;
    public static TextureRegion archerGuyFront1, archerGuyFront2, archerGuyFront3, AGMoving, AGUp;
    public static Animation AGFrontAnimation, AGMovingAni, AGUpAni;

    //ENEMIES
    public static Texture wiggler;
    public static TextureRegion wiggler1, wiggler2;
    public static Animation animationWiggler;

    public static Texture wigglerQueen;
    public static TextureRegion wigQueen1, wigQueen2;
    public static Animation animationWigQueen;

    //WEAPONS
    public static Texture weapons, textureExplosion;
    public static TextureRegion arrow, explodingArrow;
    public static TextureRegion energyBall1, energyBall2, energyBall3;
    public static Animation animationEnergyBall, animationExplosion;

    //Tiles
    public static Texture tilesGround;
    public static TextureRegion tileDirt, tileGrass, tileDirtLeft, tileDirtRight;

    //POWER UPS
    public static Texture powerUps;
    public static TextureRegion powUpInfiniteArrows, powUpExtraLife, powUpShield, powUpExplodingArrows;

    //FONT
    public static BitmapFont shadow, font, greenFont;

    //SOUNDS
    public static Sound soundDeath, soundLoseLife, soundGainLife;
    public static Sound soundFireArrow, soundArrowHit, soundExplodingArrowHit;
    public static Sound soundInfArrows, soundShieldActivated;
    public static Sound soundEnergyBall;

    //PREFERENCES
    private static Preferences preferences;
    public static String ARCHER_GUY = "ArcherGuy";
    public static String HIGH_SCORE = "HighScore";

    public static void load() {
        //GAME OBJECTS
        loadArcherGuy();
        loadWeapons();
        loadEnemies();
        loadTiles();
        loadPowerUps();

        //FONTS
        font = new BitmapFont(Gdx.files.internal("whitetext.fnt"));
        font.getData().setScale(0.25f, -0.25f);
        shadow = new BitmapFont(Gdx.files.internal("shadow.fnt"));
        shadow.getData().setScale(0.25f, -0.25f);
        greenFont = new BitmapFont(Gdx.files.internal("text.fnt"));
        greenFont.getData().setScale(0.25f, -0.25f);

        loadSounds();

        //Create or retrieve existing preferences file
        preferences = Gdx.app.getPreferences(ARCHER_GUY);
        if (!preferences.contains(HIGH_SCORE)) {
            preferences.putInteger(HIGH_SCORE, 0);
            preferences.flush();
        }
    }

    public static void setHighScore(int value) {
        preferences.putInteger(HIGH_SCORE, value);
        preferences.flush();
    }

    public static int getHighScore() {
        return preferences.getInteger(HIGH_SCORE);
    }

    private static void loadArcherGuy() {
        archerGuyFrontTex =  new Texture(Gdx.files.internal("archer_guy_front_belt.png"));
        archerGuyFrontTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        archerGuyFront1 = new TextureRegion(archerGuyFrontTex, 2, 1, 28, 30);
        archerGuyFront1.flip(false, true);
        archerGuyFront2 = new TextureRegion(archerGuyFrontTex, 34, 1, 28, 30);
        archerGuyFront2.flip(false, true);
        archerGuyFront3 = new TextureRegion(archerGuyFrontTex, 66, 1, 28, 30);
        archerGuyFront3.flip(false, true);

        TextureRegion[] guys = {archerGuyFront1, archerGuyFront2, archerGuyFront3};
        AGFrontAnimation = new Animation(0.25f, guys);
        AGFrontAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        AGMoving = new TextureRegion(archerGuyFrontTex, 98, 1, 28, 30);
        AGMoving.flip(false, true);
        TextureRegion[] moving = {archerGuyFront2, AGMoving};
        AGMovingAni = new Animation(0.15f, moving);
        AGMovingAni.setPlayMode(Animation.PlayMode.LOOP);

        AGUp = new TextureRegion(archerGuyFrontTex, 130, 1, 28, 30);
        AGUp.flip(false, true);
        TextureRegion[] goingUp = {archerGuyFront2, archerGuyFront3, AGUp};
        AGUpAni = new Animation(0.08f, goingUp);
        AGUpAni.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    private static void loadWeapons() {
        weapons = new Texture(Gdx.files.internal("weapons.png"));

        arrow = new TextureRegion(weapons, 6, 12, 21, 9);
        arrow.flip(false, true);

        explodingArrow = new TextureRegion(weapons, 8, 45, 22, 5);
        explodingArrow.flip(false, true);

        energyBall1 = new TextureRegion(weapons, 35, 4, QueenWiggler.EnergyBall.WIDTH, QueenWiggler.EnergyBall.HEIGHT);
        energyBall2 = new TextureRegion(weapons, 68, 4, QueenWiggler.EnergyBall.WIDTH, QueenWiggler.EnergyBall.HEIGHT);
        energyBall3 = new TextureRegion(weapons, 100, 4, QueenWiggler.EnergyBall.WIDTH, QueenWiggler.EnergyBall.HEIGHT);
        energyBall1.flip(false, true);
        energyBall2.flip(false, true);
        energyBall3.flip(false, true);
        TextureRegion[] energyBallFrames = {energyBall1, energyBall2, energyBall3};
        animationEnergyBall = new Animation(0.15f, energyBallFrames);
        animationEnergyBall.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        textureExplosion = new Texture(Gdx.files.internal("explosion2.png"));
        TextureRegion[] explosionFrames = new TextureRegion[48];
        for (int i = 0; i < explosionFrames.length; i++) {
            explosionFrames[i] = new TextureRegion(textureExplosion, i * 64, 0, 64, 64);
            explosionFrames[i].flip(false, true);
        }
        animationExplosion = new Animation(0.03f, explosionFrames);
        animationExplosion.setPlayMode(Animation.PlayMode.NORMAL);

    }

    private static void loadEnemies() {
        //WIGGLER
        wiggler = new Texture(Gdx.files.internal("wiggler.png"));
        wiggler1 = new TextureRegion(wiggler, 0, 0, Wiggler.WIDTH, Wiggler.HEIGHT);
        wiggler1.flip(false, true);
        wiggler2 = new TextureRegion(wiggler, 20, 0, Wiggler.WIDTH, Wiggler.HEIGHT);
        wiggler2.flip(false, true);
        TextureRegion[] wigglerFrames = {wiggler1, wiggler2};
        animationWiggler = new Animation(0.25f, wigglerFrames);
        animationWiggler.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        //WIGGLER QUEEN
        wigglerQueen = new Texture(Gdx.files.internal("wiggler_queen.png"));
        wigQueen1 = new TextureRegion(wigglerQueen, 0, 0, 40, 33);
        wigQueen2 = new TextureRegion(wigglerQueen, 44, 0, 38, 33 );
        wigQueen1.flip(false, true);
        wigQueen2.flip(false, true);
        TextureRegion[] wigQueenFrames = {wigQueen1, wigQueen2};
        animationWigQueen = new Animation(0.25f, wigQueenFrames);
        animationWigQueen.setPlayMode(Animation.PlayMode.LOOP);
    }

    private static void loadTiles() {
        tilesGround = new Texture(Gdx.files.internal("tiles_dirt.png"));
        tileDirt = new TextureRegion(tilesGround, 52, 32, TILE_WIDTH, TILE_HEIGHT);
        tileDirt.flip(false, true);
        tileDirtRight = new TextureRegion(tilesGround, 52, 6, TILE_WIDTH, TILE_HEIGHT);
        tileDirtRight.flip(false, true);
        tileDirtLeft = new TextureRegion(tilesGround, 10, 6, TILE_WIDTH, TILE_HEIGHT);
        tileDirtLeft.flip(false, true);
        tileGrass = new TextureRegion(tilesGround, 10, 32, TILE_WIDTH, TILE_HEIGHT);
        tileGrass.flip(false, true);
    }

    private static void loadPowerUps() {
        powerUps = new Texture(Gdx.files.internal("power_ups.png"));

        powUpInfiniteArrows = new TextureRegion(powerUps, 2, 2, PowerUp.LENGTH, PowerUp.LENGTH);
        powUpInfiniteArrows.flip(false, true);

        powUpExtraLife = new TextureRegion(powerUps, 18, 2, PowerUp.LENGTH, PowerUp.LENGTH);
        powUpExtraLife.flip(false, true);

        powUpShield = new TextureRegion(powerUps, 34, 2, PowerUp.LENGTH, PowerUp.LENGTH);
        powUpShield.flip(false, true);

        powUpExplodingArrows = new TextureRegion(powerUps, 50, 2, PowerUp.LENGTH, PowerUp.LENGTH);
        powUpExplodingArrows.flip(false, true);
    }

    private static void loadSounds() {
        soundFireArrow = Gdx.audio.newSound(Gdx.files.internal("sounds/cloth3.ogg"));
        soundArrowHit = Gdx.audio.newSound(Gdx.files.internal("sounds/arrow_impact.wav"));
        soundInfArrows = Gdx.audio.newSound(Gdx.files.internal("sounds/infinite_arrows.ogg"));
        soundDeath = Gdx.audio.newSound(Gdx.files.internal("sounds/jingles_NES00.ogg"));
        soundLoseLife = Gdx.audio.newSound(Gdx.files.internal("sounds/lose_life.ogg"));
        soundEnergyBall = Gdx.audio.newSound(Gdx.files.internal("sounds/energy_ball_fire.wav"));
        soundGainLife = Gdx.audio.newSound(Gdx.files.internal("sounds/gain_life.ogg"));
        soundExplodingArrowHit = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion_chemistry.flac"));
        soundShieldActivated = Gdx.audio.newSound(Gdx.files.internal("sounds/jingles_NES16.ogg"));
    }

    public static void dispose() {
        //Dispose textures
        archerGuyFrontTex.dispose();
        tilesGround.dispose();
        weapons.dispose();
        textureExplosion.dispose();

        //Dispose fonts
        font.dispose();
        shadow.dispose();
        greenFont.dispose();

        //Dispose sounds
        soundFireArrow.dispose();
        soundArrowHit.dispose();
        soundInfArrows.dispose();
        soundDeath.dispose();
        soundLoseLife.dispose();
        soundEnergyBall.dispose();
        soundGainLife.dispose();
        soundExplodingArrowHit.dispose();
        soundShieldActivated.dispose();
    }
}