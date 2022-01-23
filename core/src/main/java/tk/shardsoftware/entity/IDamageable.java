package tk.shardsoftware.entity;

import com.badlogic.gdx.math.Rectangle;

/**
 * @author James Burnell
 */
public interface IDamageable {

	/** Returns the maximum amount of health the object has */
	public float getMaxHealth();

	/** Returns the current health of the object */
	public float getHealth();

	/** Damage the object by a given amount */
	public void damage(float dmgAmount);

	/** The hitbox of the object */
	public Rectangle getHitbox();

}
