package tk.shardsoftware.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import tk.shardsoftware.PirateGame;
import tk.shardsoftware.util.ResourceUtil;

/**
 * The screen that shows up when the player's health reaches 0
 * @author Hector Woods
 */
public class VictoryScreen implements Screen {

	/**
	 * The main AssetManager for the game. Used to load the assets into memory
	 */
	private AssetManager assets;
	private SpriteBatch batch;

	/** The PirateGame object used to switch screens */
	private PirateGame pirateGameObj;
	Texture background = ResourceUtil.getTexture("textures/ui/victory-screen-background.png");

	public VictoryScreen(AssetManager assets, PirateGame pg) {
		this.assets = assets;
		this.pirateGameObj = pg;
		batch = new SpriteBatch();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		batch.dispose();
	}

}
