package tk.shardsoftware.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.World;
import tk.shardsoftware.util.ResourceUtil;

/**
 * Represents the physical location of a college on a map. College is
 * implemented as an extension of Entity which does not move. (i.e physics are
 * never applied to it) Also contains static methods which facilitate adding a
 * number of colleges to the map.
 * 
 * @author Hector Woods
 */
public class College extends Entity implements IRepairable, ICannonCarrier {

	public EntityShip player;
	public String collegeName;
	public String collegeTextureName = "textures/entity/college.png";
	public Sound hitSound = ResourceUtil.getSound("audio/entity/college-hit.mp3");
	public Sound cannonSfx = ResourceUtil.getSound("audio/entity/cannon.mp3");
	public float maxHealth = 100;
	public float health = maxHealth;
	public float timeUntilFire = 0f;
	public float reloadTime = 3f;
	public float fireDistance = 350f;
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

	public boolean fireCannons(){
		// Do not fire if still reloading
		if (timeUntilFire > 0) return false;
		//Do not fire if too far away from the player
		Vector2 center = getCenterPoint();
		Vector2 playerPos = player.getPosition();
		float distFromPlayer = center.dst(playerPos);
		if(distFromPlayer > fireDistance){
			return false;
		}

		// Reload
		timeUntilFire += reloadTime;
		// Play sfx
		cannonSfx.play();


		for(int i = -2; i < 2; i++){
			for(int j = -2; j < 2; j++){
				if(!(i==0 &&j==0)){ //if i and j ==0 then the cannonball doesn't move
					Vector2 dirVec = new Vector2(i,j);

					float xPos = center.x + dirVec.x;
					float yPos = center.y + dirVec.y;

					EntityCannonball cb = new EntityCannonball(worldObj, xPos, yPos, dirVec, this);
					worldObj.addEntity(cb);
				}
			}
		}

		return true;
	}

	@Override
	public float getReloadTime() {
		return reloadTime;
	}

	@Override
	public float getReloadProgress() {
		return timeUntilFire;
	}

	@Override
	public float getCannonDamage() {
		return 10;
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

	public void update(float delta){
		timeUntilFire -= delta;
		timeUntilFire = timeUntilFire <= 0 ? 0 : timeUntilFire;
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
	public College(World worldObj, String collegeName, float x, float y, int w, int h, EntityShip player) {
		super(worldObj, x, y, w, h);
		this.setTexture(collegeTextureName);
		this.collegeName = collegeName;
		this.player = player;
	}

	public void onRemove() {
		if (health <= 0) worldObj.onCollegeDestroyed(this);
	}

}
