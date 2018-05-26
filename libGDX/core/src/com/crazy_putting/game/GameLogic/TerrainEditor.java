package com.crazy_putting.game.GameLogic;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.crazy_putting.game.Components.Graphics3DComponent;
import com.crazy_putting.game.GameObjects.GameObject;
import com.crazy_putting.game.Graphics3D.TerrainGenerator;

import java.util.ArrayList;
import java.util.List;

public class TerrainEditor extends InputAdapter {

    private ModelInstance _terrainInstance;
    //3D dragging
    private int _selected = -1, _selecting = -1;
    private Camera _cam3D;
    private boolean _splineEdit = false;
    private Vector2 _buttonDownCoord = new Vector2();
    private Vector2 _buttonDragCoord = new Vector2();
    private boolean _dragging = false;
    private List<GameObject> _splinePoints = new ArrayList<GameObject>();

    public TerrainEditor(Camera pCam3D)
    {
        _cam3D = pCam3D;
        GameObject terrain = new GameObject();
        Graphics3DComponent terrainGraphics = new Graphics3DComponent(TerrainGenerator.generateModelTerrain());
        terrain.addGraphicComponent(terrainGraphics);
        _terrainInstance = terrainGraphics.getInstance();
        Vector2 terrainSize = TerrainGenerator.getTerrainSize();
        createSplinePoints(terrainSize,4);

    }
    private void createSplinePoints(Vector2 pTerrainSize, int pAmountPerSize){
        int dist2PointX = (int) pTerrainSize.x/(pAmountPerSize -1) ;
        int dist2PointY = (int) pTerrainSize.y/(pAmountPerSize -1);
        for(int i = 0; i < pAmountPerSize; i++)
            for(int j = 0; j < pAmountPerSize; j++)
            {
                Vector3 pos = getClosestVertex(new Vector3(dist2PointX*j - (int)(pTerrainSize.x/2), 20000,dist2PointY*i - (int)(pTerrainSize.y/2)));
                createControlPoint(pos);
                System.out.println("POINT CREATED AT: "+pos);
            }
    }
    private void createControlPoint(Vector3 pPos){
        Vector3 cache = new Vector3(pPos);
        pPos.z = cache.y;
        pPos.y = cache.z;
        GameObject point = new GameObject(pPos);
        Graphics3DComponent pointGraphics = new Graphics3DComponent(2);
        point.addGraphicComponent(pointGraphics);
        _splinePoints.add(point);

    }
    public void setSplineEditActive(boolean pActive){
        _splineEdit = pActive;
    }
    private Vector3 getClosestVertex(Vector3 pos){
        Vector3 intersection = new Vector3();
        Ray ray = new Ray(pos,new Vector3(0,-1,0));
        boolean found = Intersector.intersectRayTriangles(ray, TerrainGenerator.triangleList,intersection);
        System.out.println(found);
        return  intersection;
    }
    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
       if(_splineEdit == false) return false;

        _dragging = false;
        _buttonDownCoord.set(screenX,screenY);
        return _selecting >= 0;
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        if(_splineEdit == false) return false;
        _buttonDragCoord.set(screenX,screenY);
        if(_buttonDragCoord.dst2(_buttonDownCoord) > 2)
            _dragging = true;
        return _selecting >= 0;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if(_splineEdit == false) return false;
        if (!_dragging) {
            _selecting = getObject(screenX, screenY);
            return true;
        }
        System.out.println(screenX);
        return false;
    }
    public int getObject (int screenX, int screenY) {
        Ray ray = _cam3D.getPickRay(screenX, screenY,0,0, _cam3D.viewportWidth,_cam3D.viewportHeight);//TODO:Get the WindowsWidth -300 from a constant variable somewhere in graphics, dont hardcode
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
            for (int i = 0; i<1; i++)System.out.println("Intersection point "+intersectPos);
            Vector3 cachePos = new Vector3(intersectPos);
            intersectPos.y = cachePos.z;
            intersectPos.z = cachePos.y;
            GameObject newTest = new GameObject(intersectPos);
            newTest.addGraphicComponent(new Graphics3DComponent(2));
            //  newTest.setPosition(new Vector3(intersectPos.x,intersectPos.z));
        }

        return result;
    }
}
