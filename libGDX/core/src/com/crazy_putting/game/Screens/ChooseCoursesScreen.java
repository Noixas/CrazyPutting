package com.crazy_putting.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.Others.MultiplayerSettings;

import static com.crazy_putting.game.GameLogic.GraphicsManager.WINDOW_HEIGHT;
import static com.crazy_putting.game.GameLogic.GraphicsManager.WINDOW_WIDTH;

/**
 * Screen in which the user can choose from a variety of already defined courses(with properites such as height function,
 * friction coefficient etc.) or click a button to create his own course.
 */
public class ChooseCoursesScreen implements Screen{
    private static GolfGame game;
    private  int _mode;
    private Stage stage;
    private SelectBox<String> selectBox;
    private Skin skin;

    private Label heightValue;
    private Label frictionValue;
    private Label startValue;
    private Label goalValue;
    private Label radiusValue;
    private Label maxVelocityValue;
    private TextButton confirmButton;
    private Label splines;

    private TextField playersNumberField;
    private Label errorLabel;
    private Label allowedDistanceLabel;

    private SpriteBatch batch;
    private Sprite sprite;
    private Table table;

    private Slider allowedDistanceSlider;
    private Button[] multiplayerAmount;
    private ButtonGroup multButtonGroup;
    private ButtonGroup buttonGroupSimultaneous;
    private TextButton buttonSimultaneous;
    private TextButton buttonNotSimultaneous;
    private boolean simultaneous;

