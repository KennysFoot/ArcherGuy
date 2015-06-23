package com.zhang_000.archerguygame.gameobjects.weapons;

import com.badlogic.gdx.utils.Array;
import com.zhang_000.archerguygame.screens.GameScreen;

public class WeaponManager {

    private Array<Arrow> arrows = new Array<Arrow>();

    public WeaponManager() {
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
}
