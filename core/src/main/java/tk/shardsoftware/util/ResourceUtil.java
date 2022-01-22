package tk.shardsoftware.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Handles texture access
 * 
 * @author James Burnell
 */
public class ResourceUtil {

	// prevent instantiation
	private ResourceUtil() {
	}

	/**
	 * A basic texture to be used when other textures are unavailable/missing
	 */
	public static Texture nullTexture;
	/** A basic sound to be used when other sounds are unavailable/missing */
	public static Sound nullSound;

	/** The game's asset manager */
	private static AssetManager assetManager;

	public static BitmapFont font, debugFont;

	/**
	 * Load any required textures into memory
	 * 
	 * @param assets the main asset manager for the game
	 */
	public static void init(AssetManager assets) {
		assetManager = assets;
		loadFonts();
		addTexture("textures/tiles/noisy-waterdeep.png");
		addTexture("textures/tiles/noisy-watershallow.png");
		addTexture("textures/tiles/noisy-sand.png");
		addTexture("textures/tiles/noisy-rock.png");
		addTexture("textures/tiles/noisy-grass.png");
		addTexture("textures/entity/playership.png");
		addTexture("textures/tiles/minimap-border.png");
		addTexture("textures/tiles/player-tile.png");
		nullTexture = generateNullTexture();

		addSound("audio/entity/boat-water-movement.wav");
		addSound("audio/ambient/ocean.wav");
		nullSound = Gdx.audio.newSound(Gdx.files.internal("audio/nullsound.mp3"));
	}

	/** Load the fonts to be used within the game */
	private static void loadFonts() {
		if (Gdx.files.internal("font/cozette.fnt").exists()) {
			debugFont = new BitmapFont(Gdx.files.internal("font/cozette.fnt"), false);
			debugFont.setColor(Color.WHITE);
			debugFont.getData().setScale(0.5f);
		} else {
			Gdx.app.error("error", "Could not locate cozette font file");
		}
		if (Gdx.files.internal("font/jagged-font.fnt").exists()) {
			font = new BitmapFont(Gdx.files.internal("font/jagged-font.fnt"), // jagged-font
					false);
			font.setColor(Color.WHITE);
			font.getData().setScale(0.2f);
		} else {
			Gdx.app.error("error", "Could not locate jagged font file");
		}
	}

	/**
	 * @return {@code true} if added texture, {@code false} if texture was already
	 *         in cache
	 */
	private static boolean addTexture(String img) {
		if (assetManager.contains(img)) return false;
		if (!Gdx.files.internal(img).exists()) {
			Gdx.app.error("error", "Could not locate texture file: " + img);
			return false;
		}
		assetManager.load(img, Texture.class);
		return true;
	}

	/**
	 * @return {@code true} if added sound, {@code false} if sound was already in
	 *         cache
	 */
	private static boolean addSound(String sound) {
		if (assetManager.contains(sound)) return false;
		if (!Gdx.files.internal(sound).exists()) {
			Gdx.app.error("error", "Could not locate sound file: " + sound);
			return false;
		}
		assetManager.load(sound, Sound.class);
		return true;
	}

	/**
	 * Generates the null texture which will be used if a texture cannot be found
	 */
	public static Texture generateNullTexture() {
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
	 * @param texName The filename/path of the texture
	 * @return The pre-cached Texture object
	 */
	public static Texture getTexture(String texName) {
		if (!assetManager.isLoaded(texName)) {
			Gdx.app.error("warn", String.format("texture %s is not loaded, using null", texName));
			return nullTexture;
		}
		return assetManager.contains(texName) ? assetManager.get(texName) : nullTexture;
	}

	/**
	 * Select a sound according to its filename.
	 * 
	 * @param soundName The filename/path of the sound
	 * @return The pre-cached Sound object
	 */
	public static Sound getSound(String soundName) {
		if (!assetManager.isLoaded(soundName)) {
			Gdx.app.error("warn", String.format("sound %s is not loaded, using null", soundName));
			return nullSound;
		}
		return assetManager.contains(soundName) ? assetManager.get(soundName) : nullSound;
	}

	/**
	 * Get a tile texture according to its filename. Points to the
	 * {@code textures/tiles/} folder.
	 * 
	 * @param tileName The filename of the tile texture
	 * @return The pre-cached Texture object
	 */
	public static Texture getTileTexture(String tileName) {
		return getTexture("textures/tiles/" + tileName);
	}

	/**
	 * Get an entity texture according to its filename. Points to the
	 * {@code textures/entity/} folder.
	 * 
	 * @param tileName The filename of the entity texture
	 * @return The pre-cached Texture object
	 */
	public static Texture getEntityTexture(String entityName) {
		return getTexture("textures/entity/" + entityName);
	}

}
