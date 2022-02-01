package tk.shardsoftware;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.screens.LoadScreen;
import tk.shardsoftware.screens.LossScreen;
import tk.shardsoftware.screens.VictoryScreen;
import tk.shardsoftware.util.ResourceUtil;

public class PirateGame extends Game {

	public AssetManager assets;

	@Override
	public void create() {
		assets = new AssetManager();
		ResourceUtil.init(assets);
		this.setScreen(new LoadScreen(assets, this));

		/* -=TESTING ONLY=- Assets should be loaded within the loading screen */
//		assets.finishLoading(); // XXX: Remove before exporting
//		openNewGameScreen();
	}

	public void openNewGameScreen() {
		this.setScreen(new GameScreen(this));
	}

	public void openNewLossScreen() {
		this.setScreen(new LossScreen(this));
	}

	public void openNewVictoryScreen() {
		this.setScreen(new VictoryScreen(this));
	}

	public void openNewWinScreen() {
		this.setScreen(new VictoryScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}
}
