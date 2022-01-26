package tk.shardsoftware.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import tk.shardsoftware.TileType;
import tk.shardsoftware.WorldMap;
import tk.shardsoftware.util.ResourceUtil;
import tk.shardsoftware.entity.Entity;
import tk.shardsoftware.World;
/**
 * Represents the physical location of a college on a map.
 * College is implemented as an extension of Entity which does not move. (i.e physics are never applied to it)
 * Also contains static methods which facilitate adding a number of colleges to the map.
 * @author Hector Woods
 */
public class College extends Entity {
    public String collegeName;
    public String collegeTextureName = "textures/ui/expand-map-button.png";
    public static ArrayList<College> Colleges;
    public static ArrayList<String> availableCollegeNames = new ArrayList<>(
        Arrays.asList("James","Constantine","Alcuin","Anne Lister","David Kato", "Derwent", "Goodricke","Halifax","Langwith","Vanbrugh","Wentworth"));


    /**
     * Generates a random college name from availableCollegeNames, and removes it from the list so that each college name is unique.
     */
    public String getRandomCollegeName(){
        if(availableCollegeNames.size() <= 0){
            return "ERROR_NO_AVAILABLE_COLLEGE_NAMES";
        }
        int i = new Random().nextInt(availableCollegeNames.size());
        return availableCollegeNames.remove(i);
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
     * @param worldObj A valid worldObj that the college will be located in
     * @param numColleges The number of colleges in the world
     * @param collegeMinDist Minimum distance between each college
     */
    public static void generateColleges(World worldObj, int numColleges, float collegeMinDist){
        WorldMap map = worldObj.worldMap;
        Function<Vector2, Boolean> collegePositionConds = vector2 -> {
            int x = (int)vector2.x;
            int y = (int)vector2.y;

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

            //Check that at least 25% of the tiles around the college are water, so that the player is able to reach the college
            float numWater = 0;
            float numLand = 0;
            for (int i = x - 20; i < x + 20; i++) {
                for (int j = y - 20; j < y + 20; j++) {
                    TileType tile = worldObj.worldMap.getTile(i, j);
                    if (tile == TileType.WATER_DEEP || tile == TileType.WATER_SHALLOW) {
                        numWater = numWater + 1;
                    }else{
                        numLand = numLand + 1;
                    }
                }
            }
            if(numWater / numLand < 0.25){
                return false;
            }
            //Check that the college is not too close to other colleges in the world (as determined by collegeMinDist)
            for(College c : Colleges){
                Vector2 pos = c.getPosition();
                float dist = pos.dst(vector2); //Compare distance from the college to the point we want to place on
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
                College c = new College(worldObj,collegePos.x, collegePos.y, map.tile_size,map.tile_size); //This is added to Colleges array by the constructor automatically
            }
        }
    }

}
