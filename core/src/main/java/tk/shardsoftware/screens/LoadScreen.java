package tk.shardsoftware.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ScreenUtils;

public class LoadScreen implements Screen {

	private AssetManager assets;

	public LoadScreen(AssetManager assets) {
		this.assets = assets;
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 255);
		if (assets.update()) {

		} else {
			System.out.println(assets.getProgress());
		}

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

}
