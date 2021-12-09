package tk.shardsoftware;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PirateGame extends ApplicationAdapter {

	SpriteBatch batch;
	private OrthographicCamera camera;
	int cameraSize = 100; // width and length of the camera's viewport.
	WorldMap world;

	@Override
	public void create() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(cameraSize, cameraSize);
		world = new WorldMap(1, 100, 100);
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
