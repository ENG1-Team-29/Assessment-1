package tk.shardsoftware.entity;

import tk.shardsoftware.World;

/** @author James Burnell */
public class EntityCannonball extends Entity {

	public EntityCannonball(World worldObj, float x, float y) {
		super(worldObj, x, y, 5, 5);
		this.setCenter(x, y);
		this.setMaxSpeed(250);
		this.setHitboxScale(0.5f);
		this.setIgnoreWorldCollision(true);
		this.setIgnoreEntityCollision(true);
		this.setTexture("textures/entity/cannonball.png");
		this.setSolid(false);
	}

	@Override
	public void update(float delta) {
		// Cannonballs spin through the air
		// System.out.println(positionVec);
		this.setDirection(direction + delta * 60 * 15);
		super.update(delta);
	}

}
