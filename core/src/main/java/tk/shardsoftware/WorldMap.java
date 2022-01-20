package tk.shardsoftware;

import tk.shardsoftware.util.ResourceUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ParticleControllerInfluencer;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Hector Woods
 * @author James Burnell
 */
public class WorldMap {

	/** The different tiles that can be in the world */
	public enum TileType {
		WATER_DEEP("noisy-waterdeep.png"), WATER_SHALLOW(
				"noisy-watershallow.png"), SAND("noisy-sand.png");

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
		PerlinNoise Perlin = new PerlinNoise(1,60,8,1,1,0.3f,1000); //choosing these values is more of an art than a science
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Vector2 key = new Vector2(i, j);

				float n = Perlin.Noise(i,j);

				if(n > 0.5){
					this.tileMap.put(key, TileType.SAND); // sand
				}else if (n > 0.3){
					this.tileMap.put(key, TileType.WATER_SHALLOW);
				}

			}
		}
	}




	public void drawTile(int x, int y, SpriteBatch batch) {
		Texture texture = this.getTile(x, y);
		batch.draw(texture, x * this.tile_size, y * this.tile_size,
				this.tile_size, this.tile_size);
	}

	// TODO: Render once to off-screen buffer then render buffer to screen
	public void drawTilesInRange(Camera cam, int cameraSize,
			SpriteBatch batch) {
		int numberOfTiles = cameraSize / this.tile_size;
		int cameraTilePosX = (int) (cam.position.x / this.tile_size);
		int cameraTilePosY = (int) (cam.position.y / this.tile_size);
		int minX = Math.max(1, cameraTilePosX - (numberOfTiles));
		int minY = Math.max(1, cameraTilePosY - (numberOfTiles));
		int maxX = Math.min(width, cameraTilePosX + (numberOfTiles));
		int maxY = Math.min(width, cameraTilePosY + (numberOfTiles));

		for (int i = minX; i < maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				this.drawTile(i, j, batch);
			}
		}
	}

	/**
	 * Get the texture of the tile positioned at (x,y). If there is no tile
	 * defined at this point, it will return {@link TileType#WATER_DEEP}.
	 */
	public Texture getTile(int x, int y) {
		TileType tile = tileMap.getOrDefault(new Vector2(x, y),
				TileType.WATER_DEEP);
		return tile.getTex();
	}


	public static class VoronoiNoise{
		int numPoints;
		int width;
		int height;

		double maxStart = 1;
		double minStart = 0.95;

		double minStep = -0.05;
		double maxStep = -0.1;

		Random r;
		double[][] map = new double[width][height];

		private int RandomRange(int min,int max){
			return r.nextInt(max-min) + min;
		}

		public Vector2[] ChoosePoints(){
			Vector2[] points = new Vector2[numPoints];
			for(int i = 0; i < numPoints; i++){
				Vector2 v = new Vector2(RandomRange(0,width),RandomRange(0,height));
				points[i] = v;
			}
			return points;
		}

		public void InitiliaseMap(){
			for(double[] row : map){
				Arrays.fill(row,0);
			}
		}

		public void Generate(Vector2[] points){
			double[][] occupantMap = new double[width][height];
			for(int i = 0; i < numPoints; i++){
				Vector2 point = points[i];



			}

		}
		public VoronoiNoise(long seed, int width, int height){
			this.r = new Random(seed);
			this.width = width;
			this.height = height;
			InitiliaseMap();
			Vector2[] points = ChoosePoints();
			Generate(points);
		}



	}


	public static class PerlinNoise {
		/**
		 * @author Hector Woods
		 * based on the Perlin Noise algorithm by Ken Perlin, specifically the method described here (though with pseudo-random gradients):
		 * https://adrianb.io/2014/08/09/perlinnoise.html
		 * you can view the original algorithm as described by Perlin here:
		 * https://cs.nyu.edu/~perlin/doc/oscar.html
		 */
		float amplitude; //amplitude of our noise function, i.e how "tall" it is
		float frequency; //frequency of our noise function, i.e how often we reach a peak
		float lacunarity; //frequency is adjusted by this value for each octave
		float persistence; //amplitude is adjusted by this value for each octave, i.e the higher persistence is the more effect each nth octave has
		float scale; //How 'zoomed-in' our noise is. The higher the value, the more zoomed in we are.
		int octaves; //number of 'layers' per sample.
		Vector2[][] gradients;


		void PopulateGradientMatrix(int size){
			/**
			 * We generate gradients at each point in advance so that we don't have to calculate them multiple times.
			 * Gradients are random so we get a new map every time we load the game!
			 */
			for(int i = 0; i < size+1; i++){
				for(int j=0; j < size+1; j++){
					Vector2 v = new Vector2();
					v.setToRandomDirection();
					gradients[i][j] = v;
				}
			}
		}

		float Lerp(float x, float y, float i){
			return x + i * (y-x);
		}

		float GenerateNoiseValue(float x, float y){
			//Point (x,y)
			Vector2 p = new Vector2(x,y);





			//Gradient Vector points
			int x0 = (int)x; //round down
			int y0 = (int)y;
			int x1 = x0 + 1;
			int y1 = x0 + 1;




			//Gradient Vectors
			Vector2 g00 = this.gradients[x0][y0];
			Vector2 g01 = this.gradients[x0][y1];
			Vector2 g10 = this.gradients[x1][y0];
			Vector2 g11 = this.gradients[x1][y1];

			//Distance Vectors
			Vector2 d00 = new Vector2(x-x0, y-y0);
			Vector2 d01 = new Vector2(x-x0, y-y1);
			Vector2 d10 = new Vector2(x-x1, y-y0);
			Vector2 d11 = new Vector2(x-x1, y-y1);


			//Dot products
			float dP00 = d00.dot(g00);
			float dP01 = d01.dot(g01);
			float dP10 = d10.dot(g10);
			float dP11 = d11.dot(g11);


			//Linearly-Interpolate between our dot products and (x,y) relative to d00 to get a weighted average
			float dX = x - (float)x0; //This vector represents how where (x,y) is relative to the other points. (or (x,y) relative to d00)
			float dY = y - (float)y0; 		//This is what we will interpolate by.

			//dX = (6 * (float)Math.pow(dX,5)) - (15*(float)Math.pow(dX,4) + (10*(float)Math.pow(dX,3)));
			//dY = (6 * (float)Math.pow(dY,5)) - (15*(float)Math.pow(dY,4) + (10*(float)Math.pow(dY,3)));

			float l1 = Lerp(dP00,dP01,dX);
			float l2 = Lerp(dP10,dP11,dX);
			float n = Lerp(l1,l2,dY);//final weighted average of (x,y) relative to d00 and all dot products

			return n;
		}
		public float Noise(float x, float y){
			/**
			 * This function implements the concept of multi-layered noise. The function 'layers' multiple
			 * noise functions on top of each other to allow for small changes.
			 * This is the main function to call with the PerlinNoise class
			 */
			float n = 0;
			float amp = this.amplitude;
			float freq = this.frequency;


			for(int i=0; i < this.octaves; i++){
				float nV = GenerateNoiseValue(x/this.scale*frequency,y/this.scale*frequency);
				n = n + (nV * amp);
				amp = amp * this.persistence;
				freq = freq * this.lacunarity;
			}

			System.out.println(n);
			return n;
		}
		public PerlinNoise(float amplitude, float scale, int octaves, float frequency, float lacunarity, float persistence, int size){
			this.amplitude = amplitude;
			this.frequency = frequency;
			this.octaves = octaves;
			this.lacunarity = lacunarity;
			this.persistence = persistence;
			this.scale = scale;
			this.gradients = new Vector2[size+1][size+1];
			PopulateGradientMatrix(size);
		}
	}
}
