package com.crazy_putting.game.GameLogic;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.crazy_putting.game.Components.Graphics3DComponent;
import com.crazy_putting.game.GameLogic.Splines.BiCubicSpline;
import com.crazy_putting.game.GameLogic.Splines.SplineInfo;
import com.crazy_putting.game.GameObjects.GameObject;
import com.crazy_putting.game.GameObjects.SplinePoint;
import com.crazy_putting.game.Graphics3D.TerrainGenerator;

import java.util.List;

public class TerrainEditor extends InputAdapter {

    private ModelInstance _terrainInstance;
    //3D dragging
    private int _selected = -1, _selecting = -1;
    private Camera _cam3D;
    private boolean _splineEdit = false;
    private boolean _changeBall = false;
    private boolean _changeHole = false;
    private Vector2 _buttonDownCoord = new Vector2();
    private Vector2 _buttonDragCoord = new Vector2();
    private boolean _dragging = false;
    private SplinePoint[][]  _splinePoints;
    private float _sPointRadius = 40f;
    private GameObject _draggingPoint;
    private boolean _splineEnabled;
    boolean print = false;
    private GameManager _observer;
    double[][] p = {{-50},{50},{50},{50},
            {0},{0},{0},{0},
            {0},{0},{0},{0},
            {0},{0},{0},{0}};
    public TerrainEditor(Camera pCam3D, boolean pSplines) {
        _splineEnabled = pSplines;
        if(pSplines) {
        _cam3D = pCam3D;
        GameObject terrain = new GameObject();
        Graphics3DComponent terrainGraphics = new Graphics3DComponent(TerrainGenerator.generateModelTerrain(true, p));
        terrain.addGraphicComponent(terrainGraphics);
        _terrainInstance = terrainGraphics.getInstance();
        Vector2 terrainSize = TerrainGenerator.getTerrainSize();
       // createSplinePoints(terrainSize, p)
            BiCubicSpline spline = TerrainGenerator.getSpline();
           _splinePoints = spline.getSplinePoints();
        CourseManager.setBiCubicSpline(spline);//change CourseMAnager to use splines instead of formula height
    }  else {
        _cam3D = pCam3D;
        GameObject terrain = new GameObject();
        Graphics3DComponent terrainGraphics = new Graphics3DComponent(TerrainGenerator.generateModelTerrain());
        terrain.addGraphicComponent(terrainGraphics);
        _terrainInstance = terrainGraphics.getInstance();
        Vector2 terrainSize = TerrainGenerator.getTerrainSize();
        //createSplinePoints(terrainSize, 4);
    }

    }
    public void addObserver(GameManager pObserver) {
        _observer = pObserver;
    }
    public void updateGUIState(boolean pSpline, boolean ball, boolean hole){
        if(_splineEnabled) {
            _splineEdit = pSpline;
            showSplinePoints();
            _changeBall = ball;
            _changeHole = hole;

            System.out.println("Ball "+ball);
            System.out.println("Hole "+hole);
            System.out.println("Edit "+pSpline);
            System.out.println();
        }

    }
    private void showSplinePoints(){
        for (int i = 0; i < _splinePoints.length;i++) {
            for (int j = 0; j < _splinePoints[0].length;j++) {
                _splinePoints[i][j].enabled = _splineEdit;
            }}
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

        System.out.println(_changeBall);
       if(_splineEdit) {
           System.out.println("SplineEdit");

           _dragging = false;
        _draggingPoint = intersectSplinePoint(screenX,screenY);
        _buttonDownCoord.set(screenX,screenY);
        if(_draggingPoint != null) {
           Graphics3DComponent gp = (Graphics3DComponent)_draggingPoint.getGraphicComponent();
           gp.setColor(3);
          // printArray(p);
            System.out.println(_draggingPoint.getPosition());
           return true;
        }
       }else if(_changeBall){
           System.out.println("Ball");
            Vector3 pos = getObject(screenX,screenY);
            changeBallPos(pos);
       }
       else if(_changeHole){
           System.out.println("Hole");
           Vector3 pos = getObject(screenX,screenY);
           changeHolePos(pos);
       }
        //_screen3D.setCamControllerEnabled(false);
        return _selecting >= 0;
    }
    public boolean isDragging(){
        return _dragging;
    }
    private void changeBallPos(Vector3 pPos){
        if(pPos == null) return;;
        Vector3 cachePos = new Vector3(pPos);
        pPos.y = cachePos.z;
        pPos.z = cachePos.y;
        _observer.updateBallPos(pPos);
    }
    private void changeHolePos(Vector3 pPos){
        if(pPos == null) return;;
        Vector3 cachePos = new Vector3(pPos);
        pPos.y = cachePos.z;
        pPos.z = cachePos.y;
        _observer.updateHolePos(pPos);
    }
    private SplinePoint intersectSplinePoint(int screenX, int screenY ){
        Ray ray = _cam3D.getPickRay(screenX,screenY,0,0, _cam3D.viewportWidth,_cam3D.viewportHeight);//TODO:Get the WindowsWidth -300 from a constant variable somewhere in graphics, dont hardcode
            Vector3 intersect = new Vector3();
        for (int i = 0; i < _splinePoints.length;i++) {
            for (int j = 0; j < _splinePoints[0].length;j++) {
                Vector3 pos = new Vector3(_splinePoints[i][j].getPosition());
            swapYandZ(pos);
            boolean found = Intersector.intersectRaySphere(ray, pos, _sPointRadius,intersect);
            if(found) return _splinePoints[i][j];
        }}
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
        if(_dragging) updateDraggingPoint(prevDraggingPos);
        return _dragging;
    }
    private void updateDraggingPoint(Vector2 prevDraggingPos){
       // _draggingPoint.getPosition().z +=  prevDraggingPos.y - _buttonDragCoord.y;
        SplinePoint spl = (SplinePoint)_draggingPoint;
        spl.sstHeight(_draggingPoint.getPosition().z +  prevDraggingPos.y - _buttonDragCoord.y);
        //change from here on
       List<SplineInfo> owners = spl.getSplineOwners();
       BiCubicSpline spline = TerrainGenerator.getSpline();
        for(SplineInfo sp: spline.getSplineList())
        {
            spline.updateSplineCoeff(sp);//TODO:ERROR HERE, checked, no error
        }
        System.out.println("HEIGHT OF DRAGG"+ (((SplinePoint) _draggingPoint).getSplineHeight()));

        // Vector2 index = spl.getIndex();
        //p[(int)index.y][(int)index.x] = _draggingPoint.getPosition().z/10;
        updateTerrain();
    }
    private void updateTerrain(){//TODO: error here
        //CachedBicubicInterpolator spline = TerrainGenerator.updateTerrain(p);
      //  System.out.println("TERRAIN NPODESSS" + _terrainInstance.nodes.size);
        BiCubicSpline spline = TerrainGenerator.getSpline();
        List<SplineInfo> infoList = spline.getSplineList();
        for(int i = 0; i < infoList.size(); i++) {
            Mesh changinMesh = infoList.get(i).getNode().parts.get(0).meshPart.mesh;
            float[] vertices = new float[changinMesh.getNumVertices() * changinMesh.getVertexSize() / 4 ];
            vertices = changinMesh.getVertices(vertices);

            updateVertices(vertices, spline,i, infoList.get(i));
            changinMesh.updateVertices(0,vertices);
        }
        print = true;


    }

