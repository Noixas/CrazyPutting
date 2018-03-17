package com.crazy_putting.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyCrazyPutting extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	float x = 0;
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("golfBall.png");
	}

	@Override
	public void render () {
		int red = 34;
		int green = 137;
		int blue = 34;
		Gdx.gl.glClearColor((float)(red/255.0), (float)(green/255.0), (float)(blue/255.0), 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
//		batch.draw(img, x, 0);
		batch.draw(img, 100, 100, 50	, 50);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
