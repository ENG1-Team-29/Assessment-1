package tk.shardsoftware.util;

import com.badlogic.gdx.math.Vector2;

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
public class PerlinNoiseGenerator {

	/** Our noise function, i.e how "tall" it is */
	public float amplitude;
	/** The frequency of our noise function, i.e how often we reach a peak */
	public float frequency;
	/** The frequency is adjusted by this value for each octave */
	public float lacunarity;
	/**
	 * The amplitude is adjusted by this value for each octave, i.e the higher
	 * persistence is the more effect each nth octave has
	 */
	public float persistence;
	/**
	 * How 'zoomed-in' our noise is. The higher the value, the more zoomed in we
	 * are.
	 */
	public float scale;
	/** The number of 'layers' per sample. */
	public int octaves;
	public Vector2[][] gradients;

	/**
	 * We generate gradients at each point in advance so that we don't have to
	 * calculate them multiple times. Gradients are random so we get a new map every
	 * time we load the game!
	 */
	public void populateGradientMatrix(int width, int height) {
		for (int i = 0; i < width + 1; i++) {
			for (int j = 0; j < height + 1; j++) {
				Vector2 v = new Vector2();
				v.setToRandomDirection();
				gradients[i][j] = v;
			}
		}
	}

	public float lerp(float t, float a1, float a2) {
		return a1 + t * (a2 - a1);
	}

	public float ease(float t) {
		return ((6 * t - 15) * t + 10) * t * t * t;
	}

	public Vector2 getConstantVector(Vector2 v) {
		if (v.x < 0 && v.y < 0) return new Vector2(-1, -1);
		if (v.x > 0 && v.y < 0) return new Vector2(1, -1);
		if (v.x < 0 && v.y > 0) return new Vector2(-1, 1);
		return new Vector2(1, 1);
	}

	public float generateNoiseValue(float x, float y) {
		// Point (x,y)
		// Vector2 p = new Vector2(x, y);

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
		float u = ease(x - X); // This vector represents how where (x,y) is relative to the other
								// points. (or
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

	public PerlinNoiseGenerator(float amplitude, float scale, int octaves, float frequency,
			float lacunarity, float persistence, int width, int height) {
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