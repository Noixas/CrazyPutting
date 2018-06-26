package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crazy_putting.game.Components.Graphics.Graphics3DComponent;
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
    private CheckBox _eraseObject;
    private CheckBox _editObject;
    private boolean _spline;

    private Slider _widthObstacle;
    private Slider _deepObstacle;
    private Slider _heightObstacle;
    private CheckBox _keepRatio;

    private Label _widthObstacleLabel;
    private Label _deepObstacleLabel;
    private Label _heightObstacleLabel;
    private Label controls;
    private Label simulationCounter;

    private SelectBox<String> playerSelectBox;
    private Ball _activaBall;
    private int _indexActivePlayerGameManager = 0;

    private ProgressBar _shootBar;

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
        saveSplines = new TextButton("Save Course", _skin);
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
        _eraseObject = new CheckBox("Erase Objects", _skin);
        _editObject = new CheckBox("Edit Object", _skin);
        _shootBar = new ProgressBar(0,100,1, true,_skin);
        _shootBar.setHeight(300);
        ProgressBar.ProgressBarStyle styleProgress = new ProgressBar.ProgressBarStyle();
       // styleProgress.background ;
      // Texture background = (Texture) _shootBar.getStyle().background;
      // background.getTextureData().//TODO: change color of backgroud with shader
        _shootBar.setPosition(0,GameScreen3D.Height3DScreen-_shootBar.getHeight()-50);
        initUI();
        System.out.println("VIEWPORT POS "+ view.getScreenY());
        _activaBall = _gameManager.getBall();
       updatePlayerActive();

    }
    public void addShootBar(float diff){
        _shootBar.setValue(_shootBar.getValue()+diff);
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

        _widthObstacle = new Slider(20,400,10,false,skin);
        _deepObstacle = new Slider(20,400,10,false,skin);
        _heightObstacle = new Slider(20,400,10,false,skin);
        _widthObstacle.setValue(80);
        Label obstacleDim = new Label("Obstacle Dimensions:",skin);
        _widthObstacleLabel = new Label("Width: 80",skin);
        _deepObstacle.setValue(80);
        _deepObstacleLabel = new Label("Deep: 80",skin);
        _heightObstacle.setValue(80);
        _heightObstacleLabel = new Label("Height: 80",skin);


        playerSelectBox = new SelectBox<String>(skin);
      //  playerSelectBox.setPosition(300, WINDOW_HEIGHT*0.9f-130);
        Vector2 selectBoxSize = new Vector2(200, 50);
        playerSelectBox.setSize(selectBoxSize.x, selectBoxSize.y);
      //  playerSelectBox.setColor(Color.OLIVE);
        playerSelectBox.getStyle().fontColor = Color.PURPLE;
        String[] boxItems = new String[_gameManager.getAmountPlayers()];
        for (int i =0; i < _gameManager.getAmountPlayers(); i++)
        {
            boxItems[i] = "Player "+ (i+1);
        }
        playerSelectBox.setItems(boxItems);

        /*
            Listener that triggers action if different option is chosen in select box.
         */
        playerSelectBox.addListener(new EventListener() {
                                  @Override
                                  public boolean handle(Event event) {
                                      if (event instanceof ChangeListener.ChangeEvent) {
                                          updatePlayerActive();
                                          return true;
                                      }
                                    return false;
                                  }
                              }
        );

        _keepRatio = new CheckBox("Same Dimensions",skin);
        _keepRatio.setChecked(true);

        _widthObstacle.setVisible(true);
       //  allowedDistanceLabel = new Label("Allowed distance "+ allowedDistanceSlider.getValue(),skin);

        turnCount = new Label("Turns: " + _gameManager.getTurns(),skin);
        maxSpeed = new Label("Max speed: " + CourseManager.getMaxSpeed()+"\n",skin);
        simulationCounter = new Label("Nr of simulations: "+GameManager.simulationCounter,skin);
        if(_gameManager.getMode()==3){
            controls = new Label("Controls: \nG - simple GA \nS - shortest MazeBot \nA - advanced MazeBot",skin);
        }
        else{
                controls = new Label("Controls: \nInput velocity with the \nmouse or  press I",skin);
        }
        ButtonGroup buttonGroup = new ButtonGroup(_splineEdit, _changeBallPos, _changeHolePos,_addObjects,_eraseObject);
        //next set the max and min amount to be checked
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(0);
        buttonGroup.uncheckAll();
      //  table.top();
        table.setDebug(false);
        table.add(maxSpeed).colspan(2);
        table.row();
        table.add(speedText).colspan(2);
        table.row();
        table.add(ball_position).colspan(2);
        table.row();
        table.add(turnCount).colspan(2);
        table.row();
        table.add(playerSelectBox).colspan(2);
        table.row();
        table.add(simulationCounter).colspan(2);
        table.row();
        table.add(controls).colspan(2);
        if(_spline) {
            table.row();
            table.add(saveSplines).colspan(2);
            table.row();
            table.add(_splineEdit).colspan(2);
            table.row();
            table.add(_changeBallPos).colspan(2);
            table.row();
            table.add(_changeHolePos).colspan(2);
            table.row();
            table.add(_addObjects).colspan(2);
            table.row();
            table.add(_eraseObject).colspan(2);
            table.row();
            table.add(obstacleDim).colspan(2);
            table.row();
            table.add(_keepRatio).colspan(2);
            table.row();
            table.add(_widthObstacleLabel).align(Align.right);;
            table.add(_widthObstacle).align(Align.left);;
            table.row();
            table.add(_deepObstacleLabel).align(Align.right);
            table.add(_deepObstacle).align(Align.left);
            table.row();
            table.add(_heightObstacleLabel).align(Align.right);;
            table.add(_heightObstacle).align(Align.left);;

        }

        UIStage.addActor(table);
        UIStage.addActor(_shootBar);
    }
    private void updatePlayerActive(){
        Graphics3DComponent graphBall = (Graphics3DComponent)_activaBall.getGraphicComponent();
        graphBall.setColor(Color.WHITE);
        _activaBall = _gameManager.getPlayer(playerSelectBox.getSelectedIndex());
        Graphics3DComponent graphBall2 = (Graphics3DComponent)_activaBall.getGraphicComponent();
        graphBall2.setColor(Color.PURPLE);
 //       System.out.println("test");
    }
    private void checkIfNextPlayerTurn(){
       int playerIndexManager = _gameManager.getActivePlayerIndex();
       if(playerIndexManager != _indexActivePlayerGameManager && _activaBall.isMoving() == false)
       {
           _indexActivePlayerGameManager = playerIndexManager;
           _activaBall = _gameManager.getBall();
           updatePlayerActive();
           playerSelectBox.setSelectedIndex(_indexActivePlayerGameManager);
       }
    }
    public InputProcessor getUIInputProcessor()
    {
        return UIStage;
    }
    private void updateSliders(){
        if(_widthObstacle.isDragging()) {
            _widthObstacleLabel.setText("Width: " + _widthObstacle.getValue());
            if(_keepRatio.isChecked())
                updateSlidersWithRatio(_widthObstacle.getValue());
        }
        if(_deepObstacle.isDragging()){
            _deepObstacleLabel.setText("Deep: "+ _deepObstacle.getValue());
            if(_keepRatio.isChecked())
                updateSlidersWithRatio(_deepObstacle.getValue());
        }
        if(_heightObstacle.isDragging()) {
            _heightObstacleLabel.setText("Height: " + _heightObstacle.getValue());
            if(_keepRatio.isChecked())
                updateSlidersWithRatio(_heightObstacle.getValue());
        }
    }
    public Vector3 getObstacleDimensions(){
        return new Vector3(_widthObstacle.getValue(),_deepObstacle.getValue(),_heightObstacle.getValue());
    }
    private void updateSlidersWithRatio(float val){
        _widthObstacle.setValue(val);
        _widthObstacleLabel.setText("Width: " + _widthObstacle.getValue());
        _deepObstacle.setValue(val);
        _deepObstacleLabel.setText("Deep: "+ _deepObstacle.getValue());
        _heightObstacle.setValue(val);
        _heightObstacleLabel.setText("Height: " + _heightObstacle.getValue());

    }
    public void render()
    {
        Ball ball =_gameManager.getPlayer(playerSelectBox.getSelectedIndex());

        maxSpeed.setText("Max speed: " + CourseManager.getMaxSpeed());
        speedText.setText("Speed: " + (int) (_gameManager.getBall().getVelocity().getSpeed()));
        int x = (int)ball.getPosition().x;
        int y = (int)ball.getPosition().y;
        int height = (int)CourseManager.calculateHeight(x,y);
        ball_position.setText("Ball Position\n" + "height: " + height + "\nx:" + x + " y: " + y);
        turnCount.setText("Turns: " + _gameManager.getTurns());
        simulationCounter.setText("Nr of simulations: "+ GameManager.simulationCounter);
        checkIfNextPlayerTurn();
        if(_spline) updateSliders();
        UIStage.draw();
        UIStage.act();

    }
    public boolean isSplineEditActive()
    {
        return _splineEdit.isChecked();
    }
    public boolean isChangeBallActive() { return _changeBallPos.isChecked(); }
    public boolean isChangeHoleActive()
    {
        return _changeHolePos.isChecked();
    }
    public boolean isAddObjectsActive()
    {
        return _addObjects.isChecked();
    }
    public int getActiveBall(){
        return playerSelectBox.getSelectedIndex();
    }
    public boolean isEraseObjectsActive(){ return  _eraseObject.isChecked();}
}
