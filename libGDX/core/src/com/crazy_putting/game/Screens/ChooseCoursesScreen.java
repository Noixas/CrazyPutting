package com.crazy_putting.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import static com.crazy_putting.game.GameLogic.GraphicsManager.WINDOW_HEIGHT;
import static com.crazy_putting.game.GameLogic.GraphicsManager.WINDOW_WIDTH;

public class ChooseCoursesScreen implements Screen{
    private static GolfGame game;
    private Stage stage;
    private SelectBox<String> selectBox;
    private Label heightValue;
    private Label frictionValue;
    private Label startValue;
    private Label goalValue;
    private Label radiusValue;
    private Label maxVelocityValue;

    public ChooseCoursesScreen(GolfGame game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
        Label label = new Label("Choose course",skin);
        label.setFontScale(0.8f);

        Vector2 labelSize = new Vector2(50, 50);

        label.setSize(labelSize.x, labelSize.y);
        label.setPosition(50, WINDOW_HEIGHT*0.9f);

        selectBox = new SelectBox<String>(skin);
        selectBox.setPosition(50, WINDOW_HEIGHT*0.9f-30);
        Vector2 selectBoxSize = new Vector2(200, 50);
        selectBox.setSize(selectBoxSize.x, selectBoxSize.y);
        String[] boxItems = {"Course 1", "Course 2", "Course 3"};
        // this array doesn't have to be String - I would make an object Course which has it's name, height function
        // and all these properties and make an array of them
        selectBox.setItems(boxItems);

        Label courseProperties = new Label("Course properties", skin);
        Label heightLabel = new Label("Height function",skin);
        Label frictionLabel = new Label("Friction coefficient",skin);
        Label startLabel = new Label("Start position",skin);
        Label goalLabel = new Label("Goal position",skin);
        Label radiusLabel = new Label("Radius of the target \t", skin);
        Label maxVelocityLabel = new Label("Max velocity",skin);

        heightValue = new Label(selectBox.getSelected(),skin);
        frictionValue = new Label(selectBox.getSelected(),skin);
        startValue = new Label(selectBox.getSelected(),skin);
        goalValue = new Label(selectBox.getSelected(),skin);
        radiusValue = new Label(selectBox.getSelected(),skin);
        maxVelocityValue = new Label(selectBox.getSelected(),skin);

        Table table = new Table();
        table.setWidth(WINDOW_WIDTH);
        table.align(Align.center);
        table.setPosition(WINDOW_WIDTH/6,WINDOW_HEIGHT*0.5f);

        table.add(courseProperties).align(Align.left);
        table.row();
        table.add(heightLabel).align(Align.left);
        table.add(heightValue);
        table.row();
        table.add(frictionLabel).align(Align.left);
        table.add(frictionValue);
        table.row();
        table.add(startLabel).align(Align.left);
        table.add(startValue);
        table.row();
        table.add(goalLabel).align(Align.left);
        table.add(goalValue);
        table.row();
        table.add(radiusLabel).align(Align.left);
        table.add(radiusValue);
        table.row();
        table.add(maxVelocityLabel).align(Align.left);
        table.add(maxVelocityValue);

        stage.addActor(table);
        stage.addActor(label);
        stage.addActor(selectBox);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,1,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        System.out.println(selectBox.getSelected());
//        heightLabel.setText(selectBox.getSelected());
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