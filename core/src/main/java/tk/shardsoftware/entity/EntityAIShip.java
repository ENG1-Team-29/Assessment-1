package tk.shardsoftware.entity;

import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.World;

/**
 * A simple AI ship that circles around the player. It can't fire yet as this was not a requirement listed in the assessment
 * @author Hector Woods
 * */
public class EntityAIShip extends EntityShip {
    public String AIState;
    EntityShip player;

    public EntityAIShip(World world, EntityShip player){
        super(world);
        this.AIState = "idle";
        this.player = player;
    }

    public void CirclePlayer(float delta, float distToPlayer){
        int goalAngle = -999;
        Vector2 playerPos = player.getPosition();
       // playerPos = new Vector2(playerPos.x,playerPos.y+50);
        Vector2 shipPos = this.getPosition();
        Vector2 directionVector = new Vector2 (playerPos.x-shipPos.x,playerPos.y-shipPos.y).nor();
        double angle = Math.atan(directionVector.y/directionVector.x); // tan^-1(vY/vX)
        goalAngle = (int)Math.toDegrees(angle); //Convert to degrees and round down
        if(directionVector.x < 0){
            goalAngle = goalAngle-180;
        }
        super.rotateTowardsGoal(goalAngle,delta);
    }

    public void update(float delta){
        super.update(delta);

        Vector2 playerPos = player.getPosition();
        Vector2 shipPos = this.getPosition();
        float distToPlayer = shipPos.dst(playerPos);
        if(distToPlayer > 750){ //If the player is too far away, do nothing
            AIState = "idle";
        }else{
            AIState = "CirclePlayer";
        }
        if(AIState == "CirclePlayer"){
            CirclePlayer(delta, distToPlayer);
        }

    }

}
