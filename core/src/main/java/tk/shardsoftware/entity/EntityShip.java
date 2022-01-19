package tk.shardsoftware.entity;

import com.badlogic.gdx.Gdx;
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
	}

	public void update(float delta) {
		super.update(delta);
		// direction = velocityVec.angleDeg();
		velocityVec.setAngleDeg(direction);
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

	public void rotateTowardsGoal(float goalAngle) {
		float angle = getDirection();
		// float speed = getVelocity().len();

		double rads = Math.toRadians(angle);
		addVelocity((float) Math.cos(rads), (float) Math.sin(rads));

		if (angle <= 90 && goalAngle >= 270) goalAngle -= 360;
		if (angle >= 270 && goalAngle <= 90) goalAngle += 360;
		if (angle > 180 && goalAngle < 90) goalAngle += 360;

		if (angle != goalAngle) {
			// delta * 60fps * 2deg/s
			float turnAmount = Gdx.graphics.getDeltaTime() * 120;
			// round down to the nearest half degree
			turnAmount = (float) (Math.floor(turnAmount * 2) / 2f);
			rotate(angle > goalAngle ? -turnAmount : turnAmount);
		}
	}

}
