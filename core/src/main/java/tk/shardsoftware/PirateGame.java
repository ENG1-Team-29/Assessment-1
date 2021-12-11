package tk.shardsoftware;

import static tk.shardsoftware.util.ResourceUtil.getTexture;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import tk.shardsoftware.util.ResourceUtil;

public class PirateGame extends ApplicationAdapter {

	SpriteBatch batch;
	private OrthographicCamera camera;
	int cameraSize = 100; // width and length of the camera's viewport.
	WorldMap world;

	public AssetManager assets;

	@Override
	public void create() {
		assets = new AssetManager();
		batch = new SpriteBatch();
		camera = new OrthographicCamera(cameraSize, cameraSize);
		ResourceUtil.init(assets);
		Texture deepWaterTexture = getTexture("noisy-waterdeep");
		Texture shallowWaterTexture = getTexture("noisy-watershallow");
		Texture sandTexture = getTexture("noisy-sand");
		Texture[] worldTextures = {deepWaterTexture, shallowWaterTexture,
				sandTexture};

		world = new WorldMap(1, 100, 100, worldTextures);
		world.buildWorld();
	}

	public void exampleCamera() { // example camera that moves with WASD.
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.translate(0, 1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.translate(0, -1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.translate(-1, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.translate(1, 0);
		}
	}

	@Override
	public void render() {
		// Set ProjectionMatrix of SpriteBatch (2)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // clears the buffer
		batch.setProjectionMatrix(camera.combined);
		exampleCamera();
		camera.update();
		batch.begin();
		world.drawTilesInRange(camera, cameraSize, batch);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		// sandTexture.dispose();
	}
}
