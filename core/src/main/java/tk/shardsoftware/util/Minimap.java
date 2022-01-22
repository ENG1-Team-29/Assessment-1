package tk.shardsoftware.util;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import tk.shardsoftware.TileType;
import tk.shardsoftware.World;

/**
 * Draws a Minimap to the screen to help the player navigate.
 * 
 * @author Hector Woods
 * @author James Burnell
 */
public class Minimap implements Disposable {

	private World worldObj;
	private Texture miniMapBorder;
	private Texture wholeMap;

	public float x;
	public float y;
	public int width;
	public int height;

	public Minimap(World world, float x, float y, int width, int height) {
		this.worldObj = world;
		miniMapBorder = ResourceUtil.getTexture("textures/tiles/minimap-border.png");
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		prepareMap();
	}

	/** Generate an image of the entire map */
	public void prepareMap() {
		Pixmap screen = new Pixmap(World.WORLD_WIDTH, World.WORLD_HEIGHT, Format.RGB888);

		Map<TileType, Integer> colors = getTileColors();

		for (int i = 0; i < World.WORLD_WIDTH; i++) {
			for (int j = 0; j < World.WORLD_HEIGHT; j++) {
				TileType tile = worldObj.worldMap.getTile(i, j);
				screen.setColor(colors.get(tile));
				screen.drawPixel(i, j);
			}
		}
		wholeMap = new Texture(screen);
		screen.dispose();
	}

	/** Generate a map of tile types and their corner pixel color */
	private Map<TileType, Integer> getTileColors() {
		HashMap<TileType, Integer> result = new HashMap<TileType, Integer>();
		for (TileType t : TileType.values()) {
			t.getTex().getTextureData().prepare();
			result.put(t, t.getTex().getTextureData().consumePixmap().getPixel(0, 0));
		}

		return result;
	}

	public void drawMap(SpriteBatch batch, Vector2 playerPos) {

		int playerTileX = (int) playerPos.x / worldObj.worldMap.tile_size;
		int playerTileY = (int) playerPos.y / worldObj.worldMap.tile_size;

		int startX = (int) (playerTileX - width / 2 + 1);
		int startY = (int) (playerTileY - height / 2);

		if(startX < 0){
			startX = 0;
		}
		if(startY < 0){
			startY = 0;
		}

		if(startX > width){
			startX = width;
		}
		if(startY > height){
			startY = height;
		}

		// Draw a portion of the texture
		batch.draw(wholeMap, x, y, 0, 0, width, height, 1, 1, 0, startX, startY, width, height,
				false, true);
		// Draw minimap border
		batch.draw(miniMapBorder, x, y, width, height);

	}

	@Override
	public void dispose() {
		wholeMap.dispose();
		miniMapBorder.dispose();
	}
}
