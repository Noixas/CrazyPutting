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
import com.crazy_putting.game.GameObjects.SplinePoint;
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
    private float _sPointRadius = 40f;
    private GameObject _draggingPoint;
    private boolean _splineEnabled;

//    double[][] p = {{-10,90,90,0},
//            {0,20,70,0},
//            {0,60,30,0},
//            {-10,-57,70,0}};
//double[][] p = {{0,0,0,0},
//        {90,90,0,0},
//        {250,250,0,0},
//        {160,160,0,0}};
//double[][] p = {{-50,50,-50,50},
//        {0,0,0,0},
//        {0,0,0,0},
//        {0,0,0,0}};
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
        createSplinePoints(terrainSize, p);
    }  else {
        _cam3D = pCam3D;
        GameObject terrain = new GameObject();
        Graphics3DComponent terrainGraphics = new Graphics3DComponent(TerrainGenerator.generateModelTerrain());
        terrain.addGraphicComponent(terrainGraphics);
        _terrainInstance = terrainGraphics.getInstance();
        Vector2 terrainSize = TerrainGenerator.getTerrainSize();
        createSplinePoints(terrainSize, 4);
    }

    }
    /*
    Create spline points uniformly on the prev generated terrain
     */
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
    /*
        Create spline points based on the given points
    */
    private void createSplinePoints(Vector2 pTerrainSize, double[][] pHeight){
        double[][] newP = {{-50,50},{-50,50}};
        int dist2PointX = (int) pTerrainSize.x/(newP.length -1) ;
        int dist2PointY = (int) pTerrainSize.y/(newP[0].length -1);
        for(int i = 0; i < newP.length; i++)
            for(int j = 0; j < newP.length; j++)
            {
                Vector3 pos = getClosestVertex(new Vector3(dist2PointX*j - (int)(pTerrainSize.x/2), 20000,dist2PointY*i - (int)(pTerrainSize.y/2)));
                createControlPoint(pos,new Vector2(j,i),newP[i][j]);
                System.out.println("POINT CREATED AT: "+pos);
            }
    }
    private void createControlPoint(Vector3 pPos,Vector2 pIndex, double pHeight){
        swapYandZ(pPos);
        pPos.z = (float)pHeight*10.0f;
        SplinePoint point = new SplinePoint(pPos, pIndex, pHeight);
        Graphics3DComponent pointGraphics = new Graphics3DComponent(2);
        point.addGraphicComponent(pointGraphics);
        _splinePoints.add(point);

    }private void createControlPoint(Vector3 pPos){
        swapYandZ(pPos);
        SplinePoint point = new SplinePoint(pPos);
        Graphics3DComponent pointGraphics = new Graphics3DComponent(2);
        point.addGraphicComponent(pointGraphics);
        _splinePoints.add(point);

    }
    public void setSplineEditActive(boolean pActive){
        if(_splineEnabled)
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
           printArray(p);
            System.out.println(_draggingPoint.getPosition());
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
        if(_dragging) updateDraggingPoint(prevDraggingPos);
        return _dragging;
    }
    private void updateDraggingPoint(Vector2 prevDraggingPos){
        _draggingPoint.getPosition().z +=  prevDraggingPos.y - _buttonDragCoord.y;
        SplinePoint spl = (SplinePoint)_draggingPoint;
        Vector2 index = spl.getIndex();
        p[(int)index.y][(int)index.x] = _draggingPoint.getPosition().z/10;
        updateTerrain();
    }
    private void updateTerrain(){
        CachedBicubicInterpolator spline = TerrainGenerator.updateTerrain(p);
      //  System.out.println("TERRAIN NPODESSS" + _terrainInstance.nodes.size);
        for(int i = 0; i < _terrainInstance.nodes.size; i++) {
            Mesh changinMesh = _terrainInstance.getNode( "n "+i).parts.get(0).meshPart.mesh;
            //  System.out.println(changinMesh.getNumVertices());
            float[] vertices = new float[changinMesh.getNumVertices() * changinMesh.getVertexSize() / 4 ];
            vertices = changinMesh.getVertices(vertices);
            // printArray(vertices);
            updateVertices(vertices, spline, i);
            changinMesh.updateVertices(0,vertices);
        }

        //updateControlPoints();
       // vertices[1] =(float) p[0][0];
     //   vertices[1] = vertices[1]+100;

    }
    private void updateVertices(float [] vert, CachedBicubicInterpolator spline, int pNode){
        float perc = 1.0f*0.0005f;
        List<Vector3> triangles = TerrainGenerator.triangleList;
        int count = 0;
        int offset =  pNode*9600;

      //  System.out.println(pNode);
    //    System.out.println(vert.length);
        for(int i = 1; i < vert.length; i +=7)
        {
           // vert[i] = spline.getValuefast(perc*vert[i-1],perc*vert[i+1])*10;
            vert[i] = spline.getValueSlow(p,perc*vert[i-1],perc*vert[i+1])*10;

            triangles.get(count + offset).y = vert[i];
            count++;
        }
        //25*40*40*6
       // System.out.println("NODE UPDATED7"+pNode*vert.length/8 );
       // System.out.println(count + "count");
       // printArray(p);
       // printArray(_splinePoints);
    }

    private void updateControlPoints(){
        for(int i = 0; i < p.length; i++)
            for(int j = 0; j < p.length; j++)
            {
                GameObject sp = _splinePoints.get(i*p.length +j);
                if(sp.equals(_draggingPoint) == false) {
                   swapYandZ(sp.getPosition());
                    sp.getPosition().y = 100000;
                    Vector3 intersection = new Vector3();
                    Ray ray = new Ray(sp.getPosition(), new Vector3(0, -1, 0));
                    boolean found = Intersector.intersectRayTriangles(ray, TerrainGenerator.triangleList, intersection);
                    //if(found == false)
                       // System.out.println(found);
               //     swapYandZ(intersection);

                    sp.getPosition().y = intersection.y;
                    swapYandZ(sp.getPosition());
                   // sp.updateHeight();

                }
                ;


                   // System.out.println("POINT CREATED AT: "+pos);
            }

    }
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
                printArray(p);
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
}
