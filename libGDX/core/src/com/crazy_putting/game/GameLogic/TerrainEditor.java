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
import com.crazy_putting.game.Screens.GameScreen3D;

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
    private float _sPointRadius = 40f;
    private GameObject _draggingPoint;

    private GameScreen3D _screen3D;
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
        swapYandZ(pPos);
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
        _draggingPoint = intersectSplinePoint(screenX,screenY);
        _buttonDownCoord.set(screenX,screenY);
        if(_draggingPoint != null) {
           Graphics3DComponent gp = (Graphics3DComponent)_draggingPoint.getGraphicComponent();
           gp.setColor(3);
           return true;
        }
        //_screen3D.setCamControllerEnabled(false);
        return _selecting >= 0;
    }
    public boolean isDragging(){
        return _dragging;
    }
    private GameObject intersectSplinePoint(int screenX, int screenY ){
        Ray ray = _cam3D.getPickRay(screenX,screenY,0,0, _cam3D.viewportWidth,_cam3D.viewportHeight);//TODO:Get the WindowsWidth -300 from a constant variable somewhere in graphics, dont hardcode
            Vector3 intersect = new Vector3();
        for (GameObject point : _splinePoints) {
            Vector3 pos = new Vector3(point.getPosition());
            swapYandZ(pos);
            boolean found = Intersector.intersectRaySphere(ray, pos, _sPointRadius,intersect);
            if(found) return point;
        }
        return null;

    }
    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        if(_splineEdit == false ) return false;
        Vector2 prevDraggingPos;
        if(_dragging) prevDraggingPos = new Vector2(_buttonDragCoord); //If is first frame of drag then prev distance is 0
        else prevDraggingPos = new Vector2(screenX,screenY);

        _buttonDragCoord.set(screenX,screenY);
        float dist =_buttonDragCoord.dst2(_buttonDownCoord);
        if( dist > 2  && _draggingPoint != null)
            _dragging = true;
        if(_dragging)
        _draggingPoint.getPosition().z +=  prevDraggingPos.y - _buttonDragCoord.y;
        return _dragging;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if(_splineEdit == false) return false;
        if(_dragging){
                Graphics3DComponent gp = (Graphics3DComponent)_draggingPoint.getGraphicComponent();
                gp.setColor(3);
                return true;
            }
        //    _screen3D.setCamControllerEnabled(true);
        if (!_dragging && false) {
            _selecting = getObject(screenX, screenY);
            return true;
        }
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
    public static void swapYandZ(Vector3 vec){
        Vector3 cache = new Vector3(vec);
        vec.z = cache.y;
        vec.y = cache.z;
    }
    public void addObserver(GameScreen3D scr) {
    _screen3D = scr;
    }
}
