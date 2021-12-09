package tk.shardsoftware;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.HashMap;

class WorldMap {
	Texture deepWaterTexture;
	Texture shallowWaterTexture;
	Texture sandTexture;
	int tile_size; //squared
	int width; //in terms of number of tiles. In terms of pixels is tile_width * tile_size.
	int height; //ditto
	HashMap<String, Texture> tileMap = new HashMap<String, Texture>();

	public WorldMap(int world_tile_size, int world_width, int world_height){
		this.deepWaterTexture = new Texture("noisy-waterdeep.png");
		this.shallowWaterTexture = new Texture("noisy-watershallow.png");
		this.sandTexture = new Texture("noisy-sand.png");
		this.tile_size = world_tile_size;
		this.width = world_width;
		this.height = world_height;
	}

	public void buildWorld(){
		for(int i =0 ; i < width; i++){
			for(int j=0; j < height; j++){
				String key = Integer.toString(i) + " " +  Integer.toString(j);
				if(i % 3 ==0){
					this.tileMap.put(key, this.deepWaterTexture);
				}else{
					this.tileMap.put(key, this.sandTexture);
				}

			}
		}
	}

	public void DrawTile(int x, int y, SpriteBatch batch){
		Texture texture = this.getTile(x,y);
		if(texture != null){ //will be null if trying to draw a tile out of bounds
			batch.draw(texture, x*this.tile_size, y*this.tile_size, (float)this.tile_size, (float)this.tile_size);
		}
	}

	public void DrawTilesInRange(int cameraX, int cameraY, int cameraSize, SpriteBatch batch){
		int numberOfTiles = cameraSize / this.tile_size;
		int cameraTilePosX = cameraX/this.tile_size;
		int cameraTilePosY = cameraY/this.tile_size;
		int minX = Math.max(1, cameraTilePosX-(numberOfTiles/2));
		int minY = Math.max(1, cameraTilePosY-(numberOfTiles/2));
		int maxX = Math.min(width,cameraTilePosX + (numberOfTiles/2));
		int maxY = Math.min(width, cameraTilePosY + (numberOfTiles/2));

		for (int i = minX; i < maxX; i++){
			for(int j = minY; j < maxY; j++){
				this.DrawTile(i, j, batch);
			}
		}
	}

	public Texture getTile(int x, int y) {
		String key = Integer.toString(x) + " " + Integer.toString(y);
		return this.tileMap.get(key);
	}
}




public class PirateGame extends ApplicationAdapter {
	SpriteBatch batch;
	private OrthographicCamera camera;
	Texture img;
	int cameraX = 0;
	int cameraY = 0;
	int cameraSize = 100; //width and length of the camera's viewport.
	WorldMap world;
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(cameraSize, cameraSize);
		camera.position.set(cameraX, cameraY, 0);
		world = new WorldMap(1,100,100);
		world.buildWorld();
	}

	public void exampleCamera(){ //example camera that moves with WASD.
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			cameraY = cameraY + 1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			cameraY = cameraY - 1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			cameraX = cameraX - 1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			cameraX = cameraX + 1;
		}
	}

	@Override
	public void render () {
		//Set ProjectionMatrix of SpriteBatch (2)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);      //clears the buffer
		batch.setProjectionMatrix(camera.combined);
		exampleCamera();
		camera.position.set(cameraX, cameraY, 0);
		camera.update();
		batch.begin();
		world.DrawTilesInRange(cameraX, cameraY, cameraSize, batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//sandTexture.dispose();
	}
}
