package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;

public class AABB  implements Collidable{
    private Vector3 center;
    private Vector3 dimensions;
    private Vector3 halfSizes;


    public AABB(Vector3 pos, Vector3 dim){
        this.center = pos;
        this.dimensions = dim;
        this.halfSizes = dim.scl(0.5f);
    }

    public Vector3 getDimensions(){
        return this.dimensions;
    }

    public Vector3 getCenter(){
        return this.center;
    }

    public Vector3 getHalfSizes(){
        return this.halfSizes;
    }
}
