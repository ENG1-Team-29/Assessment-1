package tk.shardsoftware.entity;

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

}
