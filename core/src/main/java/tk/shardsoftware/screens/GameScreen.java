package tk.shardsoftware.screens;

import static tk.shardsoftware.util.DebugUtil.DEBUG_MODE;
import static tk.shardsoftware.util.ResourceUtil.collegeFont;
import static tk.shardsoftware.util.ResourceUtil.debugFont;
import static tk.shardsoftware.util.ResourceUtil.font;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import tk.shardsoftware.PirateGame;
import tk.shardsoftware.TileType;
import tk.shardsoftware.World;
import tk.shardsoftware.entity.College;
import tk.shardsoftware.entity.EntityAIShip;
import tk.shardsoftware.entity.EntityShip;
import tk.shardsoftware.entity.IDamageable;
import tk.shardsoftware.util.Bar;
import tk.shardsoftware.util.Colleges;
import tk.shardsoftware.util.DebugUtil;
import tk.shardsoftware.util.Minimap;
import tk.shardsoftware.util.ResourceUtil;

/**
 * Handles game controls, rendering, and
 * 
 * @author James Burnell
 * @author Hector Woods
 */
public class GameScreen implements Screen {

	/** The sound played as the boat moves through the water */
	public Sound boatWaterMovement;
	/** Ambient ocean sounds */
	public Sound ambientOcean;
	private long soundIdBoatMovement;
	private PirateGame pg;
	private SpriteBatch batch, hudBatch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private int DEFAULT_CAMERA_ZOOM = 1;

	private InstructionOverlay instOverlay;

	private World worldObj;
	private Minimap miniMap;

	/** The ship object that the player will control */
	private EntityShip player;

	/** The number of points the player has scored */
	public int points = 0;
	/** The amount of plunder the player has stolen */
	public int plunder = 0;

	/** The text to display the points */
	public GlyphLayout pointTxtLayout;
	/**The text to display the number of remaining colleges*/
	public GlyphLayout remainingCollegeTxtLayout;


	public void addPlunder(int p){
		plunder = plunder + p;
	}


	/**
	 * 	 * Search the world map for a region that contains only water to spawn the
	 * 	 * player in. Once this has been found, it will set the player position to this
	 * 	 * location.
	 * @param player The player's ship (an instance of EntityShip)
	 */
	public void setPlayerStartPosition(EntityShip player) {
		Function<Vector2, Boolean> startPositionConditions = vector2 -> {
			for (int i = (int) vector2.x - 20; i < vector2.x + 20; i++) {
				for (int j = (int) vector2.y - 20; j < vector2.y + 20; j++) {
					if (i < 0 || i > worldObj.worldMap.width || j < 0
							|| j > worldObj.worldMap.height) {
						return false;
					}
					TileType tile = worldObj.worldMap.getTile(i, j);
					if (tile != TileType.WATER_DEEP && tile != TileType.WATER_SHALLOW) {
						return false;
					}
				}
			}
			return true;
		};
		Vector2 startPos = worldObj.worldMap.SearchMap(startPositionConditions);
		startPos.x = startPos.x * worldObj.worldMap.tile_size;
		startPos.y = startPos.y * worldObj.worldMap.tile_size;
		System.out.println("Start Position: " + startPos);
		player.setPosition(startPos);
	}

	/**
	 * Constructor for GameScreen.
	 * @param assets Unused. Instance of AssetManager.
	 */
	public GameScreen(AssetManager assets, PirateGame pg) {
		batch = new SpriteBatch();
		this.pg = pg;

		hudBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera(360 * 16f / 9f, 360);

		camera.zoom = DEFAULT_CAMERA_ZOOM;
		pointTxtLayout = new GlyphLayout();
		remainingCollegeTxtLayout = new GlyphLayout();
		instOverlay = new InstructionOverlay(hudBatch);
		instOverlay.shouldDisplay = false;

		worldObj = new World();
		player = new EntityShip(worldObj);
		EntityAIShip exampleEnemy = new EntityAIShip(worldObj, player,750,75);

		miniMap = new Minimap(worldObj, 25, Gdx.graphics.getHeight() - 150 - 25, 150, 150,
				hudBatch);
		setPlayerStartPosition(player);
		worldObj.addEntity(player);
		placeColleges();
		exampleEnemy
				.setPosition(new Vector2(player.getPosition().x - 20, player.getPosition().y - 20));
		worldObj.addEntity(exampleEnemy);

		boatWaterMovement = ResourceUtil.getSound("audio/entity/boat-water-movement.wav");
		ambientOcean = ResourceUtil.getSound("audio/ambient/ocean.wav");
	}

