package tk.shardsoftware.util;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Facilitates drawing UI bars to the screen, e.g a health-bar.
 * @author Hector Woods, James Burnell
 */
public class Bar {
    /**
     * Draw a bar, specifying the background and foreground colours and the animation time.
     * @param batch The SpriteBatch to draw with
     * @param shapeRenderer The ShapeRenderer to render rectLine with
     * @param start The start of the bar. Vector2.
     * @param end The end of the bar. Vector2.
     * @param lerpAlpha The amount of time to animate the bar
     * @param backgroundColor The background colour
     * @param foreGroundColor The colour of the bar
     */
    public static void DrawBar(SpriteBatch batch, ShapeRenderer shapeRenderer, Vector2 start, Vector2 end, float lerpAlpha, Color backgroundColor, Color foreGroundColor){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(backgroundColor);
        shapeRenderer.rectLine(start, end, 3);
        start.x += 1;
        end.x -= 2;
        shapeRenderer.setColor(foreGroundColor);
        shapeRenderer.rectLine(start,
                end.lerp(start, lerpAlpha), 1);
        shapeRenderer.end();
    }
    /**
     * Draw a bar, specifying the animation time.
     * @param batch The SpriteBatch to draw with
     * @param shapeRenderer The ShapeRenderer to render rectLine with
     * @param start The start of the bar. Vector2.
     * @param end The end of the bar. Vector2.
     * @param lerpAlpha The amount of time to animate the bar
     */
    public static void DrawBar(SpriteBatch batch, ShapeRenderer shapeRenderer, Vector2 start, Vector2 end, float lerpAlpha){
        DrawBar(batch,shapeRenderer,start,end,lerpAlpha,Color.BLACK, Color.GREEN);
    }
    /**
     * Draw a bar.
     * @param batch The SpriteBatch to draw with
     * @param shapeRenderer The ShapeRenderer to render rectLine with
     * @param start The start of the bar. Vector2.
     * @param end The end of the bar. Vector2.
     */
    public static void DrawBar(SpriteBatch batch, ShapeRenderer shapeRenderer, Vector2 start, Vector2 end){
        DrawBar(batch,shapeRenderer,start,end,0,Color.BLACK, Color.GREEN);
    }
}
