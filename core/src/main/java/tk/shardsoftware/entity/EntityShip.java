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
	}

	public void update(float delta) {
		super.update(delta);
	}

	@Override
	public void setVelocity(float x, float y) {
		super.setVelocity(x, y);
		direction = velocityVec.angleDeg();
	}
	
	@Override
	public void addVelocity(float x, float y) {
		super.addVelocity(x, y);
		direction = velocityVec.angleDeg();
	}

	/**
	 * Does nothing as ships cannot have their direction manually set. The
	 * direction is based on the velocity vector.
	 */
	@Override
	public void setDirection(float angle) {
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
