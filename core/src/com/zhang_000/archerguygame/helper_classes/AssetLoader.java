package com.zhang_000.archerguygame.helper_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.gameobjects.enemies.Hopper;
import com.zhang_000.archerguygame.gameobjects.enemies.Wiggler;
import com.zhang_000.archerguygame.gameobjects.enemies.bosses.QueenWiggler;
import com.zhang_000.archerguygame.gameobjects.powerups.PowerUp;
import com.zhang_000.archerguygame.gameobjects.weapons.Explosion;
import com.zhang_000.archerguygame.screens.SettingsScreen;

public class AssetLoader {

    public final static int TILE_WIDTH = 22;
    public final static int TILE_HEIGHT = 14;

    //ARCHER GUY
    public static Texture archerGuyFrontTex;
    public static TextureRegion archerGuyFront1, archerGuyFront2, archerGuyFront3, AGMoving, AGUp;
    public static Animation AGFrontAnimation, AGMovingAni, AGUpAni;
    public static TextureRegion eyeInfArrows, eyeExplodingArrows;
    //SKINS
    public static TextureRegion AGPink1, AGSanta1, AGAsian1, AGBrown1, AGMetallic1;
    public static TextureRegion AGPink2, AGSanta2, AGAsian2, AGBrown2, AGMetallic2;
    public static Animation AGPinkMovingAni, AGPinkUpAni,
        AGSantaMovingAni, AGSantaUpAni,
        AGAsianMovingAni, AGAsianUpAni,
        AGBrownMovingAni, AGBrownUpAni,
        AGMetallicMovingAni, AGMetallicUpAni;

    //ENEMIES
    public static Texture textureEnemies;
    public static TextureRegion wiggler1, wiggler2;
    public static Animation animationWiggler;

    public static TextureRegion hopperOnGround, hopperInAir;

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
    public static Array<Sound> sounds = new Array<Sound>();
    public static Sound soundDeath, soundLoseLife, soundGainLife;
    public static Sound soundFireArrow, soundArrowHit, soundExplodingArrowHit;
    public static Sound soundInfArrows, soundShieldActivated;
    public static Sound soundEnergyBall, soundShieldHit, soundHopperHop;

    //PREFERENCES
    private static Preferences prefs;
    public static String ARCHER_GUY = "ArcherGuy";
    public static String HIGH_SCORE = "HighScore";

    //PLAY AND PAUSE BUTTONS
    public static Texture texturePlay, texturePause;
    public static TextureRegion play, pause;

    //SETTINGS GRAPHICAL ASSETS
    public static Texture settingsAssets, textureButtons;
    public static TextureRegionDrawable yes, no, mainMenuBacking;

    //SKIN SELECTION ASSETS
    public static TextureRegionDrawable nextArrow, prevArrow;
    public static TextureRegionDrawable regSkin, pinkSkin, santaSkin, asianSkin, brownSkin, metallicSkin;

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

        //Create or retrieve existing prefs file
        prefs = Gdx.app.getPreferences(ARCHER_GUY);
        if (!prefs.contains(HIGH_SCORE)) {
            prefs.putInteger(HIGH_SCORE, 0);
            prefs.flush();
        }

        //Play and Pause buttons
        texturePlay = new Texture(Gdx.files.internal("play.png"));
        texturePause = new Texture(Gdx.files.internal("pause.png"));
        play = new TextureRegion(texturePlay);
        pause = new TextureRegion(texturePause);
        play.flip(false, true);
        pause.flip(false, true);

        //SETTINGS ASSETS
        settingsAssets = new Texture(Gdx.files.internal("settings_yes_no.png"));
        TextureRegion yesReg = new TextureRegion(settingsAssets, 31, 0, 31, 30);
        yesReg.flip(false, true);
        yes = new TextureRegionDrawable(yesReg);
        TextureRegion noReg = new TextureRegion(settingsAssets, 0, 0, 31, 30);
        noReg.flip(false, true);
        no = new TextureRegionDrawable(noReg);

