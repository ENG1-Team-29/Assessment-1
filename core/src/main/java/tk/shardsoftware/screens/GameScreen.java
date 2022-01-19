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
import com.badlogic.gdx.math.Vector3;
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

	/**
	 * Calculates the goal angle of the player ship based on user input. If no
	 * input is provided, the angle will be {@code -999} to easily detect when
	 * the ship should not rotate. <br>
	 * Calculates NESW directions, their diagonals, and any cancelled directions
	 * due to contradictory inputs.
	 * 
	 * @return -999 if there is no input,<br>
	 *         -333 if the input cancels out,<br>
	 *         the angle the player should rotate towards otherwise.
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

		boolean accelWithoutTurn = (vertCancel || horizCancel) && !turnFlag;

		if (turnFlag) {
			if ((vertCancel || !verticalFlag) && !horizCancel) {
				goalAngle = left ? 180 : 0;
			} else if ((horizCancel || !horizontalFlag) && !vertCancel) {
				goalAngle = (up ? 90 : 0) + (down ? 270 : 0);
			} else if (horizontalFlag && verticalFlag) {
				if (down && right) {
					goalAngle = 315;
				} else {
					goalAngle = ((up ? 90 : 0) + (down ? 270 : 0)
							+ (left ? 180 : 0)) / 2;
				}
			}
			// ensure goal is >0
			goalAngle = goalAngle < 0 ? goalAngle + 360 : goalAngle;
		}
		return accelWithoutTurn ? -333 : goalAngle;
	}

	private float goalAngle;

	/** Handles user input */
	public void controls() {
		goalAngle = calcGoalAngle();

		// goalAngle = -999 : No user input
		// goalAngle = -333 : Player should not turn, but should accelerate
		if (goalAngle != -999) {
			goalAngle = (goalAngle == -333) ? player.getDirection() : goalAngle;
			player.rotateTowardsGoal(goalAngle);
		}

		// Instantly halt the player movement
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
				debugFont.draw(hudBatch, "@", 1280 / 2 - 5, 720 / 2 + 5);
				hudBatch.end();
			}
		});

	}

	/** Renders the debug HUD */
	private void renderDebug(List<String> debugList) {
		for (int i = 0; i < debugList.size(); i++) {
			debugFont.draw(hudBatch, debugList.get(i), 0,
					Gdx.graphics.getHeight() - (font.getLineHeight()) * i);
		}
	}

	/** Renders all visible entities */
	private void renderEntities() {
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

	/** Generates the debug hud's displayed text */
	private List<String> generateDebugStrings() {
		ArrayList<String> lines = new ArrayList<String>();
		lines.add(
				String.format("Current angle: %5.1f", player.getDirection()));
		lines.add(String.format("Goal angle: %6.1f", goalAngle));
		lines.add("FPS: " + Gdx.graphics.getFramesPerSecond());
		lines.add("");// blank line
		lines.addAll(DebugUtil.processingTimePercentages());

		return lines;
	}

	/** Any logical processing that needs to occur in the game */
	private void logic() {
		worldObj.update(Gdx.graphics.getDeltaTime());
		player.getVelocity().scl(0.99f); // TODO: Improve water drag
		// player.setPosition(Vector2.Zero); // good for debugging

		lerpCamera(player.getCenterPoint(), 0.04f);
	}

	/**
	 * Moves the camera smoothly to the target position
	 * 
	 * @param target
	 *            the position the camera should move to
	 * @param speed
	 *            the speed ratio the camera moves by.<br>
	 *            In range [0,1], where 0 is no movement and 1 is instant
	 *            movement
	 */
	private void lerpCamera(Vector2 target, float speed) {
		Vector3 camPos = camera.position;
		camPos.x = camera.position.x + (target.x - camera.position.x) * speed;
		camPos.y = camera.position.y + (target.y - camera.position.y) * speed;
		camera.position.set(camPos);
		camera.update();
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
