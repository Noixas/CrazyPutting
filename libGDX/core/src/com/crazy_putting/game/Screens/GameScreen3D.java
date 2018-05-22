package com.crazy_putting.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.crazy_putting.game.Components.Graphics3DComponent;
import com.crazy_putting.game.FormulaParser.FormulaParser;
import com.crazy_putting.game.GameLogic.GameManager;
import com.crazy_putting.game.GameLogic.GraphicsManager;
import com.crazy_putting.game.GameObjects.GUI;
import com.crazy_putting.game.GameObjects.GameObject;
import com.crazy_putting.game.GameObjects.OldGameObject;
import com.crazy_putting.game.Graphics3D.DebugAxesGenerator;
import com.crazy_putting.game.Graphics3D.TerrainGenerator;

/*
Handles the graphics of the in-Game screen, which is the 3D cam and 2D cam for the GUI and the tools to control the 3D environment
 */
public class GameScreen3D extends InputAdapter implements Screen {
        final GolfGame _game;
        FormulaParser parser;
        private Texture texture;
        private GameManager _gameManager;
        private Camera _cam3D;
        private ModelInstance _terrainInstance;
        private Camera _cam2D;
        private GUI _gui;
        public static int Width3DScreen;
        public static int Height3DScreen;
        public static int Width2DScreen;
        public static int Height2DScreen;
        private FitViewport  HudViewport;

        Model modelBox;
       public static ModelInstance boxInstance;

    //3D dragging
    private int selected = -1, selecting = -1;

    public CameraInputController camController;

