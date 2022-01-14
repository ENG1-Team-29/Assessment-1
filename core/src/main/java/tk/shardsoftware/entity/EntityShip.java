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
	}

	public void update(float delta) {
		super.update(delta);
		// direction = velocityVec.angleDeg();
		velocityVec.setAngleDeg(direction);
	}

	@Override
	public void setVelocity(float x, float y) {
		super.setVelocity(x, y);
	}

	@Override
	public void addVelocity(float x, float y) {
		super.addVelocity(x, y);
	}

	/**
	 * Adds a value to the direction
	 */
	public void rotate(float angle) {
		this.setDirection(direction + angle);
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

}
