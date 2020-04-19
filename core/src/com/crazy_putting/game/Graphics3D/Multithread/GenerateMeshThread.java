package com.crazy_putting.game.Graphics3D.Multithread;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameLogic.Splines.BiCubicSpline;
import com.crazy_putting.game.GameLogic.Splines.SplineInfo;

import java.util.ArrayList;
import java.util.List;

public class GenerateMeshThread extends Thread {
    private MeshPartBuilder _meshBuilder;
    int _startX, _startY, _length;
    float _scaleVertexSize;
    boolean _splines;
    SplineInfo _spline;
    BiCubicSpline _biCubicSpline;
    Thread t;
    private List<Vector3> _triangleList = new ArrayList<Vector3>();
    public GenerateMeshThread(MeshPartBuilder pMB, int startX, int startY, int length, float scaleVertexSize, boolean pSplines, SplineInfo spline, BiCubicSpline pBiCubicSpline){
        _meshBuilder = pMB;
        _startX = startX;
        _startY = startY;
        _length = length;
        _scaleVertexSize = scaleVertexSize;
        _splines = pSplines;
        _spline = spline;
        _biCubicSpline = pBiCubicSpline;
    }
    @Override
    public void run(){
        createMesh(_meshBuilder, _startX,_startY, _length, _scaleVertexSize, _splines,_spline);
        System.out.println("Thread active");
    }

    public void start () {
        System.out.println("Starting "  );
        if (t == null) {
            t = new Thread (this, "Thread");
            t.start ();
        }
    }
    public List<Vector3> getTriangleList() {
        return _triangleList;
    }

    public MeshPartBuilder getMeshBuilder() {
        return _meshBuilder;
    }

    private void createMesh(MeshPartBuilder pBuilder, int startX, int startY, int length, float scaleVertexSize, boolean pSplines, SplineInfo spline){
        float scaleAmount = scaleVertexSize;
        for(int i = startY; i < startY+length; i++) {
            for (int j = startX; j < startX +length; j++) {

                float h1 = 0;
                float h2 = 0;
                float h3 = 0;
                float h4 = 0;
                if(pSplines == false) {
                    h1 = CourseManager.calculateHeight(j * (scaleAmount), scaleAmount * i);
                    h2 = CourseManager.calculateHeight(j * (scaleAmount), scaleAmount + scaleAmount * i);
                    h3 = CourseManager.calculateHeight(scaleAmount + j * scaleAmount, scaleAmount * i);
                    h4 = CourseManager.calculateHeight(scaleAmount + j * scaleAmount, scaleAmount + scaleAmount * i);
                }
                else{
                    h1 = _biCubicSpline.getHeightAt(new Vector2(j*(scaleAmount),scaleAmount*i), spline);
                    h2 = _biCubicSpline.getHeightAt(new Vector2(j*(scaleAmount),(scaleAmount + scaleAmount*i)),spline);
                    h3 = _biCubicSpline.getHeightAt(new Vector2((scaleAmount + j *scaleAmount),scaleAmount*i),spline);
                    h4 = _biCubicSpline.getHeightAt(new Vector2((scaleAmount + j * scaleAmount),(scaleAmount + scaleAmount*i)),spline);
                }
                float hScale = 1;
                Vector3 posV1 = new Vector3(j*(scaleAmount), h1*hScale, (scaleAmount) * i);
                Vector3 posV2 = new Vector3(j*(scaleAmount), h2*hScale, scaleAmount + i * (scaleAmount));
                Vector3 posV3 = new Vector3(scaleAmount  +j*(scaleAmount), h3*hScale, scaleAmount * i);
                Vector3 posV4 = new Vector3(scaleAmount  +j* (scaleAmount), h4*hScale, scaleAmount + i * (scaleAmount));

                Vector3 normal1 =calculateNormals(posV1,posV2,posV3);
                Vector3 normal2 = calculateNormals(posV3,posV2,posV4);
                MeshPartBuilder.VertexInfo v1 = new MeshPartBuilder.VertexInfo().setPos(posV1).setNor(normal1).setCol(Color.GREEN).setUV(0.5f, 0.0f);
                MeshPartBuilder.VertexInfo v2 = new MeshPartBuilder.VertexInfo().setPos(posV2).setNor(normal1).setCol(Color.GREEN).setUV(0.5f, 0.0f);
                MeshPartBuilder.VertexInfo v3 = new MeshPartBuilder.VertexInfo().setPos(posV3).setNor(normal1).setCol(Color.GREEN).setUV(0.5f, 0.0f);
                MeshPartBuilder.VertexInfo v4 = new MeshPartBuilder.VertexInfo().setPos(posV4).setNor(normal2).setCol(Color.FOREST).setUV(0.5f, 0.0f);
                MeshPartBuilder.VertexInfo v5 = new MeshPartBuilder.VertexInfo().setPos((posV2)).setNor(normal2).setCol(Color.FOREST).setUV(0.5f, 0.0f);
                MeshPartBuilder.VertexInfo v6 = new MeshPartBuilder.VertexInfo().setPos((posV3)).setNor(normal2).setCol(Color.FOREST).setUV(0.5f, 0.0f);
                checkUnderWaterVertex(v1);
                checkUnderWaterVertex(v2);
                checkUnderWaterVertex(v3);
                checkUnderWaterVertex(v4);
                checkUnderWaterVertex(v5);
                checkUnderWaterVertex(v6);

                _triangleList.add(posV1);
                _triangleList.add(posV2);
                _triangleList.add(posV3);
                _triangleList.add(posV3);
                _triangleList.add(posV2);
                _triangleList.add(posV4);

                pBuilder.triangle(v1, v2, v3);
                pBuilder.triangle(v6, v5, v4);
            }
        }
    }
    private Vector3 calculateNormals(Vector3 pa, Vector3 pb, Vector3 pc)
    {
        Vector3 a = new Vector3(pa);
        Vector3 b = new Vector3(pb);
        Vector3 c = new Vector3(pc);
        Vector3 V1= (b.sub(a));
        Vector3 V2 = (c.sub(a));
        Vector3 surfaceNormal = V1.crs(V2);
        surfaceNormal.nor();
        return surfaceNormal;

    }

    private void checkUnderWaterVertex(com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo ver)
    {
        if(true&&ver.position.y <-1)
        {
            ver.position.y = 0;
            ver.setCol( Color.BLUE);
            ver.setNor(0,1,0);
        }
    }
}