        public GameScreen3D(GolfGame pGame, int pMode) {
            Width2DScreen = 300;
            Width3DScreen = Gdx.graphics.getWidth() - Width2DScreen;
            Height2DScreen = Height3DScreen = GraphicsManager.WINDOW_HEIGHT;
            _cam2D = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            HudViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),_cam2D);
            HudViewport.setScreenX(Width3DScreen);

            HudViewport.apply();
            _cam2D.update();
            parser = new FormulaParser();
            this._game = pGame;
            _gameManager = new GameManager(pGame, pMode);

            //3D

            _cam3D = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            _cam3D.position.add( new Vector3(0, 1300, 0));
           // _cam3D.lookAt(_gameManager.getBall().getPosition());
           _cam3D.lookAt(0,0,0);
            _cam3D.near = 1f;
            _cam3D.far = 15000f;
            _cam3D.update();

            camController = new CameraInputController(_cam3D);
            camController.translateUnits = 50;
            InputMultiplexer inputMultiplexer = new InputMultiplexer(this);
            inputMultiplexer.addProcessor(camController);
            CameraInputController control2 = new CameraInputController(_cam2D);
            inputMultiplexer.addProcessor(control2);
            Gdx.input.setInputProcessor(inputMultiplexer);
            GameObject axis = new GameObject();
            axis.addGraphicComponent  (new Graphics3DComponent(DebugAxesGenerator.generateAxes()));
            GameObject terrain = new GameObject();
            Graphics3DComponent terrainGraphics = new Graphics3DComponent(TerrainGenerator.generateModelTerrain());
            terrain.addGraphicComponent(terrainGraphics);
            _terrainInstance = terrainGraphics.getInstance();

            //Testing lightning only
            ModelBuilder modelBuilder = new ModelBuilder();
            modelBox = modelBuilder.createBox(50f, 50f, 50f,
                    new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.ColorPacked);
            //boxInstance = new ModelInstance(modelBox);

            //Where all the 2D text is handled
             _gui = new GUI(_game, _gameManager, _cam2D);

        }
        @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        selecting = getObject(screenX, screenY);
            System.out.println(screenX);
        return selecting >= 0;
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        return selecting >= 0;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if (selecting >= 0) {
            if (selecting == getObject(screenX, screenY))
                //setSelected(selecting);
            selecting = -1;
            return true;
        }
        return false;
    }
    public int getObject (int screenX, int screenY) {
        Ray ray = _cam3D.getPickRay(screenX, screenY,0,0, GraphicsManager.WINDOW_WIDTH-300,_cam3D.viewportHeight);//TODO:Get the WindowsWidth -300 from a constant variable somewhere in graphics, dont hardcode
        int result = -1;
        float distance = -1;
        Vector3 position = new Vector3();
        _terrainInstance.transform.getTranslation(position);
            // position.add(_terrainInstance.transform.);
            float dist2 = ray.origin.dst2(position);
            Mesh aMesh = _terrainInstance.getNode(TerrainGenerator.testMeshCode).parts.get(0).meshPart.mesh;
          //  if (distance >= 0f && dist2 > distance) continue;
            short[] indices = new short[aMesh.getNumIndices()];
            aMesh.getIndices(indices);
           // float[] vert = new float[aMesh.getNumVertices()*aMesh.f];//Important to multiply it against the vertex size and divide by 4
         //   aMesh.getVertices(vert);
            Vector3 intersectPos = new Vector3();
          /*  float[] tri = new float[28800];
            int countt = 0;
            for (int i = 0; i<TerrainGenerator.countTriangles;i++)
            {
                tri[countt] = TerrainGenerator.triangles[i].x;
                countt++;
                tri[countt] = TerrainGenerator.triangles[i].y;
                countt++;
                tri[countt] = TerrainGenerator.triangles[i].z;
                countt++;
            }*/
            if (Intersector.intersectRayTriangles(ray,TerrainGenerator.triangleList,intersectPos)) {
               for (int i = 0; i<10; i++)System.out.println("Intersection point "+intersectPos);
               GameObject newTest = new GameObject(intersectPos);
                newTest.addGraphicComponent(new Graphics3DComponent(2));
             //  newTest.setPosition(new Vector3(intersectPos.x,intersectPos.z));
            }

        return result;
    }

        private void handleInput() {


      /*  if(Gdx.input.isKeyJustPressed(Input.Keys.O)){
            //TODO: reference to InputScreen - Blazej
            //game.setScreen(new InputScreen(game));
            //game.getScreen();
        }
        /*
            Zooming.
         */
      /*  if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam3D.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam3D.zoom -= 0.02;
        }
        /*
            Manipulating the position of the camera.
         */
      /*  if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam3D.translate(-3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam3D.translate(3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam3D.translate(0, -3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam3D.translate(0, 3, 0);
        }

        cam3D.zoom = MathUtils.clamp(cam3D.zoom, 0.1f, Math.min(WORLD_HEIGHT/cam3D.viewportHeight, WORLD_WIDTH/cam3D.viewportWidth));

        float effectiveViewportWidth = cam3D.viewportWidth * cam3D.zoom;
        float effectiveViewportHeight = cam3D.viewportHeight * cam3D.zoom;

        cam3D.position.x = MathUtils.clamp(cam3D.position.x, effectiveViewportWidth / 2f, WORLD_HEIGHT - effectiveViewportWidth / 2f);
        cam3D.position.y = MathUtils.clamp(cam3D.position.y, effectiveViewportHeight / 2f, WORLD_WIDTH - effectiveViewportHeight / 2f);
    */}
        @Override
        public void render(float delta) {

            camController.update();
            _gameManager.Update(delta);
            updateCamera();
            _game.batch3D.begin(_cam3D);

            GraphicsManager.render3D(_game.batch3D);
            _game.batch3D.end();


            HudViewport.apply();
            _game.batch.begin();
                if(false)//Dont render 2D Graphics(?) by Rodrigo, check if is not called somewhere else
                     GraphicsManager.render2D(_game.batch);
             _gui.render();
            _game.batch.end();
        }
        private void updateCamera()
        {
          //  _cam3D.lookAt(_gameManager.getBall().getPosition().x,_gameManager.getBall().getHeight(),_gameManager.getBall().getPosition().y);
            handleInput();
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
            HudViewport.update(width,height);
            //_cam3D.viewportWidth = width*2f;
            //_cam3D.viewportHeight = _cam3D.viewportWidth * height/width;
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
