package tk.shardsoftware.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.World;
import tk.shardsoftware.util.ResourceUtil;

/**
 * To be used for any object in the world that moves
 * 
 * @author James Burnell
 */
public abstract class Entity {

	/** The maximum speed the entity is allowed to travel at */
	protected float maximumSpeed = 250f;
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
	/** The World object the entity belongs to */
	protected World worldObj;
	/** Tells the World object if the entity should be deleted */
	public boolean remove = false;
	/** The texture used to depict the entity */
	protected TextureRegion texture;

	protected Entity(World worldObj, float x, float y, float w, float h) {
		setPosition(x, y);
		hitbox.setWidth(w);
		hitbox.setHeight(h);
		this.worldObj = worldObj;
	}

	/**
	 * Entity constructor to be used only for test purposes where the worldObj
	 * is not required
	 */
	protected Entity() {
		this(null, 0, 0, 50, 50);
	}

	/**
	 * Set the texture of the entity
	 * 
	 * @param textureName
	 *            the path/name of the texture file
	 * @return This entity object for easy building
	 */
	public Entity setTexture(String textureName) {
		texture = new TextureRegion(ResourceUtil.getTexture(textureName));
		return this;
	}

	public TextureRegion getTexture() {
		return texture;
	}

	/**
	 * The logical game function called on each game tick
	 * 
	 * @param delta
	 *            the time between the previous update and this one
	 */
	public void update(float delta) {
		// prevent entity from traveling faster than the maximum speed
		if (velocityVec.len2() > maximumSpeed * maximumSpeed) {
			velocityVec.setLength(maximumSpeed);
		}
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
		// Calculate the hitbox after next step
		Rectangle nextHitbox = new Rectangle(hitbox).setPosition(
				positionVec.x + velocityVec.x, positionVec.y + velocityVec.y);

		//TODO: Stop ships from leaving the boundaries of the map

		// TODO: Change to bounding-box hierarchy if performance is too low
		// TODO: Add collision direction detection
		boolean collided = worldObj.getEntities().stream()
				.filter(e -> !e.equals(this))
				.anyMatch(e -> e.hitbox.overlaps(nextHitbox));
		if (!collided) {
			// update the position of the entity
			positionVec.mulAdd(velocityVec, delta);
			// sync hitbox with entity position
			hitbox.setPosition(positionVec);
		}
	}

	/** Returns the center point of the entity */
	public Vector2 getCenterPoint() {
		return new Vector2(positionVec.x + hitbox.width / 2f,
				positionVec.y + hitbox.height / 2f);
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

	/**
	 * Adds a value to the direction
	 */
	public void rotate(float angle) {
		this.setDirection(direction + angle);
	}

	public Vector2 getVelocity() {
		return velocityVec;
	}

	public void setVelocity(Vector2 vel) {
		setVelocity(vel.x, vel.y);
	}

	public void addVelocity(Vector2 vel) {
		addVelocity(vel.x, vel.y);
	}

	public void setVelocity(float x, float y) {
		this.velocityVec.set(x, y);
	}

	public void addVelocity(float x, float y) {
		this.velocityVec.add(x, y);
	}

	public Vector2 getPosition() {
		return positionVec;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	/**
	 * Set the current position of the Entity
	 * 
	 * @param pos
	 *            the new position
	 * 
	 * @see #setPosition(float, float)
	 */
	public void setPosition(Vector2 pos) {
		setPosition(pos.x, pos.y);
	}

	/**
	 * Set the current position of the Entity to (x, y)
	 * 
	 * @param x
	 *            the new x position
	 * @param y
	 *            the new y position
	 */
	public void setPosition(float x, float y) {
		this.positionVec.set(x, y);
		hitbox.setPosition(positionVec);
	}

	/**
	 * Set the maximum speed of the entity
	 * 
	 * @param speed
	 *            the maximum speed
	 * @return This entity object for easy building
	 */
	public Entity setMaxSpeed(float speed) {
		this.maximumSpeed = speed;
		return this;
	}

	public float getMaxSpeed() {
		return maximumSpeed;
	}

}
