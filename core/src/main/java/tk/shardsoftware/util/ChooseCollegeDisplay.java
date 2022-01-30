package tk.shardsoftware.util;

import static tk.shardsoftware.util.ResourceUtil.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tk.shardsoftware.World;
import tk.shardsoftware.entity.College;
import tk.shardsoftware.entity.Entity;
import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.util.ResourceUtil;

public class ChooseCollegeDisplay extends Minimap {
    List<College> collegeList;
    private Texture collegeTexture = ResourceUtil.getTexture("textures/ui/college-map-icon.png");
    List<Button> buttons;


    public void RemoveActors(){
        Iterator<Button> iterButtons = buttons.iterator();
        while(iterButtons.hasNext()) {
            Button b = iterButtons.next();
            b.remove();

        }
    }

    private Vector2 getCollegePosition(College c){
        Vector2 collegePos = c.getPosition();
        //Get the amount by which the image has been rescaled
        int originalWidth = wholeMap.getWidth();
        int originalHeight = wholeMap.getHeight();
        float widthMulti = width/originalWidth;
        float heightMulti = height/originalHeight;

        //Get the tile the college is located on
        int tileX = (int)collegePos.x/this.worldObj.worldMap.tile_size;
        int tileY = (int)collegePos.y/this.worldObj.worldMap.tile_size;

        //Convert the tile to a pixel on the image (on the original image it was 1-1, so we multiply by multipliers to get the position on the new image)
        int pixelX = (int)(tileX * widthMulti);
        int pixelY = (int)(tileY * heightMulti);
        return new Vector2(pixelX,pixelY);
    }


    public ChooseCollegeDisplay(World world, float x, float y, int width, int height, SpriteBatch batch, Stage stage, List<College> collegeList, GameScreen gS) {
        //create minimap
        super(world,x,y,width,height,batch,stage);
        this.collegeList = collegeList;
        buttons = new ArrayList<Button>();

        //create buttons
        Drawable buttonBackground = new TextureRegionDrawable(
                new TextureRegion(ResourceUtil.getTexture("textures/ui/college-map-icon.png")));

        Iterator<College> iter = collegeList.iterator();
        while(iter.hasNext()) {
            College c = iter.next();
            //Get college info
            Vector2 collegePos = getCollegePosition(c);
            String cName = c.getName();
            Button button = new ImageButton(buttonBackground);
            stage.addActor(button);
            button.setPosition(collegePos.x,collegePos.y);
            button.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    gS.SetPlayerCollege(cName);
                    RemoveActors();
                }
            });
            buttons.add(button);
        }



    }
    public void drawColleges(SpriteBatch batch){
        Iterator<Button> iterButtons = buttons.iterator();
        while(iterButtons.hasNext()) {
            Button b = iterButtons.next();
            b.draw(batch,1);
        }
        Iterator<College> iterColleges = collegeList.iterator();
        while(iterColleges.hasNext()){
            College c = iterColleges.next();
            String cName = c.getName();
            Vector2 collegePos = getCollegePosition(c);
            font.draw(batch,cName,collegePos.x,collegePos.y);
        }

    }

    public void drawChooseCollegeDisplay(SpriteBatch batch){
        batch.draw(wholeMap, 0, 0,
                width, height, 0,
                0, wholeMap.getWidth(), wholeMap.getHeight(), false, true);
        drawColleges(batch);
    }


}
