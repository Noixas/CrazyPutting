package com.crazy_putting.game.Graphics3D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.CachedBicubicInterpolator;
import com.crazy_putting.game.GameLogic.CourseManager;

import java.util.ArrayList;
import java.util.List;

public class TerrainGenerator {

    private static int _testWidth = 1080;
    private static int _testHeight = 720;
    private static int _triangleDistance = 20;
    public static  int MeshPartBuilder;
    public static com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo zeroPos;
    public static String testMeshCode;
    public static String testPart;
    private  static boolean testSet = false;
    public static int countTriangles = 0;
    private static boolean triangleBool = false;
    public static List<Vector3> triangleList = new ArrayList<Vector3>();
    public static Vector3[] triangles = new Vector3[19200];
    private static double[][] points = new double[4][4];
    private static  CachedBicubicInterpolator spline;
    static double[][] p= {{527,33,23,14}, {100,20,30,40}, {16,60,30,66}, {26,57,75,92}};
    public static Model generateModelTerrain()
    {
        int totalMeshLength = 200;
        int startPosX = -totalMeshLength/2; //Get half and set it negative so the middle of the mesh is at (0,0)
        int startPosY = -totalMeshLength/2;
        int verticesPerSide = 40; //Amount of vertices per side of mesh part
        float scaleVertex = 10;  //scale amount
        for (int i = 0; i<4;i++)
        {
            for (int j = 0; j<4;j++) {
                points[i][j] = 4;
            }
        }
        points[2][2] =5;
        points[2][1] =5;
        points[1][2] =5;
        points[1][1] =5;
        points[0][1] =5;
        points[3][1] =5;
        points = p;

        spline = new CachedBicubicInterpolator();
        spline.updateCoefficients(points);

       // System.out.println(CourseManager.calculateHeight(1,100)+ "Course amount");
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        int amountNodes = totalMeshLength/verticesPerSide;
        Material m = new Material();
        //Doouble loop for 2d of the plane
        int nodeAmount = 0;
        for(int i = 0; i<amountNodes; i++)
            for(int j = 0; j < amountNodes; j++)
            {
                Node node = modelBuilder.node();
                String code ="i "+i+" j "+j;
                node.id = "n "+nodeAmount+" "+code;
                if(testSet == false) {
                    testMeshCode = "n " + nodeAmount + " " + code;
                    testPart = "part " + code;
                    testSet = true;
                }
                nodeAmount++;
                MeshPartBuilder meshBuilder = modelBuilder.part("part "+code , GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal  | VertexAttributes.Usage.ColorPacked,m);
                createMesh(meshBuilder, startPosX+verticesPerSide*i,startPosY+verticesPerSide*j, verticesPerSide, scaleVertex);
            }


      /*  Node node2 = modelBuilder.node();
        node2.id = "node2";
       // node2.translation.set(40,0,40);
        MeshPartBuilder meshBuilder2 = modelBuilder.part("part2", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal  | VertexAttributes.Usage.ColorPacked, new Material());
        createMesh(meshBuilder2, -20,20,lenght);
        */
        Model terrain = modelBuilder.end();

        return terrain;
    }
   /* private static Mesh generateMesh()
    {
        for(int i = 0; i < _testWidth; i++)
        {

        }
    }*/
   private static Vector3 calculateNormals(Vector3 pa, Vector3 pb, Vector3 pc)
   {
       Vector3 a = new Vector3(pa);
       Vector3 b = new Vector3(pb);
       Vector3 c = new Vector3(pc);
       Vector3 V1= (b.sub(a));
       Vector3 V2 = (c.sub(a));
       Vector3 surfaceNormal = V1.crs(V2);
       //surfaceNormal.nor();
       return surfaceNormal;

   }
   private static void createMesh(MeshPartBuilder pBuilder, int startX, int startY, int length, float scaleVertexSize)
   {
       float perc = 1.0f*0.0005f;
       float scaleAmount = scaleVertexSize;
       for(int i = startY; i < startY+length; i++) {
           for (int j = startX; j < startX +length; j++) {
               float h1 = CourseManager.calculateHeight(j*(scaleAmount),scaleAmount*i);
               float h2 = CourseManager.calculateHeight(j*(scaleAmount),scaleAmount + scaleAmount*i);
               float h3 = CourseManager.calculateHeight(scaleAmount + j *scaleAmount,scaleAmount*i);
               float h4 = CourseManager.calculateHeight(scaleAmount + j * scaleAmount,scaleAmount + scaleAmount*i);
               if(false)
               {
                   h1 = 0;
                   h2 = 0;
                   h3 = 0;
                   h4 = 0;
                    h1 = spline.getValueSlow(p,perc*j*(scaleAmount),perc*scaleAmount*i);
                    h2 = spline.getValueSlow(p,perc*j*(scaleAmount),perc*(scaleAmount + scaleAmount*i));
                    h3 = spline.getValueSlow(p,perc*(scaleAmount + j *scaleAmount),perc*scaleAmount*i);
                    h4 = spline.getValueSlow(p,perc*(scaleAmount + j * scaleAmount),perc*(scaleAmount + scaleAmount*i));
                   System.out.println(h1);
               }

               MeshPartBuilder.VertexInfo v1;
             //  if(startX >-10 && startX <10 && startY>-10 && startY < 10) {
              //     zeroPos = new MeshPartBuilder.VertexInfo().setPos(j * (scaleAmount), h1, (scaleAmount) * i).setNor(0, 1, 0).setCol(Color.RED).setUV(0.5f, 0.0f);
              //     v1 = zeroPos;
              // }
              // else
               float hScale = 1;
               Vector3 posV1 = new Vector3(j*(scaleAmount), h1*hScale, (scaleAmount) * i);
               Vector3 posV2 = new Vector3(j*(scaleAmount), h2*hScale, scaleAmount + i * (scaleAmount));
               Vector3 posV3 = new Vector3(scaleAmount  +j*(scaleAmount), h3*hScale, scaleAmount * i);
               Vector3 posV4 = new Vector3(scaleAmount  +j* (scaleAmount), h4*hScale, scaleAmount + i * (scaleAmount));

             Vector3 normal1 =calculateNormals(posV1,posV2,posV3);
             Vector3 normal2 = calculateNormals(posV3,posV2,posV4);
                                          v1 = new MeshPartBuilder.VertexInfo().setPos(posV1).setNor(normal1).setCol(Color.GREEN).setUV(0.5f, 0.0f);
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

    triangleList.add(posV1);
    triangleList.add(posV2);
    triangleList.add(posV3);
    triangleList.add(posV3);
    triangleList.add(posV2);
    triangleList.add(posV4);

               pBuilder.triangle(v1, v2, v3);
               pBuilder.triangle(v6, v5, v4);
           }
       }
       System.out.println("TOTAL TRIANGLESSSSS" + countTriangles);
       triangleBool =true;
   }
   private static void checkUnderWaterVertex(com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo ver)
   {
       if(true&&ver.position.y <=0)
       {
           ver.position.y = 0;
           ver.setCol( Color.BLUE);
           ver.setNor(0,1,0);
       }
   }
}
