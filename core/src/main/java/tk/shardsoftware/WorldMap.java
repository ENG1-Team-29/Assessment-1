package tk.shardsoftware;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.util.ResourceUtil;

/**
 * @author Hector Woods
 * @author James Burnell
 */
public class WorldMap {

	/** The different tiles that can be in the world */
	public enum TileType {
		WATER_DEEP("noisy-waterdeep.png"), WATER_SHALLOW("noisy-watershallow.png"), SAND("noisy-sand.png");

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
		PerlinNoise Perlin = new PerlinNoise(1, 50, 8, 1, 1, 0.3f, width, height);
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

	public static class VoronoiNoise {
		int numPoints;
		int width;
		int height;

		double maxStart = 1;
		double minStart = 0.95;

		double minStep = -0.05;
		double maxStep = -0.1;

		Random r;
		double[][] map = new double[width][height];

		private int randomRange(int min, int max) {
			return r.nextInt(max - min) + min;
		}

		public Vector2[] choosePoints() {
			Vector2[] points = new Vector2[numPoints];
			for (int i = 0; i < numPoints; i++) {
				Vector2 v = new Vector2(randomRange(0, width), randomRange(0, height));
				points[i] = v;
			}
			return points;
		}

		public void initiliaseMap() {
			for (double[] row : map) {
				Arrays.fill(row, 0);
			}
		}

		public void generate(Vector2[] points) {
			double[][] occupantMap = new double[width][height];
			for (int i = 0; i < numPoints; i++) {
				Vector2 point = points[i];

			}

		}

		public VoronoiNoise(long seed, int width, int height) {
			this.r = new Random(seed);
			this.width = width;
			this.height = height;
			initiliaseMap();
			Vector2[] points = choosePoints();
			generate(points);
		}

	}

	/**
	 * Based on the Perlin Noise algorithm by Ken Perlin, specifically the method
	 * described here (though with pseudo-random gradients): <a href=
	 * "https://adrianb.io/2014/08/09/perlinnoise.html">https://adrianb.io/2014/08/09/perlinnoise.html</a>
	 * <br>
	 * You can view the original algorithm as described by Perlin here: <a href=
	 * "https://cs.nyu.edu/~perlin/doc/oscar.html">https://cs.nyu.edu/~perlin/doc/oscar.html</a>
	 * 
	 * 
	 * @author Hector Woods
	 */
	public static class PerlinNoise {

		/** our noise function, i.e how "tall" it is */
		float amplitude;
		/** frequency of our noise function, i.e how often we reach a peak */
		float frequency;
		/** frequency is adjusted by this value for each octave */
		float lacunarity;
		/**
		 * amplitude is adjusted by this value for each octave, i.e the higher
		 * persistence is the more effect each nth octave has
		 */
		float persistence;
		/**
		 * How 'zoomed-in' our noise is. The higher the value, the more zoomed in we
		 * are.
		 */
		float scale;
		/** number of 'layers' per sample. */
		int octaves;
		Vector2[][] gradients;

		/**
		 * We generate gradients at each point in advance so that we don't have to
		 * calculate them multiple times. Gradients are random so we get a new map every
		 * time we load the game!
		 */
		void populateGradientMatrix(int width, int height) {
			for (int i = 0; i < width + 1; i++) {
				for (int j = 0; j < height + 1; j++) {
					Vector2 v = new Vector2();
					v.setToRandomDirection();
					gradients[i][j] = v;
				}
			}
		}

		float lerp(float t, float a1, float a2) {
			return a1 + t * (a2 - a1);
		}

		float ease(float t) {
			return ((6 * t - 15) * t + 10) * t * t * t;
		}

		Vector2 getConstantVector(Vector2 v) {
			if (v.x < 0 && v.y < 0)
				return new Vector2(-1, -1);
			if (v.x > 0 && v.y < 0)
				return new Vector2(1, -1);
			if (v.x < 0 && v.y > 0)
				return new Vector2(-1, 1);
			return new Vector2(1, 1);
		}

		float generateNoiseValue(float x, float y) {
			// Point (x,y)
			Vector2 p = new Vector2(x, y);

			// Gradient Vector points
			int X = (int) Math.floor(x); // round down
			int Y = (int) Math.floor(y);
			float xf = x - X;
			float yf = y - Y;


			// Gradient Vectors
			Vector2 topRightGrad = this.gradients[X + 1][Y + 1];
			Vector2 topLeftGrad = this.gradients[X][Y + 1];
			Vector2 btmRightGrad = this.gradients[X + 1][Y];
			Vector2 btmLeftGrad = this.gradients[X][Y];


			// Distance Vectors
			Vector2 topRightDist = new Vector2(1 - xf, 1 - yf);
			Vector2 topLeftDist = new Vector2(xf, 1 - yf);
			Vector2 btmRightDist = new Vector2(1 - xf, yf);
			Vector2 btmLeftDist = new Vector2(xf, yf);

			// Dot products
			float dPtopRight = topRightDist.dot((topRightGrad));
			float dPtopLeft = topLeftDist.dot((topLeftGrad));
			float dPbtmRight = btmRightDist.dot((btmRightGrad));
			float dPbtmLeft = btmLeftDist.dot((btmLeftGrad));

			// Linearly-Interpolate between our dot products and (x,y) relative
			// to d00 to get a weighted average
			float u = ease(x - X); // This vector represents how where (x,y) is relative to the other points. (or
									// (x,y) relative to d00)
			float v = ease(y - Y); // This is what we will interpolate by.


			float l1 = lerp(u, dPtopLeft, dPtopRight);
			float l2 = lerp(u, dPbtmLeft, dPbtmRight);
			float n = lerp(v, l2, l1);// final weighted average of (x,y)
										// relative to d00 and all dot products

			return n;
		}

		/**
		 * This function implements the concept of multi-layered noise. The function
		 * 'layers' multiple noise functions on top of each other to allow for small
		 * changes. This is the main function to call with the PerlinNoise class
		 */
		public float noise(float x, float y) {
			float n = 0;
			float amp = this.amplitude;
			float freq = this.frequency;

			for (int i = 0; i < this.octaves; i++) {
				float nV = generateNoiseValue(x / this.scale * freq, y / this.scale * freq);
				n = n + (nV * amp);
				amp = amp * this.persistence;
				freq = freq * this.lacunarity;
			}
			return n;
		}

		public PerlinNoise(float amplitude, float scale, int octaves, float frequency, float lacunarity,
				float persistence, int width, int height) {
			this.amplitude = amplitude;
			this.frequency = frequency;
			this.octaves = octaves;
			this.lacunarity = lacunarity;
			this.persistence = persistence;
			this.scale = scale;
			width = (int) (width / scale + 1);
			height = (int) (height / scale + 1);
			this.gradients = new Vector2[width + 1][height + 1];
			populateGradientMatrix(width, height);
		}
	}
}