        loadButtons();
    }

    public static void playSound(Sound sound, float volume) {
        if (prefs.getBoolean(SettingsScreen.SFX, true)) {
            sound.play(volume);
        }
    }

    public static void setHighScore(int value) {
        prefs.putInteger(HIGH_SCORE, value);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger(HIGH_SCORE);
    }

    private static void loadArcherGuy() {
        archerGuyFrontTex =  new Texture(Gdx.files.internal("archer_guy_front_belt.png"));
        archerGuyFrontTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        loadReg();
        loadPink();
        loadSanta();
        loadAsian();
        loadBrown();
        loadMetallic();

        eyeInfArrows = new TextureRegion(archerGuyFrontTex, 2, 33, 28, 30);
        eyeExplodingArrows = new TextureRegion(archerGuyFrontTex, 34, 33, 28, 30);
        eyeInfArrows.flip(false, true);
        eyeExplodingArrows.flip(false, true);
    }

    private static void loadReg() {
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
        AGMovingAni = new Animation(0.10f, moving);
        AGMovingAni.setPlayMode(Animation.PlayMode.LOOP);

        AGUp = new TextureRegion(archerGuyFrontTex, 130, 1, 28, 30);
        AGUp.flip(false, true);
        TextureRegion[] goingUp = {archerGuyFront2, archerGuyFront3, AGUp};
        AGUpAni = new Animation(0.07f, goingUp);
        AGUpAni.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    private static void loadPink() {
        AGPink1 = new TextureRegion(archerGuyFrontTex, 2, 97, 28, 30);
        AGPink1.flip(false, true);
        AGPink2 = new TextureRegion(archerGuyFrontTex, 34, 97, 28, 30);
        AGPink2.flip(false, true);
        TextureRegion AGPink3 = new TextureRegion(archerGuyFrontTex, 66, 97, 28, 30);
        AGPink3.flip(false, true);

        TextureRegion AGPinkMoving = new TextureRegion(archerGuyFrontTex, 98, 97, 28, 30);
        AGPinkMoving.flip(false, true);
        TextureRegion[] moving = {AGPink2, AGPinkMoving};
        AGPinkMovingAni = new Animation(0.10f, moving);
        AGPinkMovingAni.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion AGPinkUp = new TextureRegion(archerGuyFrontTex, 130, 97, 28, 30);
        AGPinkUp.flip(false, true);
        TextureRegion[] goingUp = {AGPink2, AGPink3, AGPinkUp};
        AGPinkUpAni = new Animation(0.07f, goingUp);
        AGPinkUpAni.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    private static void loadSanta() {
        AGSanta1 = new TextureRegion(archerGuyFrontTex, 2, 65, 28, 30);
        AGSanta1.flip(false, true);
        AGSanta2 = new TextureRegion(archerGuyFrontTex, 34, 65, 28, 30);
        AGSanta2.flip(false, true);
        TextureRegion AGSanta3 = new TextureRegion(archerGuyFrontTex, 66, 65, 28, 30);
        AGSanta3.flip(false, true);

        TextureRegion AGSantaMoving = new TextureRegion(archerGuyFrontTex, 98, 65, 28, 30);
        AGSantaMoving.flip(false, true);
        TextureRegion[] moving = {AGSanta2, AGSantaMoving};
        AGSantaMovingAni = new Animation(0.10f, moving);
        AGSantaMovingAni.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion AGSantaUp = new TextureRegion(archerGuyFrontTex, 130, 65, 28, 30);
        AGSantaUp.flip(false, true);
        TextureRegion[] goingUp = {AGSanta2, AGSanta3, AGSantaUp};
        AGSantaUpAni = new Animation(0.07f, goingUp);
        AGSantaUpAni.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    private static void loadAsian() {
        AGAsian1 = new TextureRegion(archerGuyFrontTex, 2, 129, 28, 30);
        AGAsian1.flip(false, true);
        AGAsian2 = new TextureRegion(archerGuyFrontTex, 34, 129, 28, 30);
        AGAsian2.flip(false, true);
        TextureRegion AGAsian3 = new TextureRegion(archerGuyFrontTex, 66, 129, 28, 30);
        AGAsian3.flip(false, true);

        TextureRegion AGAsianMoving = new TextureRegion(archerGuyFrontTex, 98, 129, 28, 30);
        AGAsianMoving.flip(false, true);
        TextureRegion[] moving = {AGAsian2, AGAsianMoving};
        AGAsianMovingAni = new Animation(0.10f, moving);
        AGAsianMovingAni.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion AGAsianUp = new TextureRegion(archerGuyFrontTex, 130, 129, 28, 30);
        AGAsianUp.flip(false, true);
        TextureRegion[] goingUp = {AGAsian2, AGAsian3, AGAsianUp};
        AGAsianUpAni = new Animation(0.07f, goingUp);
        AGAsianUpAni.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    private static void loadBrown() {
        AGBrown1 = new TextureRegion(archerGuyFrontTex, 2, 161, 28, 30);
        AGBrown1.flip(false, true);
        AGBrown2 = new TextureRegion(archerGuyFrontTex, 34, 161, 28, 30);
        AGBrown2.flip(false, true);
        TextureRegion AGBrown3 = new TextureRegion(archerGuyFrontTex, 66, 161, 28, 30);
        AGBrown3.flip(false, true);

        TextureRegion AGBrownMoving = new TextureRegion(archerGuyFrontTex, 98, 161, 28, 30);
        AGBrownMoving.flip(false, true);
        TextureRegion[] moving = {AGBrown2, AGBrownMoving};
        AGBrownMovingAni = new Animation(0.10f, moving);
        AGBrownMovingAni.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion AGBrownUp = new TextureRegion(archerGuyFrontTex, 130, 161, 28, 30);
        AGBrownUp.flip(false, true);
        TextureRegion[] goingUp = {AGBrown2, AGBrown3, AGBrownUp};
        AGBrownUpAni = new Animation(0.07f, goingUp);
        AGBrownUpAni.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    private static void loadMetallic() {
        AGMetallic1 = new TextureRegion(archerGuyFrontTex, 2, 193, 28, 30);
        AGMetallic1.flip(false, true);
        AGMetallic2 = new TextureRegion(archerGuyFrontTex, 34, 193, 28, 30);
        AGMetallic2.flip(false, true);
        TextureRegion AGMetallic3 = new TextureRegion(archerGuyFrontTex, 66, 193, 28, 30);
        AGMetallic3.flip(false, true);

        TextureRegion AGMetallicMoving = new TextureRegion(archerGuyFrontTex, 98, 193, 28, 30);
        AGMetallicMoving.flip(false, true);
        TextureRegion[] moving = {AGMetallic2, AGMetallicMoving};
        AGMetallicMovingAni = new Animation(0.10f, moving);
        AGMetallicMovingAni.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion AGMetallicUp = new TextureRegion(archerGuyFrontTex, 130, 193, 28, 30);
        AGMetallicUp.flip(false, true);
        TextureRegion[] goingUp = {AGMetallic2, AGMetallic3, AGMetallicUp};
        AGMetallicUpAni = new Animation(0.07f, goingUp);
        AGMetallicUpAni.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
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

        textureExplosion = new Texture(Gdx.files.internal("explosion.png"));
        TextureRegion[] explosionFrames = new TextureRegion[Explosion.FRAMES];
        for (int i = 0; i < explosionFrames.length; i++) {
            explosionFrames[i] = new TextureRegion(textureExplosion, i * Explosion.WIDTH, 0, Explosion.WIDTH, Explosion.HEIGHT);
            explosionFrames[i].flip(false, true);
        }
        animationExplosion = new Animation(0.035f, explosionFrames);
        animationExplosion.setPlayMode(Animation.PlayMode.NORMAL);

    }

    private static void loadEnemies() {
        //WIGGLER
        textureEnemies = new Texture(Gdx.files.internal("enemies.png"));
        wiggler1 = new TextureRegion(textureEnemies, 0, 0, Wiggler.WIDTH, Wiggler.HEIGHT);
        wiggler1.flip(false, true);
        wiggler2 = new TextureRegion(textureEnemies, 20, 0, Wiggler.WIDTH, Wiggler.HEIGHT);
        wiggler2.flip(false, true);
        TextureRegion[] wigglerFrames = {wiggler1, wiggler2};
        animationWiggler = new Animation(0.25f, wigglerFrames);
        animationWiggler.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        //HOPPERS
        hopperOnGround = new TextureRegion(textureEnemies, 23, 18, Hopper.WIDTH, Hopper.HEIGHT_AIR);
        hopperOnGround.flip(false, true);
        hopperInAir = new TextureRegion(textureEnemies, 0, 18, Hopper.WIDTH, Hopper.HEIGHT_AIR);
        hopperInAir.flip(false, true);

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
        soundShieldHit = Gdx.audio.newSound(Gdx.files.internal("sounds/slap-hit.flac"));
        soundHopperHop = Gdx.audio.newSound(Gdx.files.internal("sounds/boing.wav"));

        sounds.addAll(soundFireArrow, soundArrowHit, soundInfArrows, soundDeath, soundLoseLife, soundEnergyBall,
                soundGainLife, soundExplodingArrowHit, soundShieldActivated, soundShieldHit, soundHopperHop);
    }

    private static void loadButtons() {
        textureButtons = new Texture(Gdx.files.internal("buttons.png"));

        TextureRegion mainMenuBackingRegion = new TextureRegion(textureButtons, 0, 0, 100, 26);
        mainMenuBackingRegion.flip(false, true);
        mainMenuBacking = new TextureRegionDrawable(mainMenuBackingRegion);

        TextureRegion nextArrowReg = new TextureRegion(textureButtons, 0, 27, 13, 25);
        TextureRegion prevArrowReg = new TextureRegion(nextArrowReg);
        nextArrowReg.flip(false, true);
        prevArrowReg.flip(true, true);

        nextArrow = new TextureRegionDrawable(nextArrowReg);
        prevArrow = new TextureRegionDrawable(prevArrowReg);

        //Skin drawables
        regSkin = new TextureRegionDrawable(archerGuyFront2);
        regSkin.setMinWidth(regSkin.getMinWidth() * 2.2f);
        regSkin.setMinHeight(regSkin.getMinHeight() * 2.2f);

        pinkSkin = new TextureRegionDrawable(AGPink2);
        pinkSkin.setMinWidth(pinkSkin.getMinWidth() * 2.2f);
        pinkSkin.setMinHeight(pinkSkin.getMinHeight() * 2.2f);

        santaSkin = new TextureRegionDrawable(AGSanta2);
        santaSkin.setMinWidth(santaSkin.getMinWidth() * 2.2f);
        santaSkin.setMinHeight(santaSkin.getMinHeight() * 2.2f);

        asianSkin = new TextureRegionDrawable(AGAsian2);
        asianSkin.setMinWidth(asianSkin.getMinWidth() * 2.2f);
        asianSkin.setMinHeight(asianSkin.getMinHeight() * 2.2f);

        brownSkin = new TextureRegionDrawable(AGBrown2);
        brownSkin.setMinWidth(brownSkin.getMinWidth() * 2.2f);
        brownSkin.setMinHeight(brownSkin.getMinHeight() * 2.2f);

        metallicSkin = new TextureRegionDrawable(AGMetallic2);
        metallicSkin.setMinWidth(metallicSkin.getMinWidth() * 2.2f);
        metallicSkin.setMinHeight(metallicSkin.getMinHeight() * 2.2f);
    }

    public static void pauseSounds() {
        for (Sound s : sounds) {
            s.pause();
        }
    }

    public static void resumeSounds() {
        for (Sound s : sounds) {
            s.resume();
        }
    }

    public static void dispose() {
        //Dispose textures
        archerGuyFrontTex.dispose();
        tilesGround.dispose();
        weapons.dispose();
        textureExplosion.dispose();
        textureEnemies.dispose();
        texturePlay.dispose();
        texturePause.dispose();
        settingsAssets.dispose();

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
        soundShieldHit.dispose();
        soundHopperHop.dispose();
    }
}