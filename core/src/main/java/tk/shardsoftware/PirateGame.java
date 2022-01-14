package tk.shardsoftware;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.util.ResourceUtil;

public class PirateGame extends Game {

	// private LoadScreen loadScreen;
	// private GameScreen gameScreen;

	/** Controls additional logging and on-screen data */
	public static final boolean DEBUG_MODE = true;

	public AssetManager assets;

	@Override
	public void create() {
		assets = new AssetManager();
		ResourceUtil.init(assets);
		// loadScreen = new LoadScreen(assets);
		// gameScreen = new GameScreen(assets);
		// this.setScreen(new LoadScreen(assets, this));

		// -=TESTING ONLY=- Assets should be loaded within the loading screen
		assets.finishLoading(); // XXX: Remove before exporting
		openNewGameScreen();
	}

	public void openNewGameScreen() {
		this.setScreen(new GameScreen(assets));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}
}
