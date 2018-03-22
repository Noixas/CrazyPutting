package com.crazy_putting.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import static com.crazy_putting.game.GameLogic.GraphicsManager.WINDOW_HEIGHT;
import static com.crazy_putting.game.GameLogic.GraphicsManager.WINDOW_WIDTH;

public class CourseCreatorScreen implements Screen {
    private static GolfGame game;
    private Stage stage;
    private TextButton confirmButton;
//    private TextField textField;

    public CourseCreatorScreen(GolfGame game){
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));

        Label title = new Label("Course creator", skin);
        title.setColor(Color.BLUE);
        title.setPosition(WINDOW_WIDTH/2-title.getWidth()/2,WINDOW_HEIGHT*0.75f);

        confirmButton = new TextButton("Confirm", skin);
        Vector2 buttonSize = new Vector2(200,50);
        confirmButton.setPosition(WINDOW_WIDTH/2-buttonSize.x/2, buttonSize.y*2);
        confirmButton.setSize(buttonSize.x,buttonSize.y);
        confirmButton.addListener(new ClickListener(){

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                confirmButtonClicked();
            }
        });
        confirmButton.setColor(Color.WHITE);
        Label heightLabel = new Label("Height function",skin);
        TextField heightText = new TextField("", skin);
        Label frictionLabel = new Label("Friction coefficient",skin);
        TextField frictionText = new TextField("", skin);
        Label startLabel = new Label("Start position",skin);
        TextField startText = new TextField("", skin);
        Label goalLabel = new Label("Goal position",skin);
        TextField goalText = new TextField("", skin);
        Label radiusLabel = new Label("Radius of the target",skin);
        TextField radiusText = new TextField("", skin);
        Label maxVelocityLabel = new Label("Max velocity",skin);
        TextField maxVelocityText = new TextField("", skin);
        /*
            Important: To change color of font of a label/button/etc. you need to change it in assets in .json
            file of a skin, where it is defined for a particular component.
            You can also add your own colors in RGB format.
         */

        Table table = new Table();
        table.setWidth(WINDOW_WIDTH);
        table.align(Align.center);
        table.setPosition(0,WINDOW_HEIGHT/2);
        table.add(heightLabel);
        table.add(heightText);
        table.row();
        table.add(frictionLabel);
        table.add(frictionText);
        table.row();
        table.add(startLabel);
        table.add(startText);
        table.row();
        table.add(goalLabel);
        table.add(goalText);
        table.row();
        table.add(radiusLabel);
        table.add(radiusText);
        table.row();
        table.add(maxVelocityLabel);
        table.add(maxVelocityText);
//        textField = new TextField("", skin);
//        Vector2 textFieldSize = new Vector2(200,50);
//        textField.setPosition(confirmButton.getX(),confirmButton.getY()-buttonSize.y);
//        textField.setSize(textFieldSize.x, textFieldSize.y);
//        stage.addActor(textField);
        stage.addActor(title);
        stage.addActor(table);
        stage.addActor(confirmButton);

    }

    public static void confirmButtonClicked(){
        // TODO game logic needs to be implemented
        System.out.println("Put here game logic...");
        game.setScreen(new GameScreen(game,1));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        int red = 34;
        int green = 137;
        int blue = 34;
        Gdx.gl.glClearColor((float)(red/255.0), (float)(green/255.0), (float)(blue/255.0), 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
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
