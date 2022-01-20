package tk.shardsoftware.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import tk.shardsoftware.World;
import tk.shardsoftware.util.ResourceUtil;

/**
 * @author James Burnell
 */
public class EntityShip extends Entity {

	public EntityShip(World worldObj) {
		super(worldObj, 0, 0, 50, 50);
		this.setTexture("textures/entity/playership.png");
		this.setMaxSpeed(100);
	}

	public void update(float delta) {
		super.update(delta);
		// direction = velocityVec.angleDeg();
		velocityVec.setAngleDeg(direction);

		// TODO: Write water drag system
		velocityVec.scl(0.99f);
	}

	/**
	 * Set the texture of the entity
	 * 
	 * @param textureName
	 *            the path/name of the texture file
	 * @return This entity object for easy building
	 */
	public EntityShip setTexture(String textureName) {
		texture = new TextureRegion(ResourceUtil.getTexture(textureName));
		return this;
	}

	public void rotateTowardsGoal(float goalAngle, float delta) {
		delta *= 60; // normalize to 60fps
		float angle = getDirection();
		// float speed = getVelocity().len();

		double rads = Math.toRadians(angle);
		addVelocity((float) Math.cos(rads) * delta,
				(float) Math.sin(rads) * delta);

		if (angle <= 90 && goalAngle >= 270) goalAngle -= 360;
		if (angle >= 270 && goalAngle <= 90) goalAngle += 360;
		if (angle > 180 && goalAngle < 90) goalAngle += 360;

		// System.out.println(Gdx.graphics.getDeltaTime() + " | "
		// + (1f / Gdx.graphics.getDeltaTime()));

		if (angle != goalAngle) {
			// delta * 2deg/s
			float turnAmount = delta * 2;

			float testAngle = Math.abs(angle - goalAngle);
			turnAmount = turnAmount > testAngle ? testAngle : turnAmount;

			rotate(angle > goalAngle ? -turnAmount : turnAmount);
		}
	}

}
