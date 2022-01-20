package tk.shardsoftware;

import tk.shardsoftware.util.ResourceUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.LinkedList;

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
		PerlinNoise Perlin = new PerlinNoise(1,1000,1,1,1,0.3f,1000); //choosing these values is more of an art than a science
		//VoronoiNoise Voronoi = new VoronoiNoise(1000,this.width, this.height, 100);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Vector2 key = new Vector2(i, j);

				double n = Perlin.Noise(i,j);


					if(n > 0.2){
						this.tileMap.put(key, TileType.SAND); // sand
					}else if (n > 0.3){
						this.tileMap.put(key, TileType.SAND);
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
		int numIslands;
		int width;
		int height;

		double maxStart = 1.5;
		double minStart = 0.95;

		double minStep = -0.01;
		double maxStep = -0.5;

		Random r;
		HashMap<Vector2, Double> map = new HashMap<Vector2, Double>();;
		LinkedList<Vector2> fringe = new LinkedList<Vector2>(); //FIFO Queue
		HashMap<Vector2, Integer> occupantMap = new HashMap<Vector2, Integer>(); //Records which 'island' a point belongs to, so that we do not consider it more than once in the same island


		private int RandomRange(int min,int max){ //int
			return r.nextInt(max-min) + min;
		}
		private double RandomRange(double min, double max){ //double
			return min + (max - min) * r.nextDouble();
		}

		public Vector2[] ChoosePoints(){
			Vector2[] points = new Vector2[numIslands];
			for(int i = 0; i < numIslands; i++){
				Vector2 v = new Vector2(RandomRange(0,width),RandomRange(0,height));
				points[i] = v;
			}
			return points;
		}

		public void GenerateNeighbours(Vector2 point){
			int pointIsland = occupantMap.get(point);
			if(!map.containsKey(point)){ //This is a start point
				map.put(point, RandomRange(minStart, maxStart));
			}
			for(float i = point.x-1; i <= point.x+1; i++){
				for(float j = point.y-1; j <= point.y+1; j++){
					if(!(point.x == i && point.y == j)){
						Vector2 newPoint = new Vector2(i,j);
						int existingParent = occupantMap.getOrDefault(newPoint,-1);
						if (existingParent == -1){ //if the point has already been considered by this island we don't need to add to the height.
							occupantMap.put(newPoint, pointIsland);
							double parentStep = map.get(point);
							double step = parentStep + RandomRange(minStep,maxStep);
							if(step > 0){
								map.put(newPoint,step);
								fringe.add(newPoint);
							}
						}
					}
				}
			}


		}

		public void Generate(Vector2[] points){
			for(int i = 0; i < numIslands; i++){
				Vector2 point = points[i];
				occupantMap.put(point, i);
				fringe.add(point);
			}
			while(!fringe.isEmpty()){ //FIFO queue
				GenerateNeighbours(fringe.poll()); //fringe.poll() returns the head of the queue and removes it.
			}
		}
		public VoronoiNoise(long seed, int width, int height, int numIslands){
			this.r = new Random(seed);
			this.width = width;
			this.height = height;
			this.numIslands = numIslands;
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
		Random r;
		Vector2[][] gradients;



		Vector2 RandomVector(){
			float x = r.nextFloat();
			float y = r.nextFloat();
			return new Vector2(x,y);
		}


		void PopulateGradientMatrix(int size){
			/**
			 * We generate gradients at each point in advance so that we don't have to calculate them multiple times.
			 * Gradients are random so we get a new map every time we load the game!
			 */
			for(int i = 0; i < size+1; i++){
				for(int j=0; j < size+1; j++){
					//TODO: Change this to your own random variable
					//Vector2 v = RandomVector();
					Vector2 v = new Vector2();
					v.setToRandomDirection();
					//System.out.println(v);
					gradients[i][j] = v;
				}
			}
		}

		float Lerp(float x, float y, float i){
			return x + i * (y-x);
		}

		float smoothstep(float a0, float a1,  float w){ //CHANGE THIS
			float v = w*w*w*(w*(w*6 - 15) + 10);
			return a0 + v*(a1 - a0);
		}


		float GenerateNoiseValue(float x, float y){
			//Point (x,y)
			Vector2 p = new Vector2(x,y);




			//Gradient Vector points
			int x0 = (int)Math.floor(x); //round down
			int y0 = (int)Math.floor(y);
			int x1 = x0 + 1;
			int y1 = y0 + 1;


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


			float dX = x - x0; //This vector represents how where (x,y) is relative to the other points. (or (x,y) relative to d00)
			float dY = y - y0; 		//This is what we will interpolate by.

			//Dot products
			float dP00 = d00.dot(g00);
			float dP01 = d01.dot(g01);
			float dP10 = d10.dot(g10);
			float dP11 = d11.dot(g11);


			//Linearly-Interpolate between our dot products and (x,y) relative to d00 to get a weighted average
			//dX = (6 * (float)Math.pow(dX,5)) - (15*(float)Math.pow(dX,4) + (10*(float)Math.pow(dX,3)));
			//dY = (6 * (float)Math.pow(dY,5)) - (15*(float)Math.pow(dY,4) + (10*(float)Math.pow(dY,3)));

			float l1 = smoothstep(dP00,dP01,dX);
			float l2 = smoothstep(dP10,dP11,dX);
			float n = smoothstep(l1,l2,dY);//final weighted average of (x,y) relative to d00 and all dot products

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
				float nV = GenerateNoiseValue(x/this.scale*freq,y/this.scale*freq);
				n = n + (nV * amp);
				amp = amp * this.persistence;
				freq = freq * this.lacunarity;
			}

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
			this.r = new Random();
			PopulateGradientMatrix(size);
		}
	}
}
