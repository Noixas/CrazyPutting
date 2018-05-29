package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.math.Vector3;

public class Course {
    private int _ID;
    private String _name;
    private String _height;
    private float _friction; //friction coefficient
    private Vector3 _goalPosition;
    private float _goalRadius;
    private  Vector3 _startBall;
    private float _maxSpeed;
    private float[][] _splinePoints = new float[6][6];
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

    public void setGoalPosition(Vector3 pPos)
    {
        _goalPosition = pPos;
    }

    public Vector3 getGoalPosition() {
        return _goalPosition;
    }

    public void setGoalRadius(float pGoalRadius)
    {
        _goalRadius = pGoalRadius;
    }

    public float getGoalRadius() {
        return _goalRadius;
    }

    public void setBallStartPos(Vector3 pPos)
    {
        _startBall = pPos;
    }

    public Vector3 getStartBall() {
        return _startBall;
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
    public float getMaxSpeed() {
        return _maxSpeed;
    }
    @Override
    public String toString()
    {
        String out = "";
        out +=("\nCOURSE" + "");
        out +=( "\nID: ") + getID();//+  (getAmountCourses() + 1));//Set the next course ID
        out +=("\nName: ") + getName();
        out +=("\nHeight: " ) + getHeight();
        out += ("\nFriction: ") + getFriction();
        out += ("\nGoal Pos: ") + getGoalPosition();
        out +=("\nGoal Radius: ") + getGoalRadius();
        out +=( "\nBall Start Pos: ") + getStartBall();
        out += ("\nMax Speed: ") + getMaxSpeed();
        out += ("\nSpline Points: ") + toStringSplinePoints();
        return out;
    }
}