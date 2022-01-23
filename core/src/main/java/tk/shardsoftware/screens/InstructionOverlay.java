package tk.shardsoftware.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Disposable;

import tk.shardsoftware.util.ResourceUtil;

public class InstructionOverlay implements Disposable {

	private SpriteBatch batch;
	private ShapeRenderer shapeRend;

	public boolean shouldDisplay = true;

	public InstructionOverlay(SpriteBatch batch) {
		this.batch = batch;
		this.shapeRend = new ShapeRenderer();
	}

	public void render() {
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		batch.begin();
		shapeRend.begin(ShapeType.Filled);

		shapeRend.setColor(0.1f, 0.1f, 0.1f, 0.5f);
		shapeRend.rect(0, 0, 1280, 720);

		shapeRend.setColor(Color.TAN);
		shapeRend.circle(50 + 90, Gdx.graphics.getHeight() - 50, 35); // top
		shapeRend.circle(50, Gdx.graphics.getHeight() - 50 - 90, 35); // left
		shapeRend.circle(50 + 90 + 90, Gdx.graphics.getHeight() - 50 - 90, 35); // right
		shapeRend.circle(50 + 90, Gdx.graphics.getHeight() - 50 - 90 - 90, 35); // bottom

		shapeRend.circle(50, Gdx.graphics.getHeight() - 420, 35);

		shapeRend.setColor(Color.BLACK);
		shapeRend.rectLine(50 + 50, Gdx.graphics.getHeight() - 50 - 90, 50 + 40 + 90,
				Gdx.graphics.getHeight() - 50 - 90, 3);
		shapeRend.rectLine(50 + 90, Gdx.graphics.getHeight() - 50 - 50, 50 + 90,
				Gdx.graphics.getHeight() - 50 - 90 - 40, 3);

		shapeRend.end();
		batch.end();

		batch.begin();
		ResourceUtil.font.draw(batch, "W", 50 + 78, Gdx.graphics.getHeight() - 35); // top
		ResourceUtil.font.draw(batch, "S", 50 + 80, Gdx.graphics.getHeight() - 35 - 90 - 90); // bottom
		ResourceUtil.font.draw(batch, "A", 40, Gdx.graphics.getHeight() - 35 - 90); // left
		ResourceUtil.font.draw(batch, "D", 40 + 90 + 90, Gdx.graphics.getHeight() - 35 - 90); // right

		ResourceUtil.font.draw(batch, "F", 45, Gdx.graphics.getHeight() - 410); // right
		ResourceUtil.font.draw(batch, "Fire Cannons", 40 + 70, Gdx.graphics.getHeight() - 410);

		ResourceUtil.font.draw(batch, "Move Controls", 40,
				Gdx.graphics.getHeight() - 50 - 90 - 90 - 70);
		batch.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);
	}

	@Override
	public void dispose() {
		shapeRend.dispose();
	}

}
