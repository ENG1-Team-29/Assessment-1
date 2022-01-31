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
	private ShapeRenderer shapeRenderer;

	public boolean shouldDisplay = true;

	public InstructionOverlay(SpriteBatch batch) {
		this.batch = batch;
		this.shapeRenderer = new ShapeRenderer();
	}

	public void render() {
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		batch.begin();
		shapeRenderer.begin(ShapeType.Filled);

		shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.5f);
		shapeRenderer.rect(0, 0, 1280, 720);

		shapeRenderer.setColor(Color.TAN);
		shapeRenderer.circle(50 + 90, Gdx.graphics.getHeight() - 50, 35); // top
		shapeRenderer.circle(50, Gdx.graphics.getHeight() - 50 - 90, 35); // left
		shapeRenderer.circle(50 + 90 + 90, Gdx.graphics.getHeight() - 50 - 90, 35); // right
		shapeRenderer.circle(50 + 90, Gdx.graphics.getHeight() - 50 - 90 - 90, 35); // bottom

		shapeRenderer.circle(50, Gdx.graphics.getHeight() - 420, 35); // fire

		shapeRenderer.circle(50, Gdx.graphics.getHeight() - 420 - 90, 35); // map

		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rectLine(50 + 50, Gdx.graphics.getHeight() - 50 - 90, 50 + 40 + 90,
				Gdx.graphics.getHeight() - 50 - 90, 3);
		shapeRenderer.rectLine(50 + 90, Gdx.graphics.getHeight() - 50 - 50, 50 + 90,
				Gdx.graphics.getHeight() - 50 - 90 - 40, 3);

		shapeRenderer.end();
		batch.end();

		batch.begin();
		ResourceUtil.font.draw(batch, "W", 50 + 78, Gdx.graphics.getHeight() - 35); // top
		ResourceUtil.font.draw(batch, "S", 50 + 80, Gdx.graphics.getHeight() - 35 - 90 - 90); // bottom
		ResourceUtil.font.draw(batch, "A", 40, Gdx.graphics.getHeight() - 35 - 90); // left
		ResourceUtil.font.draw(batch, "D", 40 + 90 + 90, Gdx.graphics.getHeight() - 35 - 90); // right

		ResourceUtil.font.draw(batch, "F", 45, Gdx.graphics.getHeight() - 410); // fire
		ResourceUtil.font.draw(batch, "Fire Cannons", 40 + 70, Gdx.graphics.getHeight() - 410);

		ResourceUtil.font.draw(batch, "M", 40, Gdx.graphics.getHeight() - 410 - 90); // map
		ResourceUtil.font.draw(batch, "Toggle Map", 40 + 70, Gdx.graphics.getHeight() - 410 - 90);

		ResourceUtil.font.draw(batch, "Move Controls", 40,
				Gdx.graphics.getHeight() - 50 - 90 - 90 - 70);
		batch.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

}
