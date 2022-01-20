package tk.shardsoftware.screens;

import java.io.FileNotFoundException;

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

import tk.shardsoftware.PirateGame;

/** @author James Burnell */
public class LoadScreen implements Screen {

	/**
	 * The main AssetManager for the game. Used to load the assets into memory
	 */
	private AssetManager assets;
	private SpriteBatch batch;
	/** The video player for the intro video */
	private VideoPlayer vPlayer;
	/** The sound to be played during the intro */
	private Sound logoSound;

	/** The PirateGame object used to switch screens */
	private PirateGame pirateGameObj;

	/** The Shard Software logo */
	private Sprite logo;
	/** The length of time the logo should be displayed in seconds */
	private static final float LOGO_DISPLAY_TIME = 2f;
	/** The length of time it takes for the logo to fade out in seconds */
	private static final float LOGO_FADE_TIME = 2f;
	/** Whether or not the logo is fading */
	private boolean fade = false;
	/**
	 * The opacity of the logo. This is necessary because the sprite alpha
	 * channel is not precise enough.
	 */
	private float logoAlpha = 1f;

	public LoadScreen(AssetManager assets, PirateGame pg) {
		this.assets = assets;
		this.pirateGameObj = pg;
		logo = new Sprite(new Texture("textures/logo/shardlogo-fs.png"));
		logo.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		// logoSound = Gdx.audio
		// .newSound(Gdx.files.internal("audio/logo/intro.mp3"));
		vPlayer = VideoPlayerCreator.createVideoPlayer();
		try {
			vPlayer.play(Gdx.files.internal("textures/logo/shardlogo.webm"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void show() {
		// logoSound.play(PirateGame.gameVolume);
		vPlayer.setOnCompletionListener(v -> {
			Timer.schedule(new Task() {
				public void run() {
					fade = true;
				}
			}, LOGO_DISPLAY_TIME);
		});
	}

	int count = 0;

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
		} else {
			if (fade) {
				if (logo.getColor().a > 0) {
					logo.setAlpha(logoAlpha -= (delta / LOGO_FADE_TIME));
				} else {
					// Enter different state once faded
					pirateGameObj.openNewGameScreen();
				}
			}
			logo.draw(batch);
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
		logoSound.stop();
	}

	@Override
	public void dispose() {
		batch.dispose();
		vPlayer.dispose();
		logoSound.dispose();
	}

}
