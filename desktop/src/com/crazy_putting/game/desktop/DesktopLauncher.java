package com.crazy_putting.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.crazy_putting.game.GameLogic.GraphicsManager;
import com.crazy_putting.game.Screens.GolfGame;

public class DesktopLauncher {
	private static LwjglApplication app;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GraphicsManager.WINDOW_WIDTH;
		config.height = GraphicsManager.WINDOW_HEIGHT;
		config.title = "Crazy putting";
		app = new LwjglApplication(new GolfGame(), config);
	}
	public static void restartGame(){
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GraphicsManager.WINDOW_WIDTH;
		config.height = GraphicsManager.WINDOW_HEIGHT;
		config.title = "Crazy putting";
		app = new LwjglApplication(new GolfGame(), config);
	}
}
