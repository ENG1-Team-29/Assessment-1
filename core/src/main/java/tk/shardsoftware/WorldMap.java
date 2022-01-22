package tk.shardsoftware;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.util.PerlinNoiseGenerator;

/**
 * @author Hector Woods
 * @author James Burnell
 */
public class WorldMap {

	/** The width and height of each tile */
	public int tile_size;
	/** The width of the map in tiles */
	public int width;
	/** The height of the map in tiles */
	public int height;
	/** The placement of each tile within the world */
	public HashMap<Vector2, TileType> tileMap = new HashMap<Vector2, TileType>();

	public PerlinNoiseGenerator perlin;

	public WorldMap(int world_tile_size, int world_width, int world_height) {
		this.tile_size = world_tile_size;
		this.width = world_width;
		this.height = world_height;
	}

	public void buildWorld(long seed) {
		Gdx.app.log("WorldMap", "Seed=" + seed);
		// clear map to allow for regeneration
		tileMap.clear();
		// choosing these values is more of an art than a science, see PerlinNoiseGenerator for more info
		this.perlin = new PerlinNoiseGenerator(2f, 100, 12, 1, 1.3f, 0.66f, width,
				height, seed);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Vector2 key = new Vector2(i, j);

				float n = perlin.noise(i, j);
				
				if (n > 0.95) {
					this.tileMap.put(key, TileType.GRASS);
				} else if (n > 0.9) {
					this.tileMap.put(key, TileType.DIRT);
				} else if (n > 0.5) {
					this.tileMap.put(key, TileType.SAND); // sand
				} else if (n > 0) {
					this.tileMap.put(key, TileType.WATER_SHALLOW);
				}
				//If none of these are satisfied then when this tile is read the result will be null which is interpreted as TileType.WATER_DEEP. This was done to save memory

			}
		}

	}

	public void drawTile(int x, int y, SpriteBatch batch) {
		Texture texture = this.getTile(x, y).getTex();
		batch.draw(texture, x * tile_size, y * tile_size, tile_size, tile_size);
	}



	// TODO: Render once to off-screen buffer then render buffer to screen
	public void drawTilesInRange(Camera cam, SpriteBatch batch) {
		int numberOfTilesX = (int) (cam.viewportWidth / tile_size);
		int numberOfTilesY = (int) (cam.viewportHeight / tile_size);
		int cameraTilePosX = (int) (cam.position.x / tile_size);
		int cameraTilePosY = (int) (cam.position.y / tile_size);
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
	 * Get the tile type of the tile positioned at (x,y). If there is no tile
	 * defined at this point, it will return {@link TileType#WATER_DEEP}.
	 */
	public TileType getTile(int x, int y) {
		return tileMap.getOrDefault(new Vector2(x, y), TileType.WATER_DEEP);
	}

	/**
	 * Returns a collection of tiles that are within the bounds of the rectangle
	 * 
	 * @param rect
	 * @param filterOnlySolid Filter only the tiles that would cause collision
	 * 
	 * @author James Burnell
	 */
	public Map<Vector2, TileType> getTilesWithinArea(Rectangle rect, boolean filterOnlySolid) {
		/* Create a scaled rectangle based on tiles, not pixels */
		int x = MathUtils.floor(rect.x / tile_size);
		int y = MathUtils.floor(rect.y / tile_size);
		int width = MathUtils.ceil(rect.width / tile_size);
		int height = MathUtils.ceil(rect.height / tile_size);
//		Rectangle scaledRect = new Rectangle(x, y, width, height);

//		/* Filter out only those tiles that are within the rectangle */
//		Map<Vector2, TileType> result = tileMap.entrySet().stream()
//				.filter(e -> (filterOnlySolid ? e.getValue().solid : true)
//						&& scaledRect.contains(e.getKey()))
//				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

		Map<Vector2, TileType> result = new HashMap<Vector2, TileType>();

		for (int i = x; i <= x + width; i++) {
			for (int j = y; j <= y + height; j++) {
				Vector2 key = new Vector2(i, j);
				TileType t = tileMap.getOrDefault(key, TileType.WATER_DEEP);
				// Skip to next tile if needs to be solid and isn't
				if (filterOnlySolid && !t.isSolid()) continue;
				result.put(key, t);
			}
		}

		return result;
	}

	/**
	 * By using streams, it returns whether or not there is at least one solid tile
	 * within a given area. Given the nature of streams and filters, the efficiency
	 * is poor.
	 * 
	 * @author James Burnell
	 * @see #isSolidTileWithinArea(Rectangle)
	 */
	@Deprecated
	public boolean isSolidTileWithinAreaStream(Rectangle rect) {
		/* Create a scaled rectangle based on tiles, not pixels */
		int x = MathUtils.floor(rect.x / tile_size);
		int y = MathUtils.floor(rect.y / tile_size);
		int width = MathUtils.ceil(rect.width / tile_size);
		int height = MathUtils.ceil(rect.height / tile_size);
		Rectangle scaledRect = new Rectangle(x, y, width, height);

		return tileMap.entrySet().stream()
				.anyMatch(e -> e.getValue().isSolid() && scaledRect.contains(e.getKey()));
	}

	/**
	 * Returns whether or not there is at least one solid tile within a given area
	 * 
	 * @author James Burnell
	 */
	public boolean isSolidTileWithinArea(Rectangle rect) {
		/* Create a scaled rectangle based on tiles, not pixels */
		int x = MathUtils.floor(rect.x / tile_size);
		int y = MathUtils.floor(rect.y / tile_size);
		int width = MathUtils.ceil(rect.width / tile_size);
		int height = MathUtils.ceil(rect.height / tile_size);

		for (int i = x; i <= x + width; i++) {
			for (int j = y; j <= y + height; j++) {
				if (getTile(i, j).isSolid()) return true;
			}
		}

		return false;
	}

}
