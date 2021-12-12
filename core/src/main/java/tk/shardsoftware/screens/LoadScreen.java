package tk.shardsoftware.screens;

import java.io.FileNotFoundException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

import tk.shardsoftware.PirateGame;

/** @author James Burnell */
public class LoadScreen implements Screen {

	/**
	 * The main AssetManager for the game. Used to load the assets into memory
	 */
	private AssetManager assets;
	private SpriteBatch batch;
	private VideoPlayer vPlayer;

	/** The Shard Software logo */
	private Texture logo;

	/** The PirateGame object used to switch screens */
	private PirateGame pirateGameObj;

	/** The length of time the logo should be displayed in milliseconds */
	private static final long LOGO_DISPLAY_TIME = 2000;
	/** The length of time it takes for the logo to fade out in milliseconds */
	private static final long LOGO_FADE_TIME = 1000;

	public LoadScreen(AssetManager assets, PirateGame pg) {
		this.assets = assets;
		this.pirateGameObj = pg;
		logo = new Texture("textures/logo/shardlogo-fs.png");
		batch = new SpriteBatch();
		vPlayer = VideoPlayerCreator.createVideoPlayer();
		try {
			vPlayer.play(Gdx.files.internal("textures/logo/shardlogo.webm"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void show() {

	}

	private long lastFrameTime;

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 255);
		vPlayer.update();
		if (!assets.update()) {
			System.out.printf("Loaded %.1f%% of assets\n",
					assets.getProgress() * 100f);
		}
		batch.begin();
		if (vPlayer.isPlaying()) {
			batch.draw(vPlayer.getTexture(), 0, 0, Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());
			lastFrameTime = System.currentTimeMillis();
		} else {
			if (lastFrameTime + LOGO_DISPLAY_TIME > System
					.currentTimeMillis()) {
				batch.draw(logo, 0, 0, Gdx.graphics.getWidth(),
						Gdx.graphics.getHeight());
			} else {
				// TODO: Add fade before changing screen
				pirateGameObj.openNewGameScreen();
			}
		}
		batch.end();

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
