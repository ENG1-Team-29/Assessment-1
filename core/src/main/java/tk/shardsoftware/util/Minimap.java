package tk.shardsoftware.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

import tk.shardsoftware.TileType;
import tk.shardsoftware.World;
import tk.shardsoftware.WorldMap;
import tk.shardsoftware.util.ResourceUtil;

/**
 * @author Hector Woods
 * Draws a Minimap to the screen to help the player navigate.
 */
public class Minimap {
    World world;

    Texture miniMapBackground;
    Texture playerTile;

    public Minimap(World world){
        this.world = world;
        miniMapBackground = ResourceUtil.getTexture("textures/tiles/minimap_background.png");
        playerTile = ResourceUtil.getTexture("textures/tiles/player-tile.png");
    }

    public void DrawMap(SpriteBatch batch, int width, int height, int x, int y, Vector2 playerPos){
        batch.draw(miniMapBackground, x, y, width, height);

        HashMap<Vector2, TileType> tileMap = world.worldMap.tileMap;

        int playerTileX = (int)playerPos.x / world.worldMap.tile_size;
        int playerTileY = (int)playerPos.y / world.worldMap.tile_size;

        int startX = playerTileX - width/2;
        int startY = playerTileY - height/2;

        int borderSize = 6; //We don't want to draw over the miniMap borders!

        int miniMapOriginX = x;
        int miniMapOriginY = y;

        for(int i = startX+borderSize; i < startX + width-borderSize; i++){
            for(int j = startY+borderSize; j < startY + height-borderSize; j++){
                Texture tex = world.worldMap.getTile(i,j).getTex();
                batch.draw(tex,miniMapOriginX+i-startX, miniMapOriginY+j-startY,1,1);
            }
        }



        ShapeRenderer shapes = new ShapeRenderer();

        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.YELLOW);

        shapes.circle(x+width/2-borderSize/2,y+width/2-borderSize/2, 5);

        shapes.end();
        //batch.draw(playerTile,x+width/2-borderSize/2,y+width/2-borderSize/2,5,5);

        WorldMap map = this.world.worldMap;
        PerlinNoiseGenerator generator = map.perlin;

    }
}
