package tk.shardsoftware;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tk.shardsoftware.entity.Entity;

/** @author James Burnell */
public class World {

	/** The collection of entities that are in the world */
	private List<Entity> entities;

	/** The map of the world */
	public WorldMap worldMap;
	public boolean BUILD_MAP = true; //Set this to false for faster debugging
	private long gameTicks = 0;
	public static final int world_width = 200;
	public static final int world_height = 200;
	public static final int world_tile_size = 10;


	public World() {
		entities = new ArrayList<Entity>();
		
		worldMap = new WorldMap(world_tile_size, world_width, world_height);
		if (BUILD_MAP) {
			System.out.println("Building World");
			worldMap.buildWorld();
		}else{

		}
	}

	/**
	 * The logical game function called on each game tick
	 * 
	 * @param delta
	 *            the time between the previous update and this one
	 */
	public void update(float delta) {
		gameTicks++;
		updateEntities(delta);
	}

	/**
	 * Progress the logical step for each entity. Also remove them from the
	 * world if flag is set
	 */
	private void updateEntities(float delta) {
		Iterator<Entity> iter = entities.iterator();
		while (iter.hasNext()) {
			Entity e = iter.next();
			e.update(delta);
			if (e.remove) iter.remove();
		}
	}

	/**
	 * The number of logical updates there have been since the start of the game
	 */
	public long getGameTicks() {
		return gameTicks;
	}

	/** The list of entities contained within the world */
	public List<Entity> getEntities() {
		return entities;
	}

}
