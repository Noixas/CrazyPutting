package com.crazy_putting.game.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.crazy_putting.game.MyCrazyPutting;
import com.crazy_putting.game.Screens.GolfGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = MyCrazyPutting.WIDTH;
		config.height = MyCrazyPutting.HEIGHT;
		config.title = MyCrazyPutting.TITLE;
		new LwjglApplication(new GolfGame(), config);
	}
}
