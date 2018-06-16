package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Others.Velocity;

public class Sphere implements Collidable {

    private Vector3 position;
    private float radius;
    private final float MASS = 0.04593f;
    private boolean isStatic = false;

    private Velocity velocity = new Velocity();


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
        this.position = pos;
    }

    @Override
    public float getMass() {
        return this.MASS;
    }

    public void makeStatic(){
        this.isStatic = true;
    }

    public void makeNonStatic(){
        this.isStatic = false;
    }

    @Override
    public Velocity getVelocity() {
        return this.velocity;
    }

    @Override
    public void setVeloctiy(Velocity velocity) {
        this.velocity = velocity;
    }

    @Override
    public float getInversemass() {
        return 1.0f/this.MASS;
    }

    public boolean isStatic(){
        return this.isStatic;
    }
}
