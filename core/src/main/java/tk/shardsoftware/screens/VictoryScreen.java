package tk.shardsoftware.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import tk.shardsoftware.PirateGame;
import tk.shardsoftware.util.ResourceUtil;


/**
 * The screen that shows up when all rival colleges have been defeated
 * @Author Hector Woods
 */
public class VictoryScreen implements Screen {

	/**
	 * The main AssetManager for the game. Used to load the assets into memory
	 */
	private AssetManager assets;
	private SpriteBatch batch;

	/** Width of the display */
	private int width;
	/** Height of the display */
	private int height;

	/** The PirateGame object used to switch screens */
	private PirateGame pirateGameObj;

	/** Texture for the background */
	Texture background = ResourceUtil.getTexture("textures/ui/victory-screen-background.png");

	/** Font to use */
	BitmapFont font = ResourceUtil.font;

	/**
	 * Constructor for LossScreen
	 * @param assets AssetManager used by PirateGame
	 * @param pg An instance of PirateGame
	 */
	public VictoryScreen(AssetManager assets, PirateGame pg) {
		this.assets = assets;
		this.pirateGameObj = pg;
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
	}


	@Override
	public void show() {
		System.out.println("The player has won, showing the victory screen...");
	}

	@Override
	public void render(float delta) {
		//Restart the game when a key is pressed
		if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)){
			pirateGameObj.openNewGameScreen();
		}

		batch.begin();
		batch.draw(background,0,0,width,height);
		font.draw(batch,"Congratulations, you have triumphed! Press any key to restart...",(int)(width*0.25),(int)(height*0.6));
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
