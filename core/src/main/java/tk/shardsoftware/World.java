package tk.shardsoftware;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;

import tk.shardsoftware.entity.Entity;

/** @author James Burnell */
public class World {

	// Set this to false for faster debugging
	public static final boolean BUILD_MAP = true;

	public static final int WORLD_WIDTH = 500;
	public static final int WORLD_HEIGHT = 300;
	public static final int WORLD_TILE_SIZE = 10;

	/** The collection of entities that are in the world */
	private List<Entity> entities;

	/** The map of the world */
	public WorldMap worldMap;

	public World() {
		entities = new ArrayList<Entity>();

		this.worldMap = new WorldMap(WORLD_TILE_SIZE, WORLD_WIDTH, WORLD_HEIGHT);
		if (BUILD_MAP) {
			System.out.println("Building World");
			worldMap.buildWorld(MathUtils.random.nextLong());
		} else {

		}
	}

	/**
	 * The logical game function called on each game tick
	 * 
	 * @param delta the time between the previous update and this one
	 */
	public void update(float delta) {
		updateEntities(delta);
	}

	/**
	 * Progress the logical step for each entity. Also remove them from the world if
	 * flag is set
	 */
	private void updateEntities(float delta) {
		Iterator<Entity> iter = entities.iterator();
		while (iter.hasNext()) {
			Entity e = iter.next();
			e.update(delta);
			if (e.remove) iter.remove();
		}
	}

	/** The list of entities contained within the world */
	public List<Entity> getEntities() {
		return entities;
	}

	public static float getWidth() {
		return WORLD_TILE_SIZE * WORLD_WIDTH;
	}

	public static float getHeight() {
		return WORLD_TILE_SIZE * WORLD_HEIGHT;
	}

}
