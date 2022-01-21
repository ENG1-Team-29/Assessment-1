package tk.shardsoftware;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.util.PerlinNoiseGenerator;
import tk.shardsoftware.util.ResourceUtil;

/**
 * @author Hector Woods
 * @author James Burnell
 */
public class WorldMap {

	/** The different tiles that can be in the world */
	public enum TileType {
		WATER_DEEP("noisy-waterdeep.png"), WATER_SHALLOW("noisy-watershallow.png"),
		SAND("noisy-sand.png");

		private Texture tex;

		private TileType(String texStr) {
			this.tex = ResourceUtil.getTileTexture(texStr);
		}

		public Texture getTex() {
			return tex;
		}
	}

	/** The width and height of each tile */
	int tile_size;
	/** The width of the map in tiles */
	int width;
	/** The height of the map in tiles */
	int height;
	/** The placement of each tile within the world */
	HashMap<Vector2, TileType> tileMap = new HashMap<Vector2, TileType>();

	public WorldMap(int world_tile_size, int world_width, int world_height) {
		this.tile_size = world_tile_size;
		this.width = world_width;
		this.height = world_height;
	}

	public void buildWorld() {
		// choosing these values is more of an art than a science
		PerlinNoiseGenerator Perlin = new PerlinNoiseGenerator(1.1f, 50, 8, 1, 1f, 0.3f, width,
				height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Vector2 key = new Vector2(i, j);

				float n = Perlin.noise(i, j);

				if (n > 0.5) {
					this.tileMap.put(key, TileType.SAND); // sand
				} else if (n > 0.4) {
					this.tileMap.put(key, TileType.WATER_SHALLOW);
				}

			}
		}
	}

	public void drawTile(int x, int y, SpriteBatch batch) {
		Texture texture = this.getTile(x, y);
		batch.draw(texture, x * this.tile_size, y * this.tile_size, this.tile_size, this.tile_size);
	}

	// TODO: Render once to off-screen buffer then render buffer to screen
	public void drawTilesInRange(Camera cam, SpriteBatch batch) {
		int numberOfTilesX = (int) (cam.viewportWidth / this.tile_size);
		int numberOfTilesY = (int) (cam.viewportHeight / this.tile_size);
		int cameraTilePosX = (int) (cam.position.x / this.tile_size);
		int cameraTilePosY = (int) (cam.position.y / this.tile_size);
		int minX = Math.max(1, cameraTilePosX - (numberOfTilesX));
		int minY = Math.max(1, cameraTilePosY - (numberOfTilesY));
		int maxX = Math.min(width, cameraTilePosX + (numberOfTilesX));
		int maxY = Math.min(height, cameraTilePosY + (numberOfTilesY));

		for (int i = minX; i < maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				this.drawTile(i, j, batch);
			}
		}
	}

	/**
	 * Get the texture of the tile positioned at (x,y). If there is no tile defined
	 * at this point, it will return {@link TileType#WATER_DEEP}.
	 */
	public Texture getTile(int x, int y) {
		TileType tile = tileMap.getOrDefault(new Vector2(x, y), TileType.WATER_DEEP);
		return tile.getTex();
	}

}
