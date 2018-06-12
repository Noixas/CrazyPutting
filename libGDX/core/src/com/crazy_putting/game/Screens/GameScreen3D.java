package com.crazy_putting.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.crazy_putting.game.FormulaParser.FormulaParser;
import com.crazy_putting.game.GameLogic.GameManager;
import com.crazy_putting.game.GameLogic.GraphicsManager;
import com.crazy_putting.game.GameLogic.TerrainEditor;
import com.crazy_putting.game.GameObjects.GUI;

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
    public GameScreen3D(GolfGame pGame, int pMode) {
        _mode = pMode;
        this._game = pGame;
        initCameras();
        initTerrain();
        _gameManager = new GameManager(pGame, pMode);
        _terrainEditor.addObserver(_gameManager);
        _gui = new GUI(_game, _gameManager,  _hudViewport, MenuScreen.Spline3D);
        initInput();
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
            boolean stateSpline =_gui.isSplineEditActive();
            boolean changeBall = _gui.isChangeBallActive();
            boolean changeHole = _gui.isChangeHoleActive();
            _terrainEditor.updateGUIState(stateSpline,changeBall,changeHole);
            if((stateSpline ||changeBall||changeHole)&& !_inputMain.getProcessors().contains(_terrainEditor,true)) {
                     _inputMain.addProcessor(1, _terrainEditor);
          }else
              _inputMain.removeProcessor(_terrainEditor);
       }

        @Override
        public void render(float delta) {
            retrieveGUIState();
            _camController.update();//Input
            _gameManager.update(delta);//Logic
            updateCamera();
            GraphicsManager.render3D(_game.batch3D, _cam3D);
            _hudViewport.apply();
            _gui.render();
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
            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