    public ChooseCoursesScreen(GolfGame game, int pMode) {
        if(MenuScreen.Spline3D == false)
            CourseManager.loadFile("courses.txt");
        else CourseManager.loadFile("coursesSpline.txt");

        this.game = game;
        _mode = pMode;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));

        /*
            Background
         */
        batch = game.batch;
        sprite = new Sprite(new Texture(Gdx.files.internal("GRASS4.png")));
        sprite.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //

        /*
            Set up the first label.
         */
        Label label = new Label("Choose course (Click the name)",skin);
        //label.setFontScale(0.8f);
        Vector2 labelSize = new Vector2(50, 50);
        label.setSize(labelSize.x, labelSize.y);
        label.setPosition(200, WINDOW_HEIGHT*0.9f-100);
        /*
            Multiplayer options
        */

        /*
            Set up the drop-down menu (select box).
         */

        selectBox = new SelectBox<String>(skin);
        selectBox.setPosition(300, WINDOW_HEIGHT*0.9f-130);
        Vector2 selectBoxSize = new Vector2(200, 50);
        selectBox.setSize(selectBoxSize.x, selectBoxSize.y);

        String[] boxItems = new String[CourseManager.getCourseAmount()];
        for (int i =0; i < CourseManager.getCourseAmount(); i++)
        {
            boxItems[i] = "Course "+ i;
        }
        selectBox.setItems(boxItems);

        /*
            Listener that triggers action if different option is chosen in select box.
         */
        selectBox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof ChangeListener.ChangeEvent) {
                    updateCourseInfo();
                }
                return true;
                }
            }
        );

        /*
             Set up necessary labels.
         */
        Label courseProperties = new Label("Course properties:", skin);
        Label heightLabel = new Label("Height function",skin);
        if(MenuScreen.Spline3D) heightLabel = new Label("SPLINE MODE",skin);
        Label frictionLabel = new Label("Friction coefficient",skin);
        Label startLabel = new Label("Start position",skin);
        Label goalLabel = new Label("Goal position",skin);
        Label radiusLabel = new Label("Radius of the target \t", skin);
        Label maxVelocityLabel = new Label("Max velocity",skin);


        heightValue = new Label(selectBox.getSelected(),skin);
        if(MenuScreen.Spline3D)heightValue = new Label("",skin);
        frictionValue = new Label(selectBox.getSelected(),skin);
        startValue = new Label(selectBox.getSelected(),skin);
        goalValue = new Label(selectBox.getSelected(),skin);
        radiusValue = new Label(selectBox.getSelected(),skin);
        maxVelocityValue = new Label(selectBox.getSelected(),skin);

        splines = new Label("Spline info", skin);
        Label splineText = new Label("Spline Points Height",skin);
        /*
            Add "Create course" button
         */
        TextButton createCourseButton = new TextButton("Create course", skin);
        createCourseButton.addListener(new ClickListener(){

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setCourseCreator();
            }
        });


        /*
            Add previously initialized labels to a table.
         */
        updateCourseInfo();
        table = new Table();

        // For multiple players.
        if(MenuScreen.Multiplayer) {
            Label playersNumberLabel = new Label("Number of players",skin);
            playersNumberField = new TextField("", skin);
            allowedDistanceSlider = new Slider(50,1000,10,false,skin);
            allowedDistanceSlider.setValue(400);
            allowedDistanceSlider.setWidth(400);
            allowedDistanceLabel = new Label("Allowed distance "+ allowedDistanceSlider.getValue(),skin);

            table.add(playersNumberLabel).align(Align.left);
            multiplayerAmount = new Button[4];
            multButtonGroup = new ButtonGroup();
            Table buttonsTable = new Table();//HAd to create this table because buttons werent aligning properly
            for(int i = 0; i < multiplayerAmount.length; i++){
                multiplayerAmount[i] = new CheckBox(i+1+"",skin);
                multiplayerAmount[i].setScale(1.5f);
                multButtonGroup.add(multiplayerAmount[i]);
                buttonsTable.add(multiplayerAmount[i]).align(Align.right);

            }
            table.add(buttonsTable).align(Align.left);
            multButtonGroup.uncheckAll();
            multButtonGroup.setChecked("1");
            multButtonGroup.setMinCheckCount(1);
            multButtonGroup.setMaxCheckCount(1);

            buttonSimultaneous = new TextButton("Simultaneous", skin,"toggle");
            buttonSimultaneous.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) { simultaneous = true;
                }
            });
            buttonNotSimultaneous = new TextButton("One by one", skin,"toggle");
            buttonNotSimultaneous.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) { simultaneous = false;
                }
            });

            ButtonGroup buttonGroupSimultaneous = new ButtonGroup(buttonSimultaneous, buttonNotSimultaneous);
            //next set the max and min amount to be checked
            buttonGroupSimultaneous.setMaxCheckCount(1);
            buttonGroupSimultaneous.setMinCheckCount(1);
            buttonGroupSimultaneous.setChecked("One by one");

            table.row();
            table.add(allowedDistanceLabel).align(Align.left);
            table.add(allowedDistanceSlider).align(Align.left);
            table.row();
            table.add(new Label("Strategy", skin)).align(Align.left);
            table.add(buttonSimultaneous).align(Align.left);
            table.add(buttonNotSimultaneous);
            table.row();
            errorLabel = new Label("", skin);
            errorLabel.setSize(200,50);
            errorLabel.setPosition(300,500);
            stage.addActor(errorLabel);
        }

        table.setWidth(stage.getWidth());
        table.align(Align.center|Align.top);
        table.setPosition(0, Gdx.graphics.getHeight());
        table.padTop(150);
        //table.setWidth(WINDOW_WIDTH);
        //table.align(Align.center);
        //table.setPosition(10,WINDOW_HEIGHT*0.5f);
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
        table.row();
        table.add(createCourseButton).align(Align.left);
        if(MenuScreen.Spline3D) {
            table.row();
            table.add(splineText).align(Align.left);
            table.add(splines).align(Align.left);
        }
        /*
            Add confirmation button.
         */
        confirmButton = new TextButton("Confirm", skin);
        Vector2 buttonSize = new Vector2(200,50);
        confirmButton.setPosition(WINDOW_WIDTH/2-buttonSize.x/2 + 100, buttonSize.y*2 - 50);
        confirmButton.setSize(buttonSize.x,buttonSize.y);
        confirmButton.addListener(new ClickListener(){

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                confirmButtonClicked();
            }
        });
        confirmButton.setColor(Color.WHITE);

        /*
            Add all actors to stage so that they are displayed.
         */
        stage.addActor(table);
        stage.addActor(label);
        stage.addActor(confirmButton);
        stage.addActor(selectBox);
    }

    @Override
    public void show() {

    }

    public void setCourseCreator(){
        game.setScreen(new CourseCreatorScreen(game));
    }

    /*
        update labels if user clicked on a new course.
     */
    public void updateCourseInfo()
    {
        //System.out.println(selectBox.getSelectedIndex() + " is the box index");
        //System.out.println("amount of cour " + CourseManager.getCourseAmount() );
        //System.out.println("course " + CourseManager.getCourseWithIndex(selectBox.getSelectedIndex()));
        if(MenuScreen.Spline3D == false)
        heightValue.setText(CourseManager.getCourseWithIndex(selectBox.getSelectedIndex()).getHeight());
        frictionValue.setText(CourseManager.getCourseWithIndex(selectBox.getSelectedIndex()).getFriction()+"");
        startValue.setText(CourseManager.getCourseWithIndex(selectBox.getSelectedIndex()).getStartBall(0).toString());
        goalValue.setText(CourseManager.getCourseWithIndex(selectBox.getSelectedIndex()).getGoalPosition(0).toString());
        radiusValue.setText(CourseManager.getCourseWithIndex(selectBox.getSelectedIndex()).getGoalRadius()+"");
        maxVelocityValue.setText(CourseManager.getCourseWithIndex(selectBox.getSelectedIndex()).getMaxSpeed()+"");
        if(MenuScreen.Spline3D)
            splines.setText( CourseManager.getCourseWithIndex(selectBox.getSelectedIndex()).toStringSplinePointsMatrix());

    }
    @Override
    public void render(float delta) {
       if(MenuScreen.Multiplayer) updateSliderValues();

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
    private void updateSliderValues(){
        if(allowedDistanceSlider.isDragging())
            allowedDistanceLabel.setText("Allowed distance "+ allowedDistanceSlider.getValue());
    }
    public void confirmButtonClicked(){

        if (MenuScreen.Multiplayer){
            updateMultiplayer();
        }

        CourseManager.setActiveCourseWithIndex(selectBox.getSelectedIndex());
        if(selectBox.getSelectedIndex() != CourseManager.getIndexActive())//IMPORTANT: if is a different course from the active one then we need to parse height formula again
            CourseManager.reParseHeightFormula(selectBox.getSelectedIndex());

        if(MenuScreen.Mode3D ==false)
            game.setScreen(new GameScreen(game,_mode));
        else
            game.setScreen(new GameScreen3D(game,_mode));

    }

    private void updateMultiplayer()
    {
        try{
            TextButton button = (TextButton)multButtonGroup.getChecked();
            int playersNumber = Integer.parseInt(button.getText().toString());
            int allowedDistance = (int)allowedDistanceSlider.getValue();
            new MultiplayerSettings(playersNumber, allowedDistance, simultaneous);
        }catch(Exception e)
        {
            System.out.println(e.toString());
            errorLabel.setText("You must input values in text fields");
            confirmButton.addListener(new ClickListener(){

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    confirmButtonClicked();
                }
            });
        }
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