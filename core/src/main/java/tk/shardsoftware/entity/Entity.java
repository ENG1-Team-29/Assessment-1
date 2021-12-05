package tk.shardsoftware.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * To be used for any object in the world that moves
 * 
 * @author James Burnell
 */
public class Entity {

	/**
	 * The direction the entity is facing in degrees. Note that this can be
	 * different than the direction it is moving.
	 */
	protected float direction = 0f;
	/** The vector the entity is traveling in (unnormalized) */
	protected Vector2 velocityVec = new Vector2(0, 0);
	/** The entity's current position in the world */
	protected Vector2 positionVec = new Vector2(0, 0);
	/** The hitbox of the entity. Note that this does not rotate. */
	protected Rectangle hitbox = new Rectangle();

	// /** The World object the entity belongs to */
	// private World worldObj;

	protected Entity(/* World worldObj, */ float x, float y, float w, float h) {
		this(/* worldObj */);
		setPosition(x, y);
		hitbox.setWidth(w);
		hitbox.setHeight(h);
	}

	public Entity(/* World worldObj */) {
		// this.worldObj = worldObj;
	}

	/**
	 * The logical game function called on each game tick
	 * 
	 * @param delta
	 *            the time between the previous update and this one
	 */
	public void update(float delta) {
		stepPosition(delta);
	}

	/**
	 * Step the position forward one game tick.<br>
	 * Tests for collisions with other entities before moving.
	 * 
	 * @param delta
	 *            the time between the previous update and this one
	 */
	protected void stepPosition(float delta) {
		// Requires a list of other entities in order
		// to check collisions (from worldObj)

		// update the position of the entity
		positionVec.mulAdd(velocityVec, delta);
		// sync hitbox with entity position
		hitbox.setPosition(positionVec);
	}

	public float getDirection() {
		return direction;
	}

	/**
	 * Set the angle the entity is facing and standardize to be within 0-360
	 * degrees
	 */
	public void setDirection(float angle) {
		float temp = angle % 360;
		this.direction = temp < 0 ? temp + 360 : temp;
	}

	public Vector2 getVelocity() {
		return velocityVec;
	}

	public void setVelocity(Vector2 vel) {
		setVelocity(vel.x, vel.y);
	}

	public void setVelocity(float x, float y) {
		this.velocityVec.set(x, y);
	}

	public Vector2 getPosition() {
		return positionVec;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public void setPosition(Vector2 pos) {
		setPosition(pos.x, pos.y);
	}

	public void setPosition(float x, float y) {
		this.positionVec.set(x, y);
		hitbox.setPosition(positionVec);
	}

}
