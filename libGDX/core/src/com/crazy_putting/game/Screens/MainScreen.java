package com.crazy_putting.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.MyCrazyPutting;
import com.crazy_putting.game.Parser.Parser;


public class MainScreen extends InputAdapter implements Screen {
    final GolfGame game;
    OrthographicCamera camera;
    ShapeRenderer renderer;
    FitViewport viewport;
    private Rectangle playerRectangle;
    private Rectangle fileRectangle;
    private Rectangle settingsRectangle;

    private final int viewportX = MyCrazyPutting.WIDTH;
    private final int viewportY = MyCrazyPutting.HEIGHT;

    public MainScreen(GolfGame game){
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportX, viewportY);
        CourseManager.loadFile("courses.txt");
        for (int i = 0; i < CourseManager.getCourseAmount(); i++)
        System.out.println(CourseManager.getCourseList().get(i).toString());
    }

    @Override
    public void show() {
        renderer = new ShapeRenderer();

        viewport = new FitViewport(viewportX, viewportY);
        Gdx.input.setInputProcessor(this);
        game.font.getData().setScale(1);
        game.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

    }

    @Override
    public void render(float delta) {

        int red = 34;
        int green = 137;
        int blue = 34;
        Gdx.gl.glClearColor((float)(red/255.0), (float)(green/255.0), (float)(blue/255.0), 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

//        game.batch.begin();
//        game.font.draw(game.batch, "Main menu", viewportX/2, viewportY/2);
//        game.batch.end();
        final Color BUTTON_COLOR = new Color((float)(10/255.0), (float)(50/255.0), (float)(10/255.0),0);
        final int RECTANGLE_WIDTH = 230;
        final int RECTANGLE_HEIGHT = 60;
        final int PADDING = 10;
        final String PLAYER_LABEL = "Player input";
        final String FILE_LABEL = "File input";
        final String SETTINGS_LABEL = "Settings";
        playerRectangle = new Rectangle(viewportX/2-RECTANGLE_WIDTH/2, viewportY/2+RECTANGLE_HEIGHT+PADDING, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        fileRectangle = new Rectangle(viewportX/2-RECTANGLE_WIDTH/2, viewportY/2-PADDING, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        settingsRectangle = new Rectangle(viewportX/2-RECTANGLE_WIDTH/2, viewportY/2-RECTANGLE_HEIGHT-3*PADDING, RECTANGLE_WIDTH, RECTANGLE_WIDTH);



        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(BUTTON_COLOR);
        renderer.rect(playerRectangle.x, playerRectangle.y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        renderer.setColor(BUTTON_COLOR);
        renderer.rect(fileRectangle.x, fileRectangle.y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        renderer.setColor(BUTTON_COLOR);
        renderer.rect(settingsRectangle.x, settingsRectangle.y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        renderer.end();

        game.batch.begin();

        final GlyphLayout playerLayout = new GlyphLayout(game.font, PLAYER_LABEL);
        game.font.draw(game.batch,PLAYER_LABEL, playerRectangle.x+RECTANGLE_WIDTH/2,playerRectangle.y +3*playerLayout.height, 0, Align.center, false);

        final GlyphLayout fileLayout = new GlyphLayout(game.font, FILE_LABEL);
        game.font.draw(game.batch,FILE_LABEL, fileRectangle.x+RECTANGLE_WIDTH/2,fileRectangle.y + 3*fileLayout.height, 0, Align.center, false);

        final GlyphLayout settingsLayout = new GlyphLayout(game.font, SETTINGS_LABEL);
        game.font.draw(game.batch,SETTINGS_LABEL, settingsRectangle.x+RECTANGLE_WIDTH/2,settingsRectangle.y +3*settingsLayout.height, 0, Align.center, false);
        game.batch.end();

    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 worldTouch = viewport.unproject(new Vector2(screenX, viewportY-screenY));

        System.out.println("test");
        Parser.writeCourse("Test.txt",null);
        System.out.println(worldTouch.x);
        System.out.println(screenX);
        System.out.println(worldTouch.y);
        System.out.println(playerRectangle.x);
        System.out.println(playerRectangle.y);
        System.out.println("FUck this game");
        System.out.println(worldTouch.x<playerRectangle.x);
        System.out.println(worldTouch.y<playerRectangle.y);
        if(playerRectangle.contains(screenX, viewportY-screenY)){
            System.out.println("Touched");
            game.setScreen(new GameScreen(game));

        }
        if(fileRectangle.contains(worldTouch)){
            game.setScreen(new GameScreen(game));
        }
        if(settingsRectangle.contains(worldTouch)){
            game.setScreen(new GameScreen(game));
        }
        return true;
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
        renderer.dispose();
    }

    @Override
    public void dispose() {

    }


}
