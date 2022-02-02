package tk.shardsoftware.entity;

import java.util.Stack;
import java.util.HashMap;
import java.util.ArrayList;

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
		
		//System.out.println(CalculateDiagonalDistance(tilePos, playerTilePos));
		//System.out.println(isInRange(20, 300));
		//System.out.println(worldObj.worldMap.getTile((int)tilePos.x, (int)tilePos.y));
		//System.out.println(worldObj.worldMap.getTile((int)playerTilePos.x, (int)playerTilePos.y));
		
		
		ArrayList<Vector2> path = A_Star(tilePos, playerTilePos);
		System.out.println("The path is: ");
		System.out.println(path);
		
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
	
	public boolean isUnblocked(int row, int col) {
		
		return worldObj.worldMap.getTile(row, col).isSolid();
		
	}
	
	public boolean isInRange(Vector2 cell) {
		
		if(cell.x > 0 && cell.x < 500 && cell.y > 0 && cell.y < 300) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public boolean isDestination(int row, int col, Vector2 dest)
	{
	    if (row == dest.x && col == dest.y)
	        return true;
	    else
	        return false;
	}
	
	public float CalculateDiagonalDistance(Vector2 currentPos, Vector2 goalPos) {
		
		
		
		float dx = Math.abs(currentPos.x - goalPos.x);
		float dy =  Math.abs(currentPos.y - goalPos.y);
				 
		float h = (float) ((dx + dy) + (Math.sqrt(2) - 2) * Math.min(dx, dy));
		
		return h;
		
	}
	
	public float CalculateEuclideanDistance(Vector2 currentPos, Vector2 goalPos) {
		
		return  (float) Math.sqrt ( Math.pow(currentPos.x - goalPos.x, 2) + Math.pow(currentPos.y - goalPos.y, 2) );
		
	}
	
	public ArrayList<Vector2> reconstructPath(HashMap<Vector2, Vector2> cameFrom, Vector2 current)
	{
		//Given the 'cameFrom' HashMap and the cell to find a path to, the method returns the path to that cell within an ArrayList
		
		ArrayList<Vector2> totalPath = new ArrayList<>();
		
		while(cameFrom.get(current) != null) {
			
			current = cameFrom.get(current);
			totalPath.add(0, current);
			
		}
		
		return totalPath;
		
	}	
	
	public ArrayList<Vector2> A_Star(Vector2 start, Vector2 end) {
		
		//Large value to as placeholder for unknown 
		float bigValue = 9999999f;
		
		//Create ArrayList to hold the set of cells currently known of
		ArrayList<Vector2> openSet = new ArrayList<>();
		openSet.add(start);
		
		//HashMap connecting a cell to a neighbouring cell with  the shortest path to the start 
		HashMap<Vector2, Vector2> cameFrom = new HashMap<Vector2, Vector2>();
		//Hashmaps holding the gscore and fscore for every cell respectively
		HashMap<Vector2, Float> gscore = new HashMap<Vector2, Float>();
		HashMap<Vector2, Float> fscore = new HashMap<Vector2, Float>();
		
		//initilise all the values to infinity
		
		for (int i = 0; i < 500; i++) {
			
			for (int j = 0; j < 300; j++) {
				
				gscore.put(new Vector2(i, j), bigValue);
				fscore.put(new Vector2(i, j), bigValue);
				
			}
			
		}
		//inititialise the gscore to zero at the start cell
		//inintialise the fscore to distance from start to end as the crow flies
		gscore.put(start, 0f);
		fscore.put(start, CalculateDiagonalDistance(start, end));
		
		while (openSet.size() != 0) {
			
			Vector2 current = null;
			
			float lowestValue = bigValue;
			
			for(Vector2 i : openSet) {
					
					if(lowestValue > fscore.get(i)) {
						
						current = i;
						lowestValue = fscore.get(current);
						//set the current working cell to the one with the lowest fscore in openset
						
					}
					
			}
			
			if (current == end) {
				//if at the end return the path found
		            return reconstructPath(cameFrom, current);
			}
			
			openSet.remove(openSet.indexOf(current));
			 
			 for(int i = (int) (current.x-1); i < current.x+2; i++) {
				 
				 for(int j = (int) (current.y-1); j< current.y+2; j++) {
					 
					 Vector2 neighbour = new Vector2(i, j);
					 
					 System.out.println(neighbour);
					 
					 if(neighbour == current || !isInRange(neighbour)) {
						 
						 System.out.println("Neighbour is current or out of range or blocked");
						 System.out.println("Is neighbour in range?: " + isInRange(neighbour));
						 
					 } else {
						 
						 
						 float tentative_gScore = gscore.get(current) + CalculateEuclideanDistance(current, new Vector2(i, j)) ;
						 
						 if(tentative_gScore < gscore.get(neighbour)) {
							 
							 //Best Route to neighbour so far
							 System.out.println("Best Route to neighbour so far");
							 System.out.println("tentgscore: ");
							 System.out.println(tentative_gScore);
							 cameFrom.put(neighbour, current);
							 gscore.put(neighbour, tentative_gScore);
							 fscore.put(neighbour, tentative_gScore + CalculateEuclideanDistance(neighbour, end));
							 if(!openSet.contains(neighbour)) {
								 openSet.add(neighbour);
				
							 }
						 }
						 
					 }
					  
				 }
				 
			 }
			
		}
		
		return new ArrayList<Vector2>();
		
	}
	
	
	public Vector2 GetTilePosition(Vector2 position) {
		
		return new Vector2((float)Math.floor(position.x/worldObj.WORLD_TILE_SIZE), (float)Math.floor(position.y/worldObj.WORLD_TILE_SIZE));
		
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
