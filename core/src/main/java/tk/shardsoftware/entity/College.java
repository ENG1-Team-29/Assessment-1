package tk.shardsoftware.entity;

import tk.shardsoftware.World;

/**
 * Represents the physical location of a college on a map. College is
 * implemented as an extension of Entity which does not move. (i.e physics are
 * never applied to it) Also contains static methods which facilitate adding a
 * number of colleges to the map.
 * 
 * @author Hector Woods
 */
public class College extends Entity implements IDamageable, IRepairable {

	public String collegeName;
	public String collegeTextureName = "textures/entity/college.png";
	public float maxHealth = 100;
	public float health = maxHealth;

	/**
	 * Reduces the health of the College by 'dmgAmount'. See IDamageable
	 * 
	 * @param dmgAmount the amount of damage for the college to take
	 */
	public void damage(float dmgAmount) {
		this.health = this.health - dmgAmount;
		if (this.health <= 0) {
			this.remove = true; // flag for removal by entity handler
		}
	}

	/**
	 * Increases the health of the College by 'repairAmount'. See IRepairable
	 * 
	 * @param repairAmount the amount of health for the College's health to be
	 *                     increased by
	 */
	public void repair(float repairAmount) {
		this.health = this.health + repairAmount;
		if (this.health > this.maxHealth) {
			this.health = this.maxHealth;
		}
	}

	/**
	 * Gets the current health of the College. See IDamageable
	 * 
	 * @return current health of the College.
	 */
	public float getHealth() {
		return this.health;
	}

	/**
	 * Gets the current maxHealth of the College. See IDamageable
	 * 
	 * @return current maxHealth of the College.
	 */
	public float getMaxHealth() {
		return this.maxHealth;
	}

	/**
	 * Get the name of the College.
	 * 
	 * @return the name of the college.
	 */
	public String getName() {
		return this.collegeName;
	}

	/**
	 * Constructor for College
	 * 
	 * @param worldObj A valid worldObj that the college will be located in
	 * @param x        The x-position of the entity on creation
	 * @param y        The y-position of the entity on creation
	 * @param w        The width of the entity in pixels
	 * @param h        The height of the entity in pixels
	 */
	public College(World worldObj, String collegeName, float x, float y, int w, int h) {
		super(worldObj, x, y, w, h);
		this.setTexture(collegeTextureName);
		this.collegeName = collegeName;
	}

	public void onRemove() {
		if (health <= 0) worldObj.onCollegeDestroyed(this);
	}

}
