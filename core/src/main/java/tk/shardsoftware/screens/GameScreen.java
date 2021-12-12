package tk.shardsoftware.screens;

import static tk.shardsoftware.util.ResourceUtil.getTileTexture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import tk.shardsoftware.WorldMap;

public class GameScreen implements Screen {

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private WorldMap world;

	private int cameraSize = 100; // width and length of the camera's viewport.

	public GameScreen(AssetManager assets) {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(cameraSize, cameraSize);

		Texture deepWaterTexture = getTileTexture("noisy-waterdeep.png");
		Texture shallowWaterTexture = getTileTexture("noisy-watershallow.png");
		Texture sandTexture = getTileTexture("noisy-sand.png");
		Texture[] worldTextures = {deepWaterTexture, shallowWaterTexture,
				sandTexture};

		world = new WorldMap(1, 100, 100, worldTextures);
		world.buildWorld();
	}

	@Override
	public void show() {

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
	public void render(float delta) {
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
