package tk.shardsoftware.util;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Facilitates drawing UI bars to the screen, e.g a health-bar.
 * @author Hector Woods
 * @author James Burnell
 */
public class Bar {
    /**
     * Draw a bar, specifying the background and foreground colours and the proportion of the bar to fill.
     * @param batch The SpriteBatch to draw with
     * @param shapeRenderer The ShapeRenderer to render rectLine with
     * @param start The start of the bar. Vector2.
     * @param end The end of the bar. Vector2.
     * @param p The proportion of the bar to fill
     * @param backgroundColor The background colour
     * @param foreGroundColor The colour of the bar
     */
    public static void DrawBar(SpriteBatch batch, ShapeRenderer shapeRenderer, Vector2 start, Vector2 end, float p, Color backgroundColor, Color foreGroundColor){
        shapeRenderer.setColor(backgroundColor);
        shapeRenderer.rectLine(start, end, 3);
        start.x += 1;
        end.x -= 2;
        shapeRenderer.setColor(foreGroundColor);
        shapeRenderer.rectLine(start,
                end.lerp(start, p), 1);
    }
    /**
     * Draw a bar, specifying the proportion of the bar to be filled.
     * @param batch The SpriteBatch to draw with
     * @param shapeRenderer The ShapeRenderer to render rectLine with
     * @param start The start of the bar. Vector2.
     * @param end The end of the bar. Vector2.
     * @param p The proportion of the bar to fill
     */
    public static void DrawBar(SpriteBatch batch, ShapeRenderer shapeRenderer, Vector2 start, Vector2 end, float p){
        DrawBar(batch,shapeRenderer,start,end,p,Color.BLACK, Color.GREEN);
    }
    /**
     * Draw a bar, with the whole bar being filled.
     * @param batch The SpriteBatch to draw with
     * @param shapeRenderer The ShapeRenderer to render rectLine with
     * @param start The start of the bar. Vector2.
     * @param end The end of the bar. Vector2.
     */
    public static void DrawBar(SpriteBatch batch, ShapeRenderer shapeRenderer, Vector2 start, Vector2 end){
        DrawBar(batch,shapeRenderer,start,end,0,Color.BLACK, Color.GREEN);
    }

    /**
     * Draw a bar, specifying the background/foreground colours and filling the whole bar
     * @param batch The SpriteBatch to draw with
     * @param shapeRenderer The ShapeRenderer to render rectLine with
     * @param start The start of the bar. Vector2.
     * @param end The end of the bar. Vector2.
     */
    public static void DrawBar(SpriteBatch batch, ShapeRenderer shapeRenderer, Vector2 start, Vector2 end,Color backgroundColor, Color foreGroundColor){
        DrawBar(batch,shapeRenderer,start,end,0,backgroundColor, foreGroundColor);
    }
}
