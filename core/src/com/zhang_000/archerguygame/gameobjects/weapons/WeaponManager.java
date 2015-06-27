package com.zhang_000.archerguygame.gameobjects.weapons;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.screens.GameScreen;

public class WeaponManager {

    private Array<Arrow> arrows = new Array<Arrow>();
    private GameWorld world;
    private Player player;

    public WeaponManager(GameWorld world) {
        this.world = world;
        player = world.getPlayer();
    }

    public void updateWeapons(float delta) {
        for (Arrow a : arrows) {
            a.update(delta);
            //Remove the arrow if it's off screen
            if (a.getX() > GameScreen.GAME_WIDTH) {
                arrows.removeValue(a, false);
            }
        }
    }

    public Array<Arrow> getArrows() {
        return arrows;
    }

    public void addArrow(float degrees) {
        arrows.add(new Arrow(player.getLeftEyePosition().cpy(),
                new Vector2(Arrow.ARROW_VELOCITY_MAGNITUDE * MathUtils.cosDeg(degrees),
                        Arrow.ARROW_VELOCITY_MAGNITUDE * MathUtils.sinDeg(degrees)),
                world.ACCELERATION.cpy(), degrees));

    }
}
