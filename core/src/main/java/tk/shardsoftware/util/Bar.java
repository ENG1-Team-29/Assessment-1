package tk.shardsoftware.util;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Bar {

    public static void DrawBar(SpriteBatch batch, ShapeRenderer shapeRenderer, Vector2 start, Vector2 end, float lerpAlpha){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rectLine(start, end, 3);
        start.x += 1;
        end.x -= 2;
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rectLine(start,
                end.lerp(start, lerpAlpha), 1);
        shapeRenderer.end();
    }
}
