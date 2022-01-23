/**
 * 
 */
package tk.shardsoftware.entity;

/**
 * @author James Burnell
 */
public interface IRepairable extends IDamageable {

	/** Repair the object by the given amount */
	public void repair(float repairAmount);

}
