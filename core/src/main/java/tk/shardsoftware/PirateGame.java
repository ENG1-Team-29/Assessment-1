package tk.shardsoftware;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.screens.LossScreen;
import tk.shardsoftware.screens.VictoryScreen;
import tk.shardsoftware.util.ResourceUtil;

public class PirateGame extends Game {



	public AssetManager assets;
	static GameScreen currentGameScreen;

	@Override
	public void create() {
		assets = new AssetManager();
		ResourceUtil.init(assets);
		// this.setScreen(new LoadScreen(assets, this));

		/* -=TESTING ONLY=- Assets should be loaded within the loading screen */
		assets.finishLoading(); // XXX: Remove before exporting
		openNewGameScreen();
	}










	public void openNewGameScreen() {
		this.currentGameScreen = new GameScreen(assets,this);
		this.setScreen(currentGameScreen);
	}

	public void openNewLossScreen(){
		this.setScreen(new LossScreen(assets, this));
	}

	public void openNewVictoryScreen(){
		this.setScreen(new VictoryScreen(assets, this));
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
