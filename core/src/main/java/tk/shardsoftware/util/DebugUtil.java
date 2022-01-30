package tk.shardsoftware.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tk.shardsoftware.World;

/**
 * @author James Burnell
 */
public class DebugUtil {

	/** Controls additional logging and on-screen data */
	public static final boolean DEBUG_MODE = false;

	/** Stores times of how long it took to process code snippets */
	public static HashMap<String, Long> processingTimes = new HashMap<String, Long>();

	private static long displayTime;
	private static List<String> cachedResult;

	/**
	 * Calculate the percentage time for each entry in {@link #processingTimes}
	 * and return each in a separate string, all contained in a list.
	 */
	public static List<String> processingTimePercentages() {
		if (System.currentTimeMillis() - displayTime < 1000) {
			return cachedResult;
		}
		displayTime = System.currentTimeMillis();

		ArrayList<String> result = new ArrayList<String>();

		long total = processingTimes.values().stream().reduce(0l, Long::sum);

		for (HashMap.Entry<String, Long> entry : processingTimes.entrySet()) {
			String key = entry.getKey();
			Long val = entry.getValue();
			result.add(String.format("%s: %5.2fms (%2.0f%%)", key,
					val / 1000000f, (val / (float) total) * 100));
		}

		cachedResult = result;

		return result;
	}

	/**
	 * Measure how long a code section takes to process.
	 * 
	 * @param r
	 *            the code to run and measure
	 * @return The time taken to execute the code in nanoseconds
	 */
	public static long measureProcessTime(Runnable r) {
		long t1 = System.nanoTime();
		r.run();
		return System.nanoTime() - t1;
	}

	/**
	 * Measures how long it takes to run the code then saves it to the cache for
	 * comparison & display.
	 * 
	 * @param name
	 *            the displayed name of the code being measured
	 * @param r
	 *            the code to run and measure
	 * @see #measureProcessTime(Runnable)
	 */
	public static void saveProcessTime(String name, Runnable r) {
		long time = measureProcessTime(r);
		processingTimes.put(name, time);
	}
	
	/**
	 * Causes d damage to all entities in the game. intended for debug only.
	 * @param worldObj the world object that contains the entities
	 * @param d the amount of damage
	 */
	public static void damageAllEntities(World worldObj, float d){
		worldObj.getAllDamageable().forEach(e -> e.damage(d));
	}

}
