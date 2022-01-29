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

	/**
	 * Constructor for EntityCannonball
	 * @param worldObj Instance of World, the world the cannonball will be a part of
	 * @param x the x-position of the cannonball
	 * @param y the y-position of the cannonball
	 * @param dirVec the initial direction of the cannonball
	 * @param parentObj the entity which shot the cannonball
	 */
	public EntityCannonball(World worldObj, float x, float y, Vector2 dirVec,
			ICannonCarrier parentObj) {
		this(worldObj, x, y, parentObj);
		setDirection(dirVec);
	}

	/**
	 * Sets the direction of the cannonball.
	 * @param dirVec the new direction for the cannonball.
	 */
	public void setDirection(Vector2 dirVec) {
		this.setVelocity(dirVec.setLength(maximumSpeed));
	}

	/**
	 *
	 * @param delta the time between the previous update and this one
	 */
	@Override
	public void update(float delta) {
		// Cannonballs spin through the air
		// System.out.println(positionVec);
		this.setDirection(direction + delta * 60 * 15);
		super.update(delta);
	}

	/**
	 * Called when the cannonball touches an entity which implements IDamageable
	 * @param obj
	 */
	public void onTouchingDamageable(IDamageable obj) {
		obj.damage(
				MathUtils.random(parentObj.getCannonDamage() - 2, parentObj.getCannonDamage() + 2));
		if(obj instanceof College){
			((College) obj).hitSound.play();
		}
		this.remove = true;
	}

	/**
	 * Checks whether an object which implements IDamageable is the same as the object which shot this cannonball
	 * @param dmgObj Object which implements IDamageable
	 * @return boolean
	 */
	@SuppressWarnings("unlikely-arg-type")
	public boolean isObjParent(IDamageable dmgObj) {
		return dmgObj.equals(parentObj);
	}

	/**
	 * Called when the cannonball touches the borders of the map.
	 */
	@Override
	public void onTouchingBorder() {
		this.remove = true;
	}

}
