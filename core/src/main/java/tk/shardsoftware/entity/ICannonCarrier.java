/**
 * 
 */
package tk.shardsoftware.entity;

/**
 * @author James Burnell
 */
public interface ICannonCarrier {

	/**
	 * Fires all cannons.
	 * 
	 * @return {@code true} if spawned cannonballs, {@code false} if still reloading
	 */
	public boolean fireCannons();

	/** Returns how long it takes for the carrier to reload */
	public float getReloadTime();

	/**
	 * Returns how many seconds are remaining before the carrier has reloaded. 0
	 * means it has reloaded and is ready to fire.
	 */
	public float getReloadProgress();

}
