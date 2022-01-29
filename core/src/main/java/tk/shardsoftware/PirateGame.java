package tk.shardsoftware;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.screens.LossScreen;
import tk.shardsoftware.screens.VictoryScreen;
import tk.shardsoftware.util.ResourceUtil;

public class PirateGame extends Game {

	/** The universal game volume */
	public static float gameVolume = 0.5f;

	public AssetManager assets;

	@Override
	public void create() {
		assets = new AssetManager();
		ResourceUtil.init(assets);
		// this.setScreen(new LoadScreen(assets, this));

		/* -=TESTING ONLY=- Assets should be loaded within the loading screen */
		assets.finishLoading(); // XXX: Remove before exporting
		openNewGameScreen();
	}

	public static void muteVolume() {
		gameVolume = 0;
	}

	public static void setVolume(float volume) {
		gameVolume = volume;
	}

	public void openNewGameScreen() {
		this.setScreen(new GameScreen(assets, this));
	}

	public void openNewLossScreen(){
		this.setScreen(new LossScreen(assets, this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}

	public void openNewWinScreen() {
		this.setScreen(new VictoryScreen(assets, this));
	}
}