	/**
	 * Starts a timer that increments points and begins playing ambient noise.
	 */
	@Override
	public void show() {
		soundIdBoatMovement = boatWaterMovement.loop(0);
		ambientOcean.loop(PirateGame.gameVolume);

		// Increase the points by 1 every second
		Timer.schedule(new Task() {
			public void run() {
				pointTxtLayout.setText(font, "Points: " + (++points));
			}
		}, 1, 1);
	}

	/**
	 * Restarts the game, generating a new map with colleges, clearing entities e.t.c.
	 */
	public void Restart(){
		worldObj.clearEntities();
		player = new EntityShip(worldObj);
		worldObj.addEntity(player);
		worldObj.worldMap.buildWorld(MathUtils.random.nextLong()); //generate a new map with a random seed
		miniMap.prepareMap();
		placeColleges();
		setPlayerStartPosition(player);
		points = 0;
	}


	/**
	 * Calculates the goal angle of the player ship based on user input. If no input
	 * is provided, the angle will be {@code -999} to easily detect when the ship
	 * should not rotate. <br>
	 * Calculates NESW directions, their diagonals, and any cancelled directions due
	 * to contradictory inputs.
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
		boolean turnFlag = (verticalFlag && !vertCancel) || (horizontalFlag && !horizCancel);

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
					goalAngle = ((up ? 90 : 0) + (down ? 270 : 0) + (left ? 180 : 0)) / 2;
				}
			}
			// ensure goal is >0
			goalAngle = goalAngle < 0 ? goalAngle + 360 : goalAngle;
		}
		return accelWithoutTurn ? -333 : goalAngle;
	}

	/** Used to display the goal angle in the debug screen */
	private float goalAngle;
	/**
	 * Used to modify the max FPS during gameplay. Useful for debugging delta
	 * issues.
	 */
	private int targetFPS = 60;

	/**
	 * Calls College.generateColleges(), generating the colleges on the map, and adds them to the entity handler.
	 */
	public void placeColleges(){
		Colleges.generateColleges(worldObj,5,50);
		for(College c : Colleges.collegeList){
			worldObj.addEntity(c);
		}
	}

	/**
	 * Handles user input
	 * @param delta time since the last frame
	 */
	public void controls(float delta) {
		goalAngle = calcGoalAngle();

		// goalAngle = -999 : No user input
		// goalAngle = -333 : Player should not turn, but should accelerate
		if (goalAngle != -999) {
			goalAngle = (goalAngle == -333) ? player.getDirection() : goalAngle;
			player.rotateTowardsGoal(goalAngle, delta);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
			player.fireCannons();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			miniMap.onToggleKeyJustPressed();
		}

		// TODO: Add pause screen with toggle
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			instOverlay.shouldDisplay = !instOverlay.shouldDisplay;
		}