    private void updateVertices(float [] vert, BiCubicSpline spline,int pNode,SplineInfo info){
        float perc = 1.0f*0.0005f;
        List<Vector3> triangles = TerrainGenerator.triangleList;
        int count = 0;
        int offset =  pNode*9600;

      //  System.out.println(pNode);
    //    System.out.println(vert.length);
        for(int i = 0; i <14; i ++) {
            System.out.println(vert[i]);
        }
        for(int i = 1; i < vert.length; i +=7)
        {
            boolean aboveWater = vert[i]>=-1;
            vert[i] = spline.getHeightAt(new Vector2(vert[i-1],vert[i+1]));
            if(vert[i] < -1 && aboveWater)
                vert[i+2] = Color.toFloatBits(Color.BLUE.r,Color.BLUE.g,Color.BLUE.b,Color.BLUE.a);
            else if(aboveWater == false && vert[i]>=-1)
                vert[i+2] = Color.toFloatBits(Color.GREEN.r,Color.GREEN.g,Color.GREEN.b,Color.GREEN.a);
            triangles.get(count + offset).y = vert[i];
            count++;
        }

    }

//    private void updateControlPoints(){
//        for(int i = 0; i < p.length; i++)
//            for(int j = 0; j < p.length; j++)
//            {
//             //   GameObject sp = _splinePoints.get(i*p.length +j);
//                if(sp.equals(_draggingPoint) == false) {
//                   swapYandZ(sp.getPosition());
//                    sp.getPosition().y = 100000;
//                    Vector3 intersection = new Vector3();
//                    Ray ray = new Ray(sp.getPosition(), new Vector3(0, -1, 0));
//                    boolean found = Intersector.intersectRayTriangles(ray, TerrainGenerator.triangleList, intersection);
//                    //if(found == false)
//                       // System.out.println(found);
//               //     swapYandZ(intersection);
//
//                    sp.getPosition().y = intersection.y;
//                    swapYandZ(sp.getPosition());
//                   // sp.updateHeight();
//
//                }
//                ;
//
//
//                   // System.out.println("POINT CREATED AT: "+pos);
//            }
//
//    }
    //TEMPORAL HELPER CLASS
    private void printArray(float[] array)
    {
        for(int i = 0; i<array.length; i++)
        {
            System.out.println(array[i]);
        }
    }
    private void printArray(List<GameObject> array)
    {
        for(int i = 0; i<array.size(); i++)
        {
            System.out.println(array.get(i));
        }
    }
    private void printArray(double[][] array)
    {
        String out = "\n";
        for(int i = 0; i<array.length; i++)
        {for(int j = 0; j<array.length; j++)
        {
            out += array[i][j]+"  ";
        }
        out+="\n";
        }
        System.out.println(out);
    }
    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if(_splineEdit == false) return false;
        if(_dragging){
                Graphics3DComponent gp = (Graphics3DComponent)_draggingPoint.getGraphicComponent();
            System.out.println(_draggingPoint.getPosition());
                gp.setColor(3);
               // printArray(p);
                _observer.updateObjectPos();
                return true;
            }
        //    _screen3D.setCamControllerEnabled(true);

