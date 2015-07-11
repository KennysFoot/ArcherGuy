package com.zhang_000.archerguygame.gameworld;

import com.badlogic.gdx.Preferences;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.screens.SkinSelectionScreen;

public class SkinUnlockDetector {

    private Preferences prefs;
    private GameWorld world;
    private Player player;

    private boolean santaUnlocked, asianUnlocked, brownUnlocked, metallicUnlocked;

    public SkinUnlockDetector(Preferences prefs, GameWorld world) {
        this.world = world;
        player = world.getPlayer();
        this.prefs = prefs;

        santaUnlocked = prefs.getBoolean(SkinSelectionScreen.SANTA, false);
        asianUnlocked = prefs.getBoolean(SkinSelectionScreen.ASIAN, false);
        brownUnlocked = prefs.getBoolean(SkinSelectionScreen.BROWN, false);
        metallicUnlocked = prefs.getBoolean(SkinSelectionScreen.METALLIC, false);
    }

    public void update() {
        //Check to see if any new skins have been unlocked
        if (!santaUnlocked) {
            checkSanta();
        } else if (!asianUnlocked) {
            checkAsian();
        } else if (!brownUnlocked) {
            checkBrown();
        } else if (!metallicUnlocked) {
            checkMetallic();
        }
    }

    //CHECKING METHODS
    private void checkSanta() {
        if (player.getScore() > 99) {
            unlockSanta();
        }
    }

    private void checkAsian() {
        if (player.getScore() > 199) {
            unlockAsian();
        }
    }

    private void checkBrown() {
        if (player.getScore() > 299) {
            unlockBrown();
        }
    }

    private void checkMetallic() {
        if (player.getScore() > 399) {
            unlockMetallic();
        }
    }

    //UNLOCKING METHODS
    private void unlockSanta() {
        prefs.putBoolean(SkinSelectionScreen.SANTA, true);
        prefs.flush();
        santaUnlocked = true;
        world.setDisplayMessage("SANTA SKIN UNLOCKED");
    }

    private void unlockAsian() {
        prefs.putBoolean(SkinSelectionScreen.ASIAN, true);
        prefs.flush();
        asianUnlocked = true;
        world.setDisplayMessage("ASIAN SKIN UNLOCKED");
    }

    private void unlockBrown() {
        prefs.putBoolean(SkinSelectionScreen.BROWN, true);
        prefs.flush();
        brownUnlocked = true;
        world.setDisplayMessage("BROWN SKIN UNLOCKED");
    }

    private void unlockMetallic() {
        prefs.putBoolean(SkinSelectionScreen.METALLIC, true);
        prefs.flush();
        metallicUnlocked = true;
        world.setDisplayMessage("METALLIC SKIN UNLOCKED");
    }

}
