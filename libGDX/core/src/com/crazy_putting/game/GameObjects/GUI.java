package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameLogic.GameManager;
import com.crazy_putting.game.Screens.GameScreen3D;
import com.crazy_putting.game.Screens.GolfGame;

public class GUI extends GameObject {
/*
TODO: Use stage and the view part created in gamescreen3D to create an input listener so the buttons work
 */

    private GolfGame _game;
    private GameManager _gameManager;
    private Skin _skin;
    private TextButton saveSplines;
    private Table table;
    private Stage UIStage;
    private Label speedText;
    private Label ball_position;
    private Label turnCount;
    private Label maxSpeed;
    Viewport view;
    private CheckBox _splineEdit;
    private CheckBox _changeBallPos;
    private  CheckBox _changeHolePos;
    private  CheckBox _addObjects;
    private boolean _spline;

    public GUI(GolfGame pGame, GameManager pGameManager, FitViewport viewPort, boolean pSpline)
    {
        _game = pGame;
        _spline = pSpline;
        if(viewPort ==null)
            view = new FitViewport(10,10);
       else
           view = viewPort;
        UIStage = new Stage(viewPort,_game.batch);
        UIStage.getViewport().setScreenBounds(GameScreen3D.Width3DScreen,0 ,GameScreen3D.Width2DScreen-1,GameScreen3D.Height2DScreen-1);
        UIStage.getViewport().apply();
        UIStage.setDebugAll(false);
        _gameManager = pGameManager;
        _skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
        saveSplines = new TextButton("Save Splines", _skin);
        saveSplines.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                CourseManager.saveCourseSpline();
                _gameManager.saveBallAndHolePos();
                CourseManager.reWriteCourse();
            }
        });
        _splineEdit = new CheckBox("Spline Editor", _skin);
        _changeBallPos = new CheckBox("Change Ball Position", _skin);
        _changeHolePos = new CheckBox("Change Hole Position", _skin);
        _addObjects = new CheckBox("Add Objects", _skin);

        initUI();
        System.out.println("VIEWPORT POS "+ view.getScreenY()   );

    }
    private void initUI()
    {
        Skin skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
        table = new Table();

        table.setFillParent(true);
        table.center();

        speedText = new Label("Speed: " + _gameManager.getBall().getVelocity().getSpeed(),skin);

        int x = (int)_gameManager.getBall().getPosition().x;
        int y = (int)_gameManager.getBall().getPosition().y;
        int height = (int)CourseManager.calculateHeight(x,y);
        ball_position = new Label("Ball Position\n" + "height: " + height + "\nx:" + x + " y: " + y,skin);


        turnCount = new Label("Turns: " + _gameManager.getTurns(),skin);
        maxSpeed = new Label("Max speed: " + CourseManager.getMaxSpeed()+"\n",skin);

        ButtonGroup buttonGroup = new ButtonGroup(_splineEdit, _changeBallPos, _changeHolePos,_addObjects);
        //next set the max and min amount to be checked
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(0);
        buttonGroup.uncheckAll();

        table.add(maxSpeed);
        table.row();
        table.add(speedText);
        table.row();
        table.add(ball_position);
        table.row();
        table.add(turnCount);
        if(_spline) {
            table.row();
            table.add(saveSplines);
            table.row();
            table.add(_splineEdit);
            table.row();
            table.add(_changeBallPos);
            table.row();
            table.add(_changeHolePos);
            table.row();
            table.add(_addObjects);
        }

        UIStage.addActor(table);



    }
    public InputProcessor getUIInputProcessor()
    {
        return UIStage;
    }
    public void render()
    {
        Ball ball =_gameManager.getBall();

        maxSpeed.setText("Max speed: " + CourseManager.getMaxSpeed());
        speedText.setText("Speed: " + (int) (_gameManager.getBall().getVelocity().getSpeed()));
        int x = (int)ball.getPosition().x;
        int y = (int)ball.getPosition().y;
        int height = (int)CourseManager.calculateHeight(x,y);
        ball_position.setText("Ball Position\n" + "height: " + height + "\nx:" + x + " y: " + y);
        turnCount.setText("Turns: " + _gameManager.getTurns());

        UIStage.draw();

    }
    public boolean isSplineEditActive()
    {
        return _splineEdit.isChecked();
    }
    public boolean isChangeBallActive()
    {

        return _changeBallPos.isChecked();
    }
    public boolean isChangeHoleActive()
    {
        return _changeHolePos.isChecked();
    }

    public boolean isAddObjectsActive()
    {
        return _addObjects.isChecked();
    }

}
