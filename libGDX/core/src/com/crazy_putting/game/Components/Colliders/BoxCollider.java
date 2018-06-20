package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;

public class BoxCollider extends ColliderComponent{

    private Vector3 halfSizes;

    public BoxCollider(Vector3 pos, Vector3 dim){
        this.position = pos;
        this.dimensions = new Vector3(dim);
        this.halfSizes = new Vector3(dim).scl(0.5f);
        isStatic = true;
    }

    public Vector3 getDimensions(){
        return this.dimensions;
    }



    public Vector3 getHalfSizes(){
        return this.halfSizes;
    }
    public boolean containsPoint(Vector3 pPoint){
        if(getPosition().x-halfSizes.x <= pPoint.x && getPosition().x + halfSizes.x >= pPoint.x &&
                getPosition().y-halfSizes.y <= pPoint.y && getPosition().y + halfSizes.y >= pPoint.y &&
                getPosition().z-halfSizes.z <= pPoint.z && getPosition().z + halfSizes.z >= pPoint.z)return true;
        else return  false;
    }
}
