package tk.shardsoftware.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DebugUtil {

	/** Controls additional logging and on-screen data */
	public static final boolean DEBUG_MODE = true;

	public static HashMap<String, Long> processingTimes = new HashMap<String, Long>();

	private static long displayTime;
	private static List<String> cachedResult;

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

}
