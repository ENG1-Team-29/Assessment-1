package tk.shardsoftware.screens;

import static tk.shardsoftware.util.DebugUtil.DEBUG_MODE;
import static tk.shardsoftware.util.ResourceUtil.debugFont;
import static tk.shardsoftware.util.ResourceUtil.font;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import tk.shardsoftware.World;
import tk.shardsoftware.entity.EntityShip;
import tk.shardsoftware.util.DebugUtil;

/** Handles game controls, rendering, and logic */
public class GameScreen implements Screen {

	private SpriteBatch batch, hudBatch;
	private OrthographicCamera camera;
	// width and length of the camera's viewport.
	private int cameraSize = (int) (720 / 2);

	private World worldObj;

	/** The ship object that the player will control */
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

	private int goalAngle;

	/**
	 * Calculates the goal angle of the player ship based on user input. If no
	 * input is provided, the angle will be {@code -999} to easily detect when
	 * the ship should not rotate. <br>
	 * Calculates NESW directions, their diagonals, and any cancelled directions
	 * due to contradictory inputs.
	 */
	public static int calcGoalAngle() {
		int goalAngle = -999;
		boolean up = Gdx.input.isKeyPressed(Input.Keys.W);
		boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
		boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean right = Gdx.input.isKeyPressed(Input.Keys.D);
		boolean verticalFlag = up || down;
		boolean horizontalFlag = left || right;
		boolean vertCancel = up && down;
		boolean horizCancel = left && right;
		// If a key is pressed, the ship should turn
		boolean turnFlag = (verticalFlag && !vertCancel)
				|| (horizontalFlag && !horizCancel);

		if (turnFlag) {
			if ((vertCancel || !verticalFlag) && !horizCancel) {
				goalAngle = left ? 180 : 0;
				System.out.println(1);
			} else if ((horizCancel || !horizontalFlag) && !vertCancel) {
				goalAngle = (up ? 90 : 0) + (down ? 270 : 0);
				System.out.println(2);
			} else if (horizontalFlag && verticalFlag) {
				if (down && right) {
					goalAngle = 315;
				} else {
					goalAngle = ((up ? 90 : 0) + (down ? 270 : 0)
							+ (left ? 180 : 0)) / 2;
				}
				System.out.println(3);
			}
			// ensure goal is >0
			goalAngle = goalAngle < 0 ? goalAngle + 360 : goalAngle;
		}
		return goalAngle;
	}

	public void controls() {
		goalAngle = calcGoalAngle();

		if (goalAngle != -999) player.rotateTowardsGoal(goalAngle);

		if (DEBUG_MODE && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			player.getVelocity().setZero();
		}
		// player.setPosition(0, 0);
		// System.out.println(player.getVelocity());
		// System.out.println(player.getDirection());
	}

	@Override
	public void render(float delta) {
		DebugUtil.saveProcessTime("Logic Time", () -> {
			controls();
			logic();
		});

		ScreenUtils.clear(0, 0, 0, 1); // clears the buffer
		batch.setProjectionMatrix(camera.combined);
		camera.update();
		batch.begin();

		DebugUtil.saveProcessTime("Map Draw Time", () -> {
			worldObj.worldMap.drawTilesInRange(camera, cameraSize, batch);
		});
		DebugUtil.saveProcessTime("Entity Draw Time", () -> renderEntities());

		batch.end();

		DebugUtil.saveProcessTime("Debug Draw Time", () -> {
			if (DEBUG_MODE) {
				hudBatch.begin();
				renderDebug(generateDebugStrings());
				hudBatch.end();
			}
		});

	}

	public void renderDebug(List<String> debugList) {
		for (int i = 0; i < debugList.size(); i++) {
			debugFont.draw(hudBatch, debugList.get(i), 0,
					Gdx.graphics.getHeight() - (font.getLineHeight()) * i);
		}
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

	private List<String> generateDebugStrings() {
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("Current angle: " + player.getDirection());
		lines.add("Goal angle: " + goalAngle);
		lines.add("FPS: " + Gdx.graphics.getFramesPerSecond());
		lines.add("");// blank line
		lines.addAll(DebugUtil.processingTimePercentages());

		return lines;
	}

	private void logic() {
		worldObj.update(Gdx.graphics.getDeltaTime());
		player.getVelocity().scl(0.99f); // TODO: Improve water drag
		player.setPosition(Vector2.Zero); // good for debugging
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
