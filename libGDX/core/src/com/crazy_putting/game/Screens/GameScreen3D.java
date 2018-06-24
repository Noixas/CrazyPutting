package com.crazy_putting.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.crazy_putting.game.Components.Graphics.ArrowGraphics3DComponent;
import com.crazy_putting.game.FormulaParser.FormulaParser;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameLogic.GameManager;
import com.crazy_putting.game.GameLogic.GraphicsManager;
import com.crazy_putting.game.GameLogic.TerrainEditor;
import com.crazy_putting.game.GameObjects.GUI;
import com.crazy_putting.game.GameObjects.GameObject;

/*
Handles the graphics of the in-Game screen, which is the 3D cam and 2D cam for the GUI and the tools to control the 3D environment
 */
public class GameScreen3D extends InputAdapter implements Screen {
        final GolfGame _game;
        private FormulaParser _parser;
        private GameManager _gameManager;
        private Camera _cam3D;
        private Camera _cam2D;
        private GUI _gui;
        public static int Width3DScreen;
        public static int Height3DScreen;
        public static int Width2DScreen;
        public static int Height2DScreen;
        private FitViewport  _hudViewport;
        private TerrainEditor _terrainEditor;
        private InputMultiplexer _inputMain;
        private int _mode;
        private CameraInputController _camController;
        private float _speedCache;
        private boolean _speedPressing = false;
        private float _maxShootSpeed = CourseManager.getMaxSpeed();
        private boolean _increaseSpeedBar = true;
        private GameObject _shootArrow;
    public GameScreen3D(GolfGame pGame, int pMode) {
        _mode = pMode;
        this._game = pGame;
        initCameras();
        initTerrain();
        _gameManager = new GameManager(pGame, pMode);
        _terrainEditor.addObserver(_gameManager);
        _gui = new GUI(_game, _gameManager,  _hudViewport, MenuScreen.Spline3D);
        _terrainEditor.setGUI(_gui);
        initInput();
        Gdx.graphics.setVSync(true);

    }
    private void initCameras(){
        Width2DScreen = 300;
        Width3DScreen = Gdx.graphics.getWidth() - Width2DScreen;
        Height2DScreen = Height3DScreen = GraphicsManager.WINDOW_HEIGHT;
        _cam2D = new OrthographicCamera();
        _hudViewport = new FitViewport(Width2DScreen, Height2DScreen,_cam2D);
        _cam2D.update();
        _parser = new FormulaParser();

        //3D
        _cam3D = new PerspectiveCamera(67,Width3DScreen, Height2DScreen);
        _cam3D.position.add( new Vector3(0, 1300, 0));
        _cam3D.lookAt(0,0,0);
        _cam3D.near = 1f;
        _cam3D.far = 15000f;
        _cam3D.update();
        _camController = new CameraInputController(_cam3D);
        _camController.translateUnits = 50;
    }
    private void initTerrain() {
            if(MenuScreen.Spline3D)_terrainEditor = new TerrainEditor(_cam3D,true);
            else _terrainEditor = new TerrainEditor(_cam3D,false);

    }
    private void initInput(){
        _inputMain = new InputMultiplexer(this);
       _inputMain.addProcessor(_gui.getUIInputProcessor());
        _inputMain.addProcessor(_camController);
        Gdx.input.setInputProcessor(_inputMain);
    }
        private void retrieveGUIState(){

            if(checkGUIActive()&& !_inputMain.getProcessors().contains(_terrainEditor,true)) {
                     _inputMain.addProcessor(0, _terrainEditor);
          }else if(!checkGUIActive()&& _inputMain.getProcessors().contains(_terrainEditor,true))
              _inputMain.removeProcessor(_terrainEditor);

       }
       private boolean checkGUIActive(){
           boolean stateSpline =_gui.isSplineEditActive();
           boolean changeBall = _gui.isChangeBallActive();
           boolean changeHole = _gui.isChangeHoleActive();
           boolean addObject = _gui.isAddObjectsActive();
           boolean eraseObject = _gui.isEraseObjectsActive();
           _terrainEditor.updateGUIState(stateSpline,changeBall,changeHole,addObject, eraseObject);
        return (stateSpline ||changeBall||changeHole||addObject ||eraseObject);
       }
    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {

        if(checkGUIActive())return false;
        Vector3 pos =_terrainEditor.getObject(screenX,screenY);
        if(pos == null) return false;
        _speedCache=0;
        _speedPressing = true;
        Vector3 playerPos =  _gameManager.getPlayer(_gameManager.getActivePlayerIndex()).getPosition();
        System.out.println("Ball Pos "+playerPos);
        _shootArrow = new GameObject((new Vector3(playerPos)));

        float hightOffset = 2;
        Vector3 cache = new Vector3(_terrainEditor.getObject(screenX,screenY));
        pos.y=cache.z;
        pos.z=cache.y+hightOffset;
     //   GameObject arrow = new GameObject();
        Vector3 to =_terrainEditor.getObject(screenX,screenY);
        int radius = 20;
        to.y = playerPos.z + radius;
        System.out.println("ARROW POS "+_shootArrow.getPosition());
        ArrowGraphics3DComponent g = new ArrowGraphics3DComponent(new Vector3(_gameManager.getPlayer(_gameManager.getActivePlayerIndex()).getPosition()),to, Color.DARK_GRAY);
       // SphereGraphics3DComponent g = new SphereGraphics3DComponent(40,40,2,Color.RED);
        _shootArrow.addGraphicComponent(g);
        return false;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        System.out.println("Pressed speed "+_speedCache);
      if(_speedPressing){
        _gui.addShootBar(-100);
        _shootArrow.destroy();
        Vector3 playerPos = _gameManager.getPlayer(_gameManager.getActivePlayerIndex()).getPosition();
        //System.out.println(playerPos);
        Vector3 dirShot = _terrainEditor.getObject(screenX,screenY);
        swapYZ(dirShot);
        System.out.println("dir shot"+dirShot);
        Vector2 pos2 = new Vector2(playerPos.x,playerPos.y);
        Vector2 dir2 = new Vector2(dirShot.x,dirShot.y);
        System.out.println("play2 "+pos2);
        System.out.println("dir2 "+dir2);
        float distance = dir2.dst(pos2);
        float initialAngle = (float) Math.toDegrees(Math.acos(Math.abs(pos2.x-dir2.x)/distance));
        float angle = 0;

          if(pos2.x<dir2.x && pos2.y<dir2.y){
              angle = initialAngle;
          }
          else if(pos2.x>dir2.x&&pos2.y<dir2.y){
              angle = 180-initialAngle;
          }
          else if(pos2.x>dir2.x&&pos2.y>dir2.y){
              angle = 180+initialAngle;
          }
          else if(pos2.x<dir2.x&&pos2.y>dir2.y){
              angle = 360-initialAngle;
          }
          if(_increaseSpeedBar==false){
              angle+=180;
          }
        float[][] input = new float[_gameManager.getAmountPlayers()][2];
        for(int i = 0; i < _gameManager.getAmountPlayers(); i++){
            input[i][0] = _speedCache;
            input[i][1] = angle;
        }
        _gameManager.checkConstrainsAndSetVelocity(input);
      }
        _speedPressing =false;
          return false;

    }
    private void swapYZ(Vector3 v){
        Vector3 cache = new Vector3(v);
        v.y=cache.z;
        v.z=cache.y;
    }
        @Override
        public void render(float delta) {
        if(_speedPressing){
            handleShootSpeed();


        }
            retrieveGUIState();
            _camController.update();//Input
            _gameManager.update(delta);//Logic
            updateCamera();
            GraphicsManager.render3D(_game.batch3D, _cam3D);
            _hudViewport.apply();
            _gui.render();
        }
        private void handleShootSpeed(){
            System.out.println("toolbar" + _increaseSpeedBar);
            float step = _maxShootSpeed / 100;
            System.out.println(step);
        if(_increaseSpeedBar){
            if(_speedCache + step < _maxShootSpeed) {
                _speedCache += step;
                _gui.addShootBar(+1);
            }
        }
        else if(_increaseSpeedBar == false){
            _speedCache-=step;
            _gui.addShootBar(-1);
        }
        if(_speedCache >= _maxShootSpeed || _speedCache == 0) {
            boolean currentState = _increaseSpeedBar;
            _increaseSpeedBar = !currentState;
        }
}
        private void updateCamera()
        {
            _cam3D.update();
            _cam2D.update();
            _game.batch.setProjectionMatrix(_cam2D.combined);

            int red = 66;
            int green = 134;
            int blue = 244;
            Gdx.gl.glClearColor((float)(red/255.0), (float)(green/255.0), (float)(blue/255.0), 1);
           // Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        @Override
        public void resize(int width, int height) {
           // _hudViewport.update(width,height);
           // _cam3D.viewportWidth = width*2f;
           // _cam3D.viewportHeight = _cam3D.viewportWidth * height/width;
            _cam3D.update();
        }
        @Override
        public void show() {
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
