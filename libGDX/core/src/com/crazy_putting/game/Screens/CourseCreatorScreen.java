package com.crazy_putting.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.crazy_putting.game.FormulaParser.*;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameObjects.Course;

import static com.crazy_putting.game.GameLogic.GraphicsManager.WINDOW_HEIGHT;
import static com.crazy_putting.game.GameLogic.GraphicsManager.WINDOW_WIDTH;

public class CourseCreatorScreen implements Screen {
    private static GolfGame game;
    private Stage stage;
    private TextButton confirmButton;
//    private TextField textField;

    private TextField heightText;
    private TextField frictionText;
    private TextField startTextX;
    private TextField startTextY;
    private TextField goalTextX;
    private TextField goalTextY;
    private TextField radiusText;
    private TextField maxVelocityText;
    private Label errorLabel;
    private ExpressionNode exp = null;
    private FormulaParser parser = new FormulaParser();

    private SpriteBatch batch;
    private Sprite sprite;
    private Table table;

    public CourseCreatorScreen(GolfGame game){
        if(MenuScreen.Spline3D == false)
        CourseManager.loadFile("courses.txt");
        else CourseManager.loadFile("coursesSpline.txt");

        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));

        /*
            Background
         */
        batch = game.batch;
        sprite = new Sprite(new Texture(Gdx.files.internal("GRASS4.png")));
        sprite.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //

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
        heightText = new TextField("", skin);
        Label frictionLabel = new Label("Friction coefficient",skin);
        frictionText = new TextField("", skin);
        Label startLabelX = new Label("Start position X",skin);
        startTextX = new TextField("", skin);
        Label startLabelY = new Label("Start position Y",skin);
        startTextY = new TextField("", skin);
        Label goalLabelX = new Label("Goal position X",skin);
        goalTextX = new TextField("", skin);
        Label goalLabelY = new Label("Goal position Y",skin);
        goalTextY = new TextField("", skin);
        Label radiusLabel = new Label("Radius of the target",skin);
        radiusText = new TextField("", skin);
        Label maxVelocityLabel = new Label("Max velocity",skin);
        maxVelocityText = new TextField("", skin);
        errorLabel = new Label("", skin);
        errorLabel.setSize(200,50);
        errorLabel.setPosition(300,buttonSize.y*3 + 30);

        /*
            Important: To change color of font of a label/button/etc. you need to change it in assets in .json
            file of a skin, where it is defined for a particular component.
            You can also add your own colors in RGB format.
         */

        table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center|Align.top);
        table.setPosition(0, Gdx.graphics.getHeight());

        table.padTop(150);
        //table.setWidth(WINDOW_WIDTH);
        //table.align(Align.center);
        //table.setPosition(0,WINDOW_HEIGHT/2);
        table.add(heightLabel);
        table.add(heightText);
        table.row();
        table.add(frictionLabel);
        table.add(frictionText);
        table.row();
        table.add(startLabelX);
        table.add(startTextX);

        table.add(startLabelY);
        table.add(startTextY);
        table.row();
        table.add(goalLabelX);
        table.add(goalTextX);
        table.add(goalLabelY);
        table.add(goalTextY);
        table.row();
        table.add(radiusLabel);
        table.add(radiusText);
        table.row();
        table.add(maxVelocityLabel);
        table.add(maxVelocityText);

        stage.addActor(errorLabel);
//        textField = new TextField("", skin);
//        Vector2 textFieldSize = new Vector2(200,50);
//        textField.setPosition(confirmButton.getX(),confirmButton.getY()-buttonSize.y);
//        textField.setSize(textFieldSize.x, textFieldSize.y);
//        stage.addActor(textField);
        stage.addActor(title);
        stage.addActor(table);
        stage.addActor(confirmButton);

    }

    public void confirmButtonClicked(){
        // TODO game logic needs to be implemented
        createCourse();
    }

    private  void createCourse()
    {


            try{
                System.out.println("Put here game logic...");
                Course newCourse = new Course();
                newCourse.setName("Course without name D:");
                newCourse.setHeight(heightText.getText());
                newCourse.setFriction(Float.parseFloat(frictionText.getText()));
                Vector3 ball_start_position = new Vector3(Float.parseFloat(startTextX.getText()), Float.parseFloat(startTextY.getText()),0);
                newCourse.setBallStartPos(ball_start_position,0);
                Vector3 goalStartPosition = new Vector3(Float.parseFloat(goalTextX.getText()), Float.parseFloat(goalTextY.getText()),0);
                newCourse.setGoalPosition(goalStartPosition,0);
                newCourse.setGoalRadius(Float.parseFloat(radiusText.getText()));
                newCourse.setMaxSpeed(Float.parseFloat(maxVelocityText.getText()));
                if(!isBallOrGoalUnderWater(ball_start_position, goalStartPosition)) {
                    CourseManager.addCourseToList(newCourse);
                    CourseManager.setActiveCourseWithIndex(CourseManager.getCourseAmount() - 1);
                    CourseManager.reWriteCourse();
                    game.setScreen(new MenuScreen(game));
                }
            }catch(Exception e)
            {
                System.out.println("Error saving course... Going Back to Menu");
                System.out.println(e.toString());
                System.out.println("Error saving course... Going Back to Menu");
                System.out.println("Error saving course... Going Back to Menu");
                errorLabel.setText("You must input values in text fields");
//            game.setScreen(new MenuScreen(game));
                confirmButton.addListener(new ClickListener(){

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        confirmButtonClicked();
                    }
                });
            }
    }
    private boolean isBallOrGoalUnderWater(Vector3 pBallPos, Vector3 pGoalPos)
    {
        try {
           // pPos = new Vector2(Float.parseFloat(startTextX.getText()), Float.parseFloat(startTextY.getText()));

            //   if (exp == null) {
            exp = parser.parse(heightText.getText());
            exp.accept(new SetVariable("x", pBallPos.x));
            exp.accept(new SetVariable("y", pBallPos.y));
            float resultBall = (float) exp.getValue();
            float resultGoal;
            exp.accept(new SetVariable("x", pGoalPos.x));
            exp.accept(new SetVariable("y", pGoalPos.y));
            resultGoal = (float) exp.getValue();
            if (resultBall < 0 || resultGoal < 0) {
                throw new IllegalArgumentException("Neither ball nor hole can be in water");
            }
            else return false;
        }   catch (IllegalArgumentException e){
            errorLabel.setText("Ball and hole starting position can't be in water");
            confirmButton.addListener(new ClickListener(){

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("Button clicked");
                    confirmButtonClicked();
                }
            });
            return true;
        }
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        /*
        int red = 34;
        int green = 137;
        int blue = 34;
        Gdx.gl.glClearColor((float)(red/255.0), (float)(green/255.0), (float)(blue/255.0), 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        */

          /*
            Background
         */
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //

        batch.begin();
        sprite.draw(batch);
        batch.end();

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
