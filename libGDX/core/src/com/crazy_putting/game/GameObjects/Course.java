package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Components.Colliders.BoxCollider;
import com.crazy_putting.game.Components.Colliders.ColliderComponent;
import com.crazy_putting.game.Components.Colliders.SphereCollider;
import com.crazy_putting.game.Components.Graphics.BoxGraphics3DComponent;
import com.crazy_putting.game.Components.Graphics.SphereGraphics3DComponent;
import com.crazy_putting.game.Parser.ObstacleData;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private int _ID;
    private String _name;
    private String _height;
    private float _friction; //friction coefficient
    private Vector3[] _goalPosition;
    private float _goalRadius;
    private  Vector3[] _startBall;
    private float _maxSpeed;
    private float[][] _splinePoints = new float[6][6];
    private List<GameObject> _obstacles = new ArrayList<GameObject>();
    private List<ObstacleData> cacheDataList = new ArrayList<ObstacleData>();

    public Course(){
        _goalPosition = new Vector3[4];
        _startBall = new Vector3[4];
        for(int i=0; i< 4 ; i++){
            _goalPosition[i] = new Vector3();
            _startBall[i] = new Vector3();
        }
    }
    public void setID(int pID)
    {
        _ID = pID;
    }
    public int getID()
    {
        return _ID;
    }
    public void setName(String pName)
    {
        _name = pName;
    }
    public String getName() {
        return _name;
    }
    public void setHeight(String pFormula)
    {
        _height = pFormula;
    }

    public String getHeight() {
        return _height;
    }

    public void setFriction(float pFriction)
    {
        _friction = pFriction;
    }

    public float getFriction() {
        return _friction;
    }

    public void setGoalPosition(Vector3 pPos, int pIndex)
    {
        if(pIndex>=_goalPosition.length)throw new ArrayIndexOutOfBoundsException();
        _goalPosition[pIndex] = pPos;
    }

    public Vector3 getGoalPosition( int pIndex) {
        if(pIndex>=_goalPosition.length)throw new ArrayIndexOutOfBoundsException();
        return _goalPosition[pIndex];
    }

    public void setGoalRadius(float pGoalRadius)
    {
        _goalRadius = pGoalRadius;
    }

    public float getGoalRadius() {
        return _goalRadius;
    }

    public void setBallStartPos(Vector3 pPos, int pIndex)
    {
        if(pIndex>=_goalPosition.length)throw new ArrayIndexOutOfBoundsException();
        _startBall[pIndex] = pPos;
    }

    public Vector3 getStartBall( int pIndex) {
        if(pIndex>=_startBall.length)throw new ArrayIndexOutOfBoundsException();
        return new Vector3(_startBall[pIndex]);
    }

    public void setMaxSpeed(float pMax)
    {
        _maxSpeed = pMax;
    }
    public float[][] getSplinePoints(){
        return _splinePoints;
    }
    public void setSplinePoints(float[][] points){
        _splinePoints = points;
    }
    public void initObstacles(){
createObstacle();
    }
    public String toStringSplinePoints()    {
        String out = ""+_splinePoints.length +" "+ _splinePoints[0].length+" ";
        for(int i = 0; i<_splinePoints.length; i++)
        {for(int j = 0; j<_splinePoints[0].length; j++)
        {
            out += _splinePoints[i][j]+"  ";
        }
            //out+="\n";
        }
        return out;
    }
    public String toStringSplinePointsMatrix()    {
        String out = "";
        for(int i = 0; i<_splinePoints.length; i++)
        {for(int j = 0; j<_splinePoints[0].length; j++)
        {
            out += _splinePoints[i][j]+"  ";
        }
            out+="\n";
        }
        return out;
    }
    public void addObstacleToList(GameObject pObstacle){
        _obstacles.add(pObstacle);
    }
    public boolean checkObstaclesAt(Vector3 pPosition){
        for (GameObject obstacle:_obstacles) {
           ColliderComponent colliderComponent = obstacle.getColliderComponent();
           if(colliderComponent instanceof SphereCollider){
               SphereCollider sphere = (SphereCollider)colliderComponent;
                    if(sphere.containsPoint(pPosition))return true;
           }else if(colliderComponent instanceof BoxCollider){
               BoxCollider box = (BoxCollider)colliderComponent;
                if( box.containsPoint(pPosition)) return true;
           }
        }
        return false;
    }
    public float getMaxSpeed() {
        return _maxSpeed;
    }
    @Override
    public String toString()
    {
        String out = "";
        out += ("\nCOURSE" + "");
        out += ("\nID: ") + getID();//+  (getAmountCourses() + 1));//Set the next course ID
        out += ("\nName: ") + getName();
        out += ("\nHeight: " ) + getHeight();
        out += ("\nFriction: ") + getFriction();
        out += ("\nGoal Pos: ") + getGoalPosition(0);
        out += ("\nGoal Pos: ") + getGoalPosition(1);
        out += ("\nGoal Pos: ") + getGoalPosition(2);
        out += ("\nGoal Pos: ") + getGoalPosition(3);
        out += ("\nGoal Radius: ") + getGoalRadius();
        out += ("\nBall Start Pos: ") + getStartBall(0);
        out += ("\nBall Start Pos: ") + getStartBall(1);
        out += ("\nBall Start Pos: ") + getStartBall(2);
        out += ("\nBall Start Pos: ") + getStartBall(3);
        out += ("\nMax Speed: ") + getMaxSpeed();
        out += ("\nSpline Points: ") + toStringSplinePoints();
        out += getObstaclesString();
        return out;
    }
    private String getObstaclesString(){
        String out = "";
        for(GameObject obstacle: _obstacles){
            ColliderComponent colliderComponent = obstacle.getColliderComponent();
            if(colliderComponent instanceof SphereCollider){
                SphereCollider sphere = (SphereCollider)colliderComponent;
                out += "\nCollider type 1";
                out += "\nPosition: "+ obstacle.getPosition();
                out += "\nDimensions: "+ sphere.getDimensions();
            }else if(colliderComponent instanceof BoxCollider){
                BoxCollider box = (BoxCollider)colliderComponent;
                out += "\nCollider type 2";
                out += "\nPosition: "+ obstacle.getPosition();
                out += "\nDimensions: "+ box.getDimensions();
            }
        }
        return out;
    }
    public List<String> getObstaclesStringList(){
        List<String> out = new ArrayList<String>();
      //  String out = "";
        out.add("\nObstacles: "+ _obstacles.size());
        for(GameObject obstacle: _obstacles){
            ColliderComponent colliderComponent = obstacle.getColliderComponent();
            if(colliderComponent instanceof SphereCollider){
                SphereCollider sphere = (SphereCollider)colliderComponent;
                out.add("\nCollider type: 1");
                out.add("\nPosition: "+ obstacle.getPosition().x +" " + obstacle.getPosition().y +" " + obstacle.getPosition().z +" " );
                out.add("\nDimensions: "+ sphere.getDimensions().x+" " + sphere.getDimensions().y +" " + sphere.getDimensions().z +" " );
            }else if(colliderComponent instanceof BoxCollider){
                BoxCollider box = (BoxCollider)colliderComponent;
                out.add("\nCollider type: 2");
                out.add("\nPosition: "+ obstacle.getPosition().x +" " + obstacle.getPosition().y +" " + obstacle.getPosition().z +" " );
                System.out.println("Box added with dimensions: x " + box.getDimensions().x + " y " + box.getDimensions() + " z " + box.getDimensions());
                out.add("\nDimensions: "+box.getDimensions().x+" " + box.getDimensions().y +" " + box.getDimensions().z +" " );
            }
        }
        return out;
    }
    public void addObstacle(int line, String value){
        switch (line){
            case 0:
                value = value.replace("Collider type: ","");
                cacheDataList.add(new ObstacleData());
                cacheDataList.get(cacheDataList.size()-1).type = Integer.parseInt(value);
                break;
            case 1:
                value = value.replace("Position: ","");
                String[] pos = value.trim().split("\\s+");
                cacheDataList.get(cacheDataList.size()-1).position = new Vector3(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]),Float.parseFloat(pos[2]));
               break;
            case 2:
                value = value.replace("Dimensions: ","");
                String[] dim = value.trim().split("\\s+");
                cacheDataList.get(cacheDataList.size()-1).dimensions = new Vector3(Float.parseFloat(dim[0]), Float.parseFloat(dim[1]),Float.parseFloat(dim[2]));
                //createObstacle();
                break;
        }
    }
    private void createObstacle(){
        for(ObstacleData cacheData:  cacheDataList){
        GameObject obj = new GameObject(cacheData.position);
        switch (cacheData.type){
            case 1:
                SphereCollider sphere = new SphereCollider(cacheData.position,cacheData.dimensions.x);
                obj.addColliderComponent(sphere);
                SphereGraphics3DComponent graphSphere = new SphereGraphics3DComponent(cacheData.dimensions.x, Color.DARK_GRAY);
                obj.addGraphicComponent(graphSphere);
                break;
            case 2:
                BoxCollider box = new BoxCollider(cacheData.position, cacheData.dimensions);
                obj.addColliderComponent(box);
                BoxGraphics3DComponent boxGraph = new BoxGraphics3DComponent(cacheData.dimensions,Color.DARK_GRAY);
                obj.addGraphicComponent(boxGraph);
                break;
        }
        addObstacleToList(obj);
    }
    }
    public void deleteObstacle(GameObject obj){
        _obstacles.remove(obj);
    }
    public List<GameObject> getObstaclesList() {
        return _obstacles;
    }
}