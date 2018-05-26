package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class SplinePoint extends GameObject {
    private double _height;
    private Vector2 _index;
    public SplinePoint(Vector3 pPosition, Vector2 pIndex, double pHeight){
        super(pPosition);
        _height = pHeight;
        _index = pIndex;
    }
    public SplinePoint(Vector3 pPosition){
        super(pPosition);
        _height = pPosition.z;
        _index = null;
    }
    public double getSplineHeight(){
        return  _height;
    }
    public void sstHeight(double pHight){
        _height = pHight;
    }
    public Vector2 getIndex(){
        return  _index;
    }


}
