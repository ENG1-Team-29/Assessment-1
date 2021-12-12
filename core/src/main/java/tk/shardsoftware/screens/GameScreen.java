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

import tk.shardsoftware.World;
import tk.shardsoftware.WorldMap;

/** Handles game controls, rendering, and logic */
public class GameScreen implements Screen {

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private WorldMap worldMap; // TODO: Integrate WorldMap into World

	private World worldObj;

	private int cameraSize = 100; // width and length of the camera's viewport.

	// /** The ship object that the player will control*/
	// private EntityShip player;

	public GameScreen(AssetManager assets) {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(cameraSize * 16f / 9f, cameraSize);
		worldObj = new World();

		Texture deepWaterTexture = getTileTexture("noisy-waterdeep.png");
		Texture shallowWaterTexture = getTileTexture("noisy-watershallow.png");
		Texture sandTexture = getTileTexture("noisy-sand.png");
		Texture[] worldTextures = {deepWaterTexture, shallowWaterTexture,
				sandTexture};

		worldMap = new WorldMap(1, 100, 100, worldTextures);
		worldMap.buildWorld();

		// player = new EntityShip(worldObj);

		// worldObj.getEntities().add(player);
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
		logic();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // clears the buffer
		batch.setProjectionMatrix(camera.combined);
		exampleCamera();
		camera.update();
		batch.begin();
		worldMap.drawTilesInRange(camera, cameraSize, batch);
		renderEntities();
		batch.end();
	}

	private void logic() {
		worldObj.update(Gdx.graphics.getDeltaTime());
	}

	public void renderEntities() {
		worldObj.getEntities().forEach(e -> {
			// batch.draw(e.getTexture(), e.getPosition().x, e.getPosition().y,
			// e.getHitbox().width, e.getHitbox().height);

			// Draw each entity with its own texture and apply rotation
			batch.draw(e.getTexture(), e.getPosition().x, e.getPosition().y,
					e.getHitbox().width / 2, e.getHitbox().height / 2,
					e.getHitbox().width, e.getHitbox().height, 1, 1,
					e.getDirection() - 90, false);
		});
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
