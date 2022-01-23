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

	public EntityAIShip(World world, EntityShip player) {
		super(world);
		this.aiState = AIState.IDLE;
		this.player = player;
	}

	public void CirclePlayer(float delta, float distToPlayer) {
		float goalAngle = -999;
		Vector2 playerPos = player.getPosition();
		// playerPos = new Vector2(playerPos.x,playerPos.y+50);
		Vector2 shipPos = this.getPosition();
		Vector2 directionVector = new Vector2(playerPos.x - shipPos.x, playerPos.y - shipPos.y);
		goalAngle = directionVector.angleDeg();// Convert to degrees and round down
		super.rotateTowardsGoal(goalAngle, delta);
	}

	public void update(float delta) {
		super.update(delta);

		Vector2 playerPos = player.getPosition();
		Vector2 shipPos = this.getPosition();
		float distToPlayer = shipPos.dst(playerPos);
		if (distToPlayer > 750 || distToPlayer < 50) { // If the player is too far away, do nothing
			aiState = AIState.IDLE;
		} else {
			aiState = AIState.CIRCLE_PLAYER;
		}
		if (aiState == AIState.CIRCLE_PLAYER) {
			CirclePlayer(delta, distToPlayer);
		}

	}

}
