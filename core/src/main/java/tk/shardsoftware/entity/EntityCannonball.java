package tk.shardsoftware.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.World;

/** @author James Burnell */
public class EntityCannonball extends Entity {

	private ICannonCarrier parentObj;

	public EntityCannonball(World worldObj, float x, float y, ICannonCarrier parentObj) {
		super(worldObj, 5, 5);
		this.setCenter(x, y);
		this.setMaxSpeed(250);
		this.setHitboxScale(0.5f);
		this.setIgnoreWorldCollision(true);
		this.setIgnoreEntityCollision(true);
		this.setTexture("textures/entity/cannonball.png");
		this.setSolid(false);
		this.parentObj = parentObj;
	}

	public EntityCannonball(World worldObj, float x, float y, Vector2 dirVec,
			ICannonCarrier parentObj) {
		this(worldObj, x, y, parentObj);
		setDirection(dirVec);
	}

	public void setDirection(Vector2 dirVec) {
		this.setVelocity(dirVec.setLength(maximumSpeed));
	}

	@Override
	public void update(float delta) {
		// Cannonballs spin through the air
		// System.out.println(positionVec);
		this.setDirection(direction + delta * 60 * 15);
		super.update(delta);
	}

	public void onTouchingDamageable(IDamageable obj) {
		obj.damage(
				MathUtils.random(parentObj.getCannonDamage() - 2, parentObj.getCannonDamage() + 2));
		this.remove = true;
	}

	@SuppressWarnings("unlikely-arg-type")
	public boolean isObjParent(IDamageable dmgObj) {
		return dmgObj.equals(parentObj);
	}

	@Override
	public void onTouchingBorder() {
		this.remove = true;
	}

}
