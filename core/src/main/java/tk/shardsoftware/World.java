package tk.shardsoftware;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tk.shardsoftware.entity.Entity;

public class World {

	private List<Entity> entities;

	private long gameTicks = 0;

	public World() {
		entities = new ArrayList<Entity>();
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
