package com.zhang_000.archerguygame;

import com.badlogic.gdx.Game;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.screens.MenuScreen;

public class ArcherGuyGame extends Game {

	@Override
	public void create () {
		AssetLoader.load();
		setScreen(new MenuScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}

}
