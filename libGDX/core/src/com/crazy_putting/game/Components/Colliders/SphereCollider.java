package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;

public class SphereCollider extends ColliderComponent {

    private float radius;

    public SphereCollider(Vector3 center, float radius){
        this.position = center;
        this.radius = radius;
        dimensions = new Vector3(radius,radius,radius);
    }
    public float getRadius(){
        return this.radius;
    }

    @Override
    public boolean containsPoint(Vector3 pPoint) {
        if(getPosition().dst(pPoint) <= radius)return true;
        else return false;
    }
}
