package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.Splines.SplineInfo;

import java.util.ArrayList;
import java.util.List;

public class SplinePoint extends GameObject {
    private double _height;
    private Vector2 _index;
    private List<SplineInfo> _splineOwners = new ArrayList<SplineInfo>();

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
    public void addSplineInfo(SplineInfo spline){
        if(_splineOwners.contains(spline) == false)
        _splineOwners.add(spline);
    }

    public List<SplineInfo> getSplineOwners() {
        return _splineOwners;
    }
    public double getSplineHeight(){
        return  _height;
    }
    public void sstHeight(double pHight){
        _position.z = (float)pHight;
        _height = pHight;
    }
    public Vector2 getIndex(){
        return  _index;
    }


}
