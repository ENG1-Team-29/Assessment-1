package tk.shardsoftware;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.screens.LoadScreen;
import tk.shardsoftware.util.ResourceUtil;

public class PirateGame extends Game {

	private LoadScreen loadScreen;
	private GameScreen gameScreen;

	public AssetManager assets;

	@Override
	public void create() {
		assets = new AssetManager();
		ResourceUtil.init(assets);
		assets.finishLoading(); // TODO: Move asset loading to loading screen
		// loadScreen = new LoadScreen(assets);
		gameScreen = new GameScreen(assets);
		this.setScreen(gameScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}
}
