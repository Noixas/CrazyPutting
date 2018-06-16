package com.crazy_putting.game.Components.Colliders;

import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Others.Velocity;

public class AABB  implements Collidable{
    private Vector3 center;
    private Vector3 dimensions;
    private Vector3 halfSizes;

    private final float MASS = 100;

    private boolean isStatic = false;

    private Velocity velocity = new Velocity();


    public AABB(Vector3 pos, Vector3 dim){
        this.center = pos;
        this.dimensions = dim;
        this.halfSizes = dim.scl(0.5f);
    }

    public Vector3 getDimensions(){
        return this.dimensions;
    }

    public Vector3 getPosition(){
        return this.center;
    }

    @Override
    public void setPosition(Vector3 position) {
        this.center = position;
    }

    public Vector3 getHalfSizes(){
        return this.halfSizes;
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
