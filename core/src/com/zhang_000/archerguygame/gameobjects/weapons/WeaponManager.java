package com.zhang_000.archerguygame.gameobjects.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.screens.GameScreen;

public class WeaponManager {

    private Array<Arrow> arrows = new Array<Arrow>();
    private Array<Explosion> explosions = new Array<Explosion>();
    private boolean explodingArrowsActivated;

    private GameWorld world;
    private Player player;

    public WeaponManager(GameWorld world) {
        this.world = world;
        player = world.getPlayer();
        explodingArrowsActivated = false;
    }

    public void updateWeapons(float delta) {
        for (Arrow a : arrows) {
            a.update(delta);
            //Remove the arrow if it's off screen
            if (a.getX() > GameScreen.GAME_WIDTH) {
                arrows.removeValue(a, false);
            }
        }
        for (Explosion e : explosions) {
            e.update(delta);

            if (e.isAnimationFinished()) {
                explosions.removeValue(e, false);
            }
        }
    }

    public void render(float runTime, SpriteBatch batch) {
        for (Arrow a : arrows) {
            a.render(runTime, batch);
        }
        for (Explosion e : explosions) {
            e.render(runTime, batch);
        }
    }

    public Array<Arrow> getArrows() {
        return arrows;
    }

    public void addArrow(float degrees) {
        if (explodingArrowsActivated) {
            arrows.add(new ExplodingArrow(player.getLeftEyePosition().cpy(),
                    new Vector2(Arrow.VELOCITY_MAGNITUDE * MathUtils.cosDeg(degrees),
                            Arrow.VELOCITY_MAGNITUDE * MathUtils.sinDeg(degrees)),
                    world.ACCELERATION.cpy(), degrees));
        } else {
            //REGULAR ARROWS
            arrows.add(new Arrow(player.getLeftEyePosition().cpy(),
                    new Vector2(Arrow.VELOCITY_MAGNITUDE * MathUtils.cosDeg(degrees),
                            Arrow.VELOCITY_MAGNITUDE * MathUtils.sinDeg(degrees)),
                    world.ACCELERATION.cpy(), degrees));
        }
    }

    public void addExplosion(float x, float y) {
        explosions.add(new Explosion(new Vector2(x, y)));
    }

    public void setExplodingArrowsActivated(boolean b) {
        explodingArrowsActivated = b;
    }

    public boolean isExplodingArrowsActivated() {
        return explodingArrowsActivated;
    }

    public Array<Explosion> getExplosions() {
        return explosions;
    }

}
