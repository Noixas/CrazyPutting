package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;

public class Sphere implements Collidable {

    private Vector3 position;
    private float radius;


    public Sphere(Vector3 center,float radius){
        this.position = center;
        this.radius = radius;
    }

    public Vector3 getPosition(){
        return this.position;
    }

    public float getRadius(){
        return this.radius;
    }

    public void setPosition(Vector3 pos){
        this.position = new Vector3(pos);
    }
}
