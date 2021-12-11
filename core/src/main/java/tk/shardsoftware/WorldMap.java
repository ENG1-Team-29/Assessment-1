package tk.shardsoftware;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Hector Woods
 */
public class WorldMap {
	Texture deepWaterTexture;
	Texture shallowWaterTexture;
	Texture sandTexture;
	Texture[] textures;
	int tile_size; // squared
	int width; // in terms of number of tiles. In terms of pixels is tile_width
				// * tile_size.
	int height; // ditto
	HashMap<String, Integer> tileMap = new HashMap<String, Integer>();

	public WorldMap(int world_tile_size, int world_width, int world_height,
			Texture[] world_textures) {
		this.textures = world_textures;
		this.tile_size = world_tile_size;
		this.width = world_width;
		this.height = world_height;
	}

	public void buildWorld() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				String key = i + " " + j;
				if (i % 3 == 0) {
					this.tileMap.put(key, 0); // deep water
				} else {
					this.tileMap.put(key, 2); // sand
				}

			}
		}
	}

	public void drawTile(int x, int y, SpriteBatch batch) {
		Texture texture = this.getTile(x, y);
		if (texture != null) { // will be null if trying to draw a tile out of
								// bounds
			batch.draw(texture, x * this.tile_size, y * this.tile_size,
					(float) this.tile_size, (float) this.tile_size);
		}
	}

	public void drawTilesInRange(Camera cam, int cameraSize,
			SpriteBatch batch) {
		int numberOfTiles = cameraSize / this.tile_size;
		int cameraTilePosX = (int) (cam.position.x / this.tile_size);
		int cameraTilePosY = (int) (cam.position.y / this.tile_size);
		int minX = Math.max(1, cameraTilePosX - (numberOfTiles / 2));
		int minY = Math.max(1, cameraTilePosY - (numberOfTiles / 2));
		int maxX = Math.min(width, cameraTilePosX + (numberOfTiles / 2));
		int maxY = Math.min(width, cameraTilePosY + (numberOfTiles / 2));

		for (int i = minX; i < maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				this.drawTile(i, j, batch);
			}
		}
	}

	public Texture getTile(int x, int y) {
		String key = x + " " + y;
		Integer textureNum = tileMap.get(key);
		return this.textures[textureNum];
	}
}