		if (DEBUG_MODE) {
			// Instantly halt the player movement
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				player.getVelocity().setZero();
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ADD)) {
				Gdx.graphics.setForegroundFPS(targetFPS *= 2);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT)) {
				Gdx.graphics.setForegroundFPS(targetFPS /= 2);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
				Restart();
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.N)){
				DebugUtil.damageAllEntities(worldObj, 5); //cause 5 damage to all entities
			}

		}
		// player.setPosition(0, 0);
		// System.out.println(player.getVelocity());
		// System.out.println(player.getDirection());

	}

	/**
	 * Renders the game.
	 * @param delta time since the last frame.
	 */
	@Override
	public void render(float delta) {
		DebugUtil.saveProcessTime("Logic Time", () -> {
			controls(delta);
			logic(delta);
		});

		ScreenUtils.clear(0, 0, 0, 1); // clears the buffer
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		/* Render objects in scrolling view */
		batch.begin();

		DebugUtil.saveProcessTime("Map Draw Time", () -> {
			worldObj.worldMap.drawTilesInRange(camera, batch);
		});
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		DebugUtil.saveProcessTime("Entity Draw Time", () -> renderEntities());

		batch.end();

		/* Render shapes in scrolling view */
		DebugUtil.saveProcessTime("Reload Info Render", () -> {
			if (player.timeUntilFire > 0) {
				batch.begin();
				float p = player.timeUntilFire / player.reloadTime; //time the bar should lerp by
				Vector2 start = new Vector2(player.getX(), player.getY() - 5);
				Vector2 end = new Vector2(player.getX() + player.getWidth(),
						player.getY() - 5);
				Bar.DrawBar(batch,shapeRenderer,start,end,p);
				batch.end();
			}
		});
		shapeRenderer.end();

		/* Render objects to fixed view */
		if (DEBUG_MODE) DebugUtil.saveProcessTime("Hitbox Render", () -> renderHitboxes());

		hudBatch.begin();

		miniMap.drawMap(hudBatch, player.getPosition()); // <1% draw time, no point measuring
		if (DEBUG_MODE) DebugUtil.saveProcessTime("Debug HUD Draw Time", () -> {

			renderDebug(generateDebugStrings());
			debugFont.draw(hudBatch, "@", 1280 / 2 - 5, 720 / 2 + 5);

		});
		DebugUtil.saveProcessTime("HUD Draw Time", () -> {
			// TODO: Change to allow for different screen sizes
			font.draw(hudBatch, pointTxtLayout, Gdx.graphics.getWidth() - pointTxtLayout.width - 20,
					Gdx.graphics.getHeight() - 20);
			font.draw(hudBatch, remainingCollegeTxtLayout, Gdx.graphics.getWidth() - remainingCollegeTxtLayout.width - 20, Gdx.graphics.getHeight()-60);
		});

		hudBatch.end();
		miniMap.stage.act();
		miniMap.stage.draw();
		if (instOverlay.shouldDisplay) instOverlay.render();
	}

	/** Renders the hitbox outline for all entities */
	private void renderHitboxes() {
		batch.begin();
		shapeRenderer.begin(ShapeType.Line);
		worldObj.getEntities().forEach(e -> {
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.rect(e.getHitbox().x, e.getHitbox().y, e.getHitbox().width,
					e.getHitbox().height);
		});
		shapeRenderer.end();
		batch.end();
	}

	/**
	 * Renders the debug HUD
	 * @param debugList List of strings to be drawn on the debug HUD
	 */
	private void renderDebug(List<String> debugList) {
		for (int i = 0; i < debugList.size(); i++) {
			debugFont.draw(hudBatch, debugList.get(i), 0, 20 + (debugFont.getLineHeight()) * i);
		}
	}

	/** Renders all visible entities */
	private void renderEntities() {
		worldObj.getEntities().forEach(e -> {
			// batch.draw(e.getTexture(), e.getPosition().x, e.getPosition().y,
			// e.getHitbox().width, e.getHitbox().height);

			//We draw the college name above it
			if(e instanceof College){
				String cName = ((College) e).getName() + " College";

				//Get the width of the text after we draw it
				GlyphLayout gLayout = new GlyphLayout();
				gLayout.setText(collegeFont,cName);
				float w = gLayout.width;
				collegeFont.draw(batch,cName,e.getX()-w/2,e.getY()-10);
			}
			// Draw each entity with its own texture and apply rotation
			batch.draw(e.getTexture(), e.getX(), e.getY(), e.getWidth() / 2, e.getHeight() / 2,
					e.getWidth(), e.getHeight(), 1, 1, e.getDirection(), false);
			if(e instanceof IDamageable){
				IDamageable eDamageable = ((IDamageable) e); //cast to IDamageable so we can get the health value
				float health = eDamageable.getHealth();
				float maxHealth = eDamageable.getMaxHealth();
				if(health < maxHealth){
					Bar.DrawBar(batch,shapeRenderer,new Vector2(e.getX(),e.getY()+e.getHeight()),
							new Vector2(e.getX()+e.getWidth(),e.getY()+e.getHeight()), 1-(health/maxHealth), Color.BLACK, Color.RED);
				}
			}
		});
	}

	/**
	 * Generates the debug hud's displayed text
	 * @return list of strings to be drawn on the debug hud
	 */
	private List<String> generateDebugStrings() {
		ArrayList<String> lines = new ArrayList<String>();
		lines.add(String.format("Current angle: %5.1f", player.getDirection()));
		lines.add(String.format("Goal angle: %6.1f", goalAngle));
		lines.add(String.format("Speed %4.1fp/s", player.getVelocity().len()));
		lines.add("FPS: " + Gdx.graphics.getFramesPerSecond());
		lines.add(String.format("Entities %3d", worldObj.getEntities().size()));
		lines.add("");// blank line
		lines.addAll(DebugUtil.processingTimePercentages());

		return lines;
	}

	/**
	 * Any logical processing that needs to occur in the game
	 * @param delta time since the last frame
	 */
	private void logic(float delta) {
		// Check if the player has lost the game, and if so open a loss screen
		if(player.getHealth() <= 0) {
			pg.openNewLossScreen();
		}
		
		if(worldObj.getRemainingColleges() <= 0) {
			pg.openNewWinScreen();
		}

		worldObj.update(delta);
		
		remainingCollegeTxtLayout.setText(font, "Remaining Colleges: " + worldObj.getRemainingColleges());

		lerpCamera(player.getCenterPoint(), 0.04f, delta);

		/* Sound Calculations */

		// if the game is muted, skip processing
		if (PirateGame.gameVolume == 0) return;
		float vol = (player.getVelocity().len2() / (player.getMaxSpeed() * player.getMaxSpeed()));
		boatWaterMovement.setVolume(soundIdBoatMovement, vol * PirateGame.gameVolume * 0.8f);
	}

	/**
	 * Moves the camera smoothly to the target position
	 * 
	 * @param target the position the camera should move to
	 * @param speed  the speed ratio the camera moves by.<br>
	 *               In range [0,1], where 0 is no movement and 1 is instant
	 *               movement
	 */
	private void lerpCamera(Vector2 target, float speed, float delta) {
		delta *= 60; // standardize for 60fps
		Vector3 camPos = camera.position;

		camPos.x = camera.position.x + (target.x - camera.position.x) * speed * delta;
		camPos.y = camera.position.y + (target.y - camera.position.y) * speed * delta;

		/* Confine the camera to the bounds of the map */

		float widthMaxLimit = World.WORLD_WIDTH * World.WORLD_TILE_SIZE - camera.viewportWidth / 2;
		float heightMaxLimit = World.WORLD_HEIGHT * World.WORLD_TILE_SIZE
				- camera.viewportHeight / 2;
		float widthMinLimit = camera.viewportWidth / 2 + World.WORLD_TILE_SIZE;
		float heightMinLimit = camera.viewportHeight / 2 + World.WORLD_TILE_SIZE;

		if (camPos.x < widthMinLimit) camPos.x = widthMinLimit;
		if (camPos.x > widthMaxLimit) camPos.x = widthMaxLimit;
		if (camPos.y < heightMinLimit) camPos.y = heightMinLimit;
		if (camPos.y > heightMaxLimit) camPos.y = heightMaxLimit;

		camera.position.set(camPos);
		camera.update();
	}

	/**
	 * Resize the game camera.
	 * @param width new width of the window
	 * @param height new height of the window
	 */
	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width / 2, height / 2);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		// TODO: Add hud scaling
		// hudBatch.setProjectionMatrix();
		shapeRenderer.setProjectionMatrix(camera.combined);
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
		hudBatch.dispose();
		shapeRenderer.dispose();
		miniMap.dispose();
	}
}
