package tk.shardsoftware.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Handles texture access
 * 
 * @author James Burnell
 */
public class ResourceUtil {

	// prevent instantiation
	private ResourceUtil() {
	}

	/** A basic texture to be used when other textures are unavailable */
	public static Texture nullTexture;

	/** The game's asset manager */
	private static AssetManager assetManager;

	/**
	 * Load any required textures into memory
	 * 
	 * @param assets
	 *            the main asset manager for the game
	 */
	public static void init(AssetManager assets) {
		assetManager = assets;
		nullTexture = generateNullTexture();
		// textureCache.put("null", nullTexture);
		addTexture("noisy-waterdeep.png");
		addTexture("noisy-watershallow.png");
		addTexture("noisy-sand.png");
	}

	/**
	 * @return {@code true} if added texture, {@code false} if texture was
	 *         already in cache
	 */
	private static boolean addTexture(String img) {
		if (assetManager.contains(img)) return false;
		assetManager.load(img, Texture.class);
		return true;
	}

	/**
	 * Generates the null texture which will be used if a texture cannot be
	 * found
	 */
	private static Texture generateNullTexture() {
		Pixmap p = new Pixmap(2, 2, Pixmap.Format.RGB888);
		p.setColor(Color.PURPLE);
		p.drawPixel(0, 0);
		p.drawPixel(1, 1);
		p.setColor(Color.BLACK);
		p.drawPixel(0, 1);
		p.drawPixel(1, 0);
		return new Texture(p);
	}

	/**
	 * Select a texture according to its filename.
	 * 
	 * @param texName
	 *            The filename/path of the texture
	 * @return The pre-cached Texture object
	 */
	public static Texture getTexture(String texName) {
		if (!assetManager.isLoaded(texName)) {
			Gdx.app.error("warn", String
					.format("texture %s is not loaded, using null", texName));
			return nullTexture;
		}
		return assetManager.contains(texName)
				? assetManager.get(texName)
				: nullTexture;
	}

}
