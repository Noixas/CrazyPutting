package com.crazy_putting.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.crazy_putting.game.states.GameStateManager;
import com.crazy_putting.game.states.MenuState;

public class MyCrazyPutting extends ApplicationAdapter {
	public static final int WIDTH = 700;
	public static final int HEIGHT =  700;
	public static final String TITLE = "Golf Game";

	Texture img;
	private GameStateManager gsm;
	private SpriteBatch batch;

	float x = 0;
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		batch = new SpriteBatch();
		gsm = new GameStateManager();

		int red = 34;
		int green = 137;
		int blue = 34;
		Gdx.gl.glClearColor((float)(red/255.0), (float)(green/255.0), (float)(blue/255.0), 1);
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
//		img.dispose();
	}
}
