package com.crazy_putting.game.Graphics3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

/**
 * See: http://blog.xoppa.com/basic-3d-using-libgdx-2/
 * @author Xoppa
 */
public class Basic3DTest implements Screen {
    public Environment environment;
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public Model model;
    public Model model2;
    public ModelInstance instance;
    public  ModelInstance instance2;
    public ModelInstance axisInstance;
    public boolean debugAxis = true;
    public float[] vertices;
    public Mesh changinMesh;



    public Basic3DTest() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
       cam.position.add( new Vector3(0, 200, 200));
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 15000f;
        cam.update();

        model = TerrainGenerator.generateModelTerrain();
        instance = new ModelInstance(model);

        ModelBuilder modelBuilder2 = new ModelBuilder();
        model2 = modelBuilder2.createBox(10f, 10f, 10f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                Usage.Position | Usage.Normal);

        instance2 = new ModelInstance(model2);
        instance2.transform.translate(100,100,0);

        axisInstance=new ModelInstance( DebugAxesGenerator.generateAxes());
        camController = new CameraInputController(cam);
        camController.translateUnits = 50;

        Gdx.input.setInputProcessor(camController);
    }
@Override
public void show(){

}
    @Override
    public void render(float delta) {
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            String code ="node i: 0 j 0";
        modelBatch.begin(cam);
        if(false && Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            changinMesh =  instance.getNode(code).parts.get(0).meshPart.mesh;
            vertices = new float[changinMesh.getNumVertices() * changinMesh.getVertexSize() / 4];
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.V))//This works!!
        {
            changinMesh = instance.getNode(TerrainGenerator.testMeshCode).parts.get(0).meshPart.mesh;
          //  System.out.println(changinMesh.getNumVertices());
            vertices = new float[changinMesh.getNumVertices() * changinMesh.getVertexSize() / 4];
            vertices = changinMesh.getVertices(vertices);
              printArray(vertices);
              vertices[1] = vertices[1]+100; //Test making one vertex y higher so is visible the change
            changinMesh.updateVertices(0,vertices);

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.P))
        {
            //System.out.println("Camera pos = " + cam.position);
        }


    //       instance.getNode(code).parts.get(0).meshPart.mesh.updateVertices(10,);
    //    TerrainGenerator.zeroPos.position.y += .1f;
        modelBatch.render(instance, environment);
        modelBatch.render(instance2, environment);
        if(debugAxis)
        modelBatch.render(axisInstance, environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        model.dispose();
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
    private void printArray(float[] array)
    {
        for(int i = 0; i<array.length; i++)
        {
            //System.out.println(array[i]);
        }
    }

}