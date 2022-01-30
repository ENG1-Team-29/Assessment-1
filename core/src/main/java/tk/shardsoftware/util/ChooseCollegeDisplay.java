package tk.shardsoftware.util;

import static tk.shardsoftware.util.ResourceUtil.miniMapFont;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import java.util.List;

import tk.shardsoftware.World;
import tk.shardsoftware.entity.College;
import tk.shardsoftware.util.ResourceUtil;

public class ChooseCollegeDisplay implements Disposable {

    public BitmapFont font = ResourceUtil.font;
    public List<String> collegeNames;
    public Texture backgroundTexture = ResourceUtil.getTexture("")

    public void ChooseCollegeDisplay(int width, int height, College[] colleges){
        for(College c : colleges){
         collegeNames.add(c.getName());
        }
    }

    public void DrawCollegeDisplay(SpriteBatch batch){
        font.draw(batch,"hello :3",100,100);
        batch.draw
    }

    public void dispose() {

    }
}