        return false;
    }
    public Vector3 getObject (int screenX, int screenY) {
        Ray ray = _cam3D.getPickRay(screenX, screenY,0,0, _cam3D.viewportWidth,_cam3D.viewportHeight);//TODO:Get the WindowsWidth -300 from a constant variable somewhere in graphics, dont hardcode
        int result = -1;
        float distance = -1;
        Vector3 position = new Vector3();
        _terrainInstance.transform.getTranslation(position);
        // position.add(_terrainInstance.transform.);
        float dist2 = ray.origin.dst2(position);
//        Mesh aMesh = _terrainInstance.getNode(TerrainGenerator.testMeshCode).parts.get(0).meshPart.mesh;
//        //  if (distance >= 0f && dist2 > distance) continue;
//        short[] indices = new short[aMesh.getNumIndices()];
//        aMesh.getIndices(indices);
//        // float[] vert = new float[aMesh.getNumVertices()*aMesh.f];//Important to multiply it against the vertex size and divide by 4
//        //   aMesh.getVertices(vert);
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

            System.out.println("Intersection point "+intersectPos);
//            Vector3 cachePos = new Vector3(intersectPos);
//            intersectPos.y = cachePos.z;
//            intersectPos.z = cachePos.y;
//            GameObject newTest = new GameObject(intersectPos);
//            newTest.addGraphicComponent(new Graphics3DComponent(2));
            //  newTest.setPosition(new Vector3(intersectPos.x,intersectPos.z));
            return intersectPos;
        }

        return null;
    }
    public static void swapYandZ(Vector3 vec){
        Vector3 cache = new Vector3(vec);
        vec.z = cache.y;
        vec.y = cache.z;
    }
}
