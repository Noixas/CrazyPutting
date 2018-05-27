package com.crazy_putting.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen {

    private GolfGame golfGame;

    private Stage stage;
    private Skin skin;
    public static MenuScreen finalMenu;

    private SpriteBatch batch;
    private Sprite sprite;

    private Table table;
    private TextButton soloButton;
    private TextButton fileButton;
    private TextButton aiButton;
    private TextButton courseCreatorButton;
    //private Label heading;
    //private BitmapFont font;
    //private TextureAtlas atlas;
    private Skin skin2;


    private TextButton button2D;
    private TextButton button3D;
    private  TextButton button3DSpline;

    public static boolean Mode3D = true; //TODO:Check if its better to implement this somewhere else
    public static boolean Spline3D = false; //TODO:Check if its better to implement this somewhere else

    public MenuScreen(final GolfGame golfGame) {

        this.golfGame = golfGame;
        finalMenu = this;

        //font = new BitmapFont(Gdx.files.internal("skin/font-title-export.fnt"));
        //atlas = new TextureAtlas("skin/myButton.json");
        //skin = new Skin(atlas);
        //skin2 = new Skin(Gdx.files.internal("skin/plain-james-ui2.json"));
        skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // background
        batch = golfGame.batch;
        sprite = new Sprite(new Texture(Gdx.files.internal("GRASS4.png")));
        sprite.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        // buttons
        soloButton = new TextButton("Human player mode", skin);
        soloButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // go to "Select course" screen, with mode 1
                golfGame.setScreen(new ChooseCoursesScreen(golfGame, 1));
            }
        });

        fileButton = new TextButton("File input mode", skin);
        fileButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                golfGame.setScreen(new ChooseCoursesScreen(golfGame,2));
            }
        });

        aiButton = new TextButton("AI player mode", skin);
        aiButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                golfGame.setScreen(new ChooseCoursesScreen(golfGame,3)); // go to "ModesScreen" screen
            }
        });

        courseCreatorButton = new TextButton("Create course", skin);
        courseCreatorButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                golfGame.setScreen(new CourseCreatorScreen(golfGame)); // go to "Course creator" screen
            }
        });

        button2D = new TextButton("2D", skin,"toggle");
        button2D.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
              Mode3D = false;
            }
        });

        button3D = new TextButton("3D", skin,"toggle");
        button3D.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Mode3D = true;
            }
        });

        button3DSpline = new TextButton("Spline", skin,"toggle");
        button3DSpline.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Spline3D = true;
            }
        });

        ButtonGroup buttonGroup = new ButtonGroup(button2D, button3D, button3DSpline);
//next set the max and min amount to be checked
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        buttonGroup.setChecked("3D");

        Table tableDimensions = new Table();
        tableDimensions.setWidth(stage.getWidth());
        //  tableDimensions.align(Align.center|Align.top);
        tableDimensions.setPosition(400, Gdx.graphics.getHeight()-100);
        tableDimensions.row();
        tableDimensions.add(button3D).size(100, 50);;
        tableDimensions.add(button2D).size(100, 50);;
        tableDimensions.add(button3DSpline).size(100, 50);;

        // table
        table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center|Align.top);
        table.setPosition(0, Gdx.graphics.getHeight());

        table.padTop(150);
        table.add(soloButton).size(300, 50).padBottom(20);
        table.row();
        table.add(fileButton).size(300, 50).padBottom(20);
        table.row();
        table.add(aiButton).size(300, 50).padBottom(20);
        table.row();
        table.add(courseCreatorButton).size(300, 50);;

        stage.addActor(table);
        stage.addActor(tableDimensions);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // background
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        sprite.draw(batch);
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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
