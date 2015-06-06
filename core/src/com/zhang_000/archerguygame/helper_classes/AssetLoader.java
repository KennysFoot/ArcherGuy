package com.zhang_000.archerguygame.helper_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {

    public static TextureRegion archerGuy;

    public static void load() {
        Texture archerGuyTex =  new Texture(Gdx.files.internal("archer_guy_front.png"));
        archerGuyTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        archerGuy = new TextureRegion(archerGuyTex);
        archerGuy.flip(false, true);


    }

    public static void dispose() {

    }
}
