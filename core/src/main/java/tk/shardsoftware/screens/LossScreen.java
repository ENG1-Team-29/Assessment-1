package tk.shardsoftware.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

import java.io.FileNotFoundException;

import tk.shardsoftware.PirateGame;
import tk.shardsoftware.util.ResourceUtil;

/**
 * The screen that shows up when the player's health reaches 0
 * @Author Hector Woods
 */
public class LossScreen implements Screen {

	/**
	 * The main AssetManager for the game. Used to load the assets into memory
	 */
	private AssetManager assets;
	private SpriteBatch batch;

	/** The PirateGame object used to switch screens */
	private PirateGame pirateGameObj;
	Texture background = ResourceUtil.getTexture("textures/ui/loss-screen-background.png");


	public LossScreen(AssetManager assets, PirateGame pg) {
		this.assets = assets;
		this.pirateGameObj = pg;
		batch = new SpriteBatch();
	}

	@Override
	public void show() {
		System.out.println("The player has lost, showing the loss screen...");
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
