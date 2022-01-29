package tk.shardsoftware.entity;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import tk.shardsoftware.TileType;
import tk.shardsoftware.WorldMap;
import tk.shardsoftware.World;
/**
 * Represents the physical location of a college on a map.
 * College is implemented as an extension of Entity which does not move. (i.e physics are never applied to it)
 * Also contains static methods which facilitate adding a number of colleges to the map.
 * @author Hector Woods
 */
public class College extends Entity implements IDamageable, IRepairable {
    public String collegeName;
    public String collegeTextureName = "textures/entity/college.png";
    public static ArrayList<College> Colleges = new ArrayList<College>();
    public static ArrayList<String> availableCollegeNames;
    public float maxHealth = 100;
    public float health = maxHealth;

    /**
     * Reduces the health of the College by 'dmgAmount'. See IDamageable
     * @param dmgAmount the amount of damage for the college to take
     */
    public void damage(float dmgAmount){
        this.health = this.health - dmgAmount;
        if(this.health <= 0){
            this.remove = true; //flag for removal by entity handler
        }
    }

    /**
     * Increases the health of the College by 'repairAmount'. See IRepairable
     * @param repairAmount the amount of health for the College's health to be increased by
     */
    public void repair(float repairAmount){
        this.health = this.health + repairAmount;
        if(this.health > this.maxHealth){
            this.health = this.maxHealth;
        }
    }


    /**
     * Gets the current health of the College. See IDamageable
     * @return current health of the College.
     */
    public float getHealth(){
        return this.health;
    }

    /**
     * Gets the current maxHealth of the College. See IDamageable
     * @return current maxHealth of the College.
     */
    public float getMaxHealth(){
        return this.maxHealth;
    }

    /**
     * Generates a random college name from availableCollegeNames, and removes it from the list so that each college name is unique.
     * @return A college name (string) from availableCollegeNames
     */
    public String getRandomCollegeName(){
        if(availableCollegeNames.size() <= 0){
            return "ERROR_NO_AVAILABLE_COLLEGE_NAMES";
        }
        int i = new Random().nextInt(availableCollegeNames.size());
        return availableCollegeNames.remove(i);
    }

    /**
     * Gets a college of the specified name.
     * @param name Name of the college
     * @return college with the specified name if it exists, otherwise null.
     */
    public static College getCollegeWithName(String name){
        for(College c : Colleges){
            if(c.getName() == name){
                return c;
            }
        }
        return null;
    }

    /**
     * Get the name of the College.
     * @return the name of the college.
     */
    public String getName(){
        return this.collegeName;
    }

    /**
     * Constructor for College
     * @param worldObj A valid worldObj that the college  will be located in
     * @param x The x-position of the entity on creation
     * @param y The y-position of the entity on creation
     * @param w The width of the entity in pixels
     * @param h The height of the entity in pixels
     */
    public College(World worldObj, float x, float y, int w, int h){
        super(worldObj,x,y,w,h);
        this.setTexture(collegeTextureName);
        this.collegeName = getRandomCollegeName();
        Colleges.add(this); //add this to the Colleges ArrayList so we can keep track of it
    }

    /**
     * Static method that generates numColleges College, provided that a valid position exists
     * @param worldObj A valid worldObj that the colleges will be located in
     * @param numColleges The number of colleges in the world
     * @param collegeMinDist Minimum distance between each college (magnitude of the distance vector between the two)
     */
    public static void generateColleges(World worldObj, int numColleges, float collegeMinDist){
        availableCollegeNames = new ArrayList<>(
                Arrays.asList("James","Constantine","Alcuin","Anne Lister","David Kato", "Derwent", "Goodricke","Halifax","Langwith","Vanbrugh","Wentworth"));
        Colleges = new ArrayList<College>();
        WorldMap map = worldObj.worldMap;
        Function<Vector2, Boolean> collegePositionConds = vector2 -> {
            int x = (int)vector2.x;
            int y = (int)vector2.y;

            //Check that the college is not too close to the edges of the map
            if(x < 50 || y < 50 || x > map.width-50 || y > map.height-50){
                return false;
            }

            //Check that the college is located on land
            TileType collegeTile = map.getTile(x,y);
            if(collegeTile == TileType.WATER_DEEP || collegeTile == TileType.WATER_SHALLOW){
                return false;
            }
            //Check that the college is directly adjacent to some water
            if(!(map.getTile(x+1,y) == TileType.WATER_SHALLOW) && !(map.getTile(x-1,y) == TileType.WATER_SHALLOW)
                    && !(map.getTile(x,y+1) == TileType.WATER_SHALLOW) && !(map.getTile(x,y-1) == TileType.WATER_SHALLOW)){
                return false;
            }
            /////I commented out the below check because on testing I found it was unnecessary.////////
                    //Check that at least 25% of the tiles around the college are water, so that the player is able to reach the college
                   // float numWater = 0;
                   // float numLand = 0;
                   // for (int i = x - 20; i < x + 20; i++) {
                    //    for (int j = y - 20; j < y + 20; j++) {
                    //        TileType tile = worldObj.worldMap.getTile(i, j);
                      //      if (tile == TileType.WATER_DEEP || tile == TileType.WATER_SHALLOW) {
                     //           numWater = numWater + 1;
                     //       }else{
                      //          numLand = numLand + 1;
                      //      }
                    //    }
                   // }
                   // if(numWater / numLand < 0.25){
                      //  return false;
                    //}
            //////
            //Check that the college is not too close to other colleges in the world (as determined by collegeMinDist)
            for(College c : Colleges){
                Vector2 pos = c.getPosition();
                Vector2 collegePosTiles = new Vector2(pos.x/map.tile_size,pos.y/ map.tile_size);
                float dist = vector2.dst(collegePosTiles); //Compare distance from the college to the point we want to place on
                if(dist < collegeMinDist){
                    return false;
                }
            }

            //If all the above is true, this is a valid tile for a college to be placed
            return true;
        };
        for(int i = 0; i<numColleges; i++){
            Vector2 collegePos = worldObj.worldMap.SearchMap(collegePositionConds);
            if(collegePos != null){
                College c = new College(worldObj,collegePos.x * worldObj.worldMap.tile_size,
                        collegePos.y * worldObj.worldMap.tile_size, map.tile_size*6,map.tile_size*6); //This is added to Colleges array by the constructor automatically
            }
        }
    }

}
