package com.crazy_putting.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

    public MenuScreen(final GolfGame golfGame) {

        this.golfGame = golfGame;
        finalMenu = this;

        skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // background
        batch = golfGame.batch;
        sprite = new Sprite(new Texture(Gdx.files.internal("GRASS.jpg")));
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
