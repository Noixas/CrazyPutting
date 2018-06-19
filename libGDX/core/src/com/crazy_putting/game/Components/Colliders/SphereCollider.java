package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Others.Velocity;

public class SphereCollider extends ColliderComponent {

    private float radius;

    public SphereCollider(Vector3 center, float radius){
        this.position = center;
        this.radius = radius;
    }
    public float getRadius(){
        return this.radius;
    }

}
