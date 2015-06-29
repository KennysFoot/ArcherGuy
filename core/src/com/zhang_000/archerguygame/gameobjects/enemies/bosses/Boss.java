package com.zhang_000.archerguygame.gameobjects.enemies.bosses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.zhang_000.archerguygame.gameobjects.GameObject;
import com.zhang_000.archerguygame.gameobjects.weapons.Weapon;

public abstract class Boss extends GameObject {

    protected int HP;
    protected Polygon hitPolygon = new Polygon();

    public abstract void render(float runTime, SpriteBatch batch);

    public Polygon getHitPolygon() {
        return hitPolygon;
    }

    public void doDamage(int damage) {
        HP -= damage;
    }

    public int getHP() {
        return HP;
    }

    public abstract Weapon getWeapon();

    public abstract void removeWeapon();

}
