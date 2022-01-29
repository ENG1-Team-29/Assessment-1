package tk.shardsoftware.entity;

import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.World;

/**
 * A simple AI ship that circles around the player. It can't fire yet as this
 * was not a requirement listed in the assessment
 * 
 * @author Hector Woods
 */
public class EntityAIShip extends EntityShip {
	public AIState aiState;
	EntityShip player;
	int chaseDistance = 500; //the range at which the ship will start chasing the player
	int minDistance = 50; //min distance the entity maintains from the player

	/**
	 * Constructor for EntityAIShip.
	 * @param world world the Entity is part of
	 * @param player the ship controlled by the player
	 */
	public EntityAIShip(World world, EntityShip player) {
		super(world);
		this.aiState = AIState.IDLE;
		this.player = player;
	}

	/**
	 * Constructor for EntityAIShip, specifying unique chaseDistance and minDistance
	 * @param world
	 * @param player
	 * @param chaseDistance
	 * @param minDistance
	 */
	public EntityAIShip(World world, EntityShip player, int chaseDistance, int minDistance){
		super(world);
		this.aiState = AIState.IDLE;
		this.player = player;
		this.chaseDistance = chaseDistance;
		this.minDistance = minDistance;
	}


	/**
	 * Makes the ship follow the player.
	 * @param delta time since last frame
	 */
	public void FollowPlayer(float delta) {
		float goalAngle = -999;
		Vector2 playerPos = player.getPosition();
		// playerPos = new Vector2(playerPos.x,playerPos.y+50);
		Vector2 shipPos = this.getPosition();
		Vector2 directionVector = new Vector2(playerPos.x - shipPos.x, playerPos.y - shipPos.y);
		goalAngle = directionVector.angleDeg();// Convert to degrees and round down
		super.rotateTowardsGoal(goalAngle, delta);
	}

	/**
	 * Called once a frame for each entity.
	 * @param delta time since last frame
	 */
	public void update(float delta) {
		super.update(delta);

		Vector2 playerPos = player.getPosition();
		Vector2 shipPos = this.getPosition();
		float distToPlayer = shipPos.dst(playerPos);
		if (distToPlayer > chaseDistance || distToPlayer < minDistance) { // If the player is too far away, or too close, do nothing
			aiState = AIState.IDLE;
			//very slowly come to a stop
			Vector2 currentVelocity = this.getVelocity();
			this.setVelocity(new Vector2((float)(currentVelocity.x*0.99), (float)((currentVelocity.y*0.99))));
		} else {
			aiState = AIState.FOLLOW_PLAYER;
		}
		if (aiState == AIState.FOLLOW_PLAYER) {
			FollowPlayer(delta);
		}

	}

}
