package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.math.Vector2;

public class Course {
    private int _ID;
    private String _name;
    private String _height;
    private float _friction; //friction coefficient
    private Vector2 _goalPosition;
    private float _goalRadius;
    private  Vector2 _startBall;
    private float _maxSpeed;
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

    public void setGoalPosition(Vector2 pPos)
    {
        _goalPosition = pPos;
    }

    public Vector2 getGoalPosition() {
        return _goalPosition;
    }

    public void setGoalRadius(float pGoalRadius)
    {
        _goalRadius = pGoalRadius;
    }

    public float getGoalRadius() {
        return _goalRadius;
    }

    public void setBallStartPos(Vector2 pPos)
    {
        _startBall = pPos;
    }

    public Vector2 getStartBall() {
        return _startBall;
    }

    public void setMaxSpeed(float pMax)
    {
        _maxSpeed = pMax;
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
        return out;
    }
}