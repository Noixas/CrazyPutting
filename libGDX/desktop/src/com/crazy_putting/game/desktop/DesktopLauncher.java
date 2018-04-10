package com.crazy_putting.game.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.crazy_putting.game.GameLogic.GraphicsManager;
import com.crazy_putting.game.Screens.GolfGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GraphicsManager.WINDOW_WIDTH;
		config.height = GraphicsManager.WINDOW_HEIGHT;
		config.title = "Crazy putting";
		new LwjglApplication(new GolfGame(), config);
	}
}
