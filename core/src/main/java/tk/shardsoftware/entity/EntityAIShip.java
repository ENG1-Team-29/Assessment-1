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
	
	public void CircleCollege(float delta) {
		
		Vector2 tilePos = GetTilePosition(this.getPosition());
		Vector2 playerTilePos = GetTilePosition(player.getPosition());
		
		System.out.println(CalculateDiagonalDistance(tilePos, playerTilePos));
		
		//System.out.println(worldObj.worldMap.getTile((int)tilePos.x, (int)tilePos.y));
		//System.out.println(worldObj.worldMap.getTile((int)playerTilePos.x, (int)playerTilePos.y));
		
		
		
		float goalAngle = -999;
		Vector2 playerPos = player.getPosition();
		// playerPos = new Vector2(playerPos.x,playerPos.y+50);
		Vector2 shipPos = this.getPosition();
		Vector2 directionVector = new Vector2(playerPos.x - shipPos.x, playerPos.y - shipPos.y);
		goalAngle = directionVector.angleDeg();// Convert to degrees and round down
		super.rotateTowardsGoal(goalAngle, delta);
		
		
		
		
		//GetTilePosition(this);
		//GetTilePosition(college);
		
		//AStar(Tilemap, thistilepos, collegetilepos);
		
		//super.rotateTowardsGoal(goalAngle, delta);
		
	}
	
	public int CalculateDiagonalDistance(Vector2 currentPos, Vector2 goalPos) {
		
		
		
		float dx = Math.abs(currentPos.x - goalPos.x);
		float dy =  Math.abs(currentPos.y - goalPos.y);
				 
		int h = (int) ((dx + dy) + (Math.sqrt(2) - 2) * Math.min(dx, dy));
		
		return h;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Vector2 GetTilePosition(Vector2 position) {
		
		return new Vector2(position.x/worldObj.WORLD_TILE_SIZE, position.y/worldObj.WORLD_TILE_SIZE);
		
	}

	public void update(float delta) {
		super.update(delta);

		Vector2 playerPos = player.getPosition();
		Vector2 shipPos = this.getPosition();
		float distToPlayer = shipPos.dst(playerPos);
		/*if (distToPlayer > 750 || distToPlayer < 50) { // If the player is too far away, do nothing
			aiState = AIState.CIRCLE_COLLEGE;
		} else {
			aiState = AIState.CIRCLE_PLAYER;
		}
		if (aiState == AIState.CIRCLE_PLAYER) {
			CirclePlayer(delta, distToPlayer);
		}*/
		
		CircleCollege(delta);

	}

}
