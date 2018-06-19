package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;

public class BoxCollider extends ColliderComponent{

    private Vector3 dimensions;
    private Vector3 halfSizes;


    public BoxCollider(Vector3 pos, Vector3 dim){
        this.position = pos;
        this.dimensions = dim;
        this.halfSizes = dim.scl(0.5f);
        isStatic = true;
    }

    public Vector3 getDimensions(){
        return this.dimensions;
    }



    public Vector3 getHalfSizes(){
        return this.halfSizes;
    }

}
