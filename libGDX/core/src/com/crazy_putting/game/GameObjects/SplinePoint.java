package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.math.Vector3;

public class SplinePoint extends GameObject {
    private double _height;

    public SplinePoint(Vector3 pPosition){
        super(pPosition);
        _height = pPosition.z;
    }
    public double getSplineHeight(){
        return  _height;
    }
    public void setHeight(double pHeight){
        _position.z = (float)pHeight;
        _height = pHeight;
    }


}
