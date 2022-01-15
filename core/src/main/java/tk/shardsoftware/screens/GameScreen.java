package tk.shardsoftware.screens;

import static tk.shardsoftware.PirateGame.DEBUG_MODE;
import static tk.shardsoftware.util.ResourceUtil.font;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import tk.shardsoftware.World;
import tk.shardsoftware.entity.EntityShip;

/** Handles game controls, rendering, and logic */
public class GameScreen implements Screen {

	private SpriteBatch batch, hudBatch;
	private OrthographicCamera camera;
	// width and length of the camera's viewport.
	private int cameraSize = (int) (720 / 2);

	private World worldObj;

	// /** The ship object that the player will control*/
	private EntityShip player;

	public GameScreen(AssetManager assets) {
		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();
		camera = new OrthographicCamera(cameraSize * 16f / 9f, cameraSize);
		worldObj = new World();

		player = new EntityShip(worldObj);

		worldObj.getEntities().add(player);
	}

	@Override
	public void show() {

	}

	public void controls() { // TODO: Rotate ship with controls
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			// player.addVelocity(0, 1);
			rotatePlayer(90);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			// player.addVelocity(0, -1);
			rotatePlayer(270);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			// player.addVelocity(-1, 0);
			rotatePlayer(180);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			// player.addVelocity(1, 0);
			rotatePlayer(0);
		}
		if (DEBUG_MODE && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			player.getVelocity().setZero();
		}
		// player.setPosition(0, 0);
		// System.out.println(player.getVelocity());
		// System.out.println(player.getDirection());
	}

	private float goalAngle;

	// TODO: Prevent multi-key speedup in turning bug
	private void rotatePlayer(float goalAngle) {
		float angle = player.getDirection();
		float speed = player.getVelocity().len();

		double rads = Math.toRadians(angle);
		player.addVelocity((float) Math.cos(rads), (float) Math.sin(rads));

		if (angle <= 90 && goalAngle >= 270) goalAngle -= 360;
		if (angle >= 270 && goalAngle <= 90) goalAngle += 360;
		if (angle > 180 && goalAngle < 90) goalAngle += 360;
		this.goalAngle = goalAngle;

		if (angle > goalAngle) {
			player.rotate(-2);
		} else if (angle < goalAngle) {
			player.rotate(2);
		}
	}

	@Override
	public void render(float delta) {
		controls();
		logic();
		ScreenUtils.clear(0, 0, 0, 1); // clears the buffer
		batch.setProjectionMatrix(camera.combined);
		camera.update();
		batch.begin();
		worldObj.worldMap.drawTilesInRange(camera, cameraSize, batch);
		renderEntities();
		batch.end();

		if (DEBUG_MODE) {
			hudBatch.begin();
			renderDebug(generateDebugStrings());
			hudBatch.end();
		}

	}

	private List<String> generateDebugStrings() {
		return List.of("Current angle: " + player.getDirection(),
				"Goal angle: " + goalAngle,
				"FPS: " + Gdx.graphics.getFramesPerSecond());
	}

	public void renderDebug(List<String> debugList) {
		for (int i = 0; i < debugList.size(); i++) {
			font.draw(hudBatch, debugList.get(i), 0,
					Gdx.graphics.getHeight() - (font.getLineHeight()) * i);
		}
	}

	private void logic() {
		worldObj.update(Gdx.graphics.getDeltaTime());
		player.getVelocity().scl(0.99f); // TODO: Improve water drag
		// player.setPosition(Vector2.Zero); // good for debugging
	}

	public void renderEntities() {
		worldObj.getEntities().forEach(e -> {
			// batch.draw(e.getTexture(), e.getPosition().x, e.getPosition().y,
			// e.getHitbox().width, e.getHitbox().height);

			// Draw each entity with its own texture and apply rotation
			batch.draw(e.getTexture(), e.getPosition().x, e.getPosition().y,
					e.getHitbox().width / 2, e.getHitbox().height / 2,
					e.getHitbox().width, e.getHitbox().height, 1, 1,
					e.getDirection(), false);
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
