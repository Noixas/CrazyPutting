package com.crazy_putting.game.Others;

import com.badlogic.gdx.math.Vector3;

public class Velocity {
    public float Vx;
    public float Vy;
    public float speed;
    public float angle;

    public Velocity(){
        setAngle(0);
        setSpeed(0);

    }

    public void updateVelocityComponents(){
        this.Vx = (float) (speed * Math.cos(Math.toRadians(angle)));
        this.Vy = (float) (speed * Math.sin(Math.toRadians(angle)));

    }

    public Velocity(float speed, float angle){
        setAngle(angle);
        setSpeed(speed);
        updateVelocityComponents();
//        System.out.println("Speed "+speed+" angle "+angle);
//        System.out.println("Initial Vel: " + Vx + " " + Vy);
    }


    //getters and setters for speed components
    public void setSpeed(float speed){
        this.speed = speed;
        updateVelocityComponents();
    }

    public float getSpeed(){
        return this.speed;
    }


    public float getAngle(){
        return this.angle;
    }

    public void setAngle(float angle){
        this.angle = angle;
    }

    public float getActualAngle(){
        return (float) Math.toDegrees(Math.acos(Vx/getActualSpeed()));
    }

    private float getActualSpeed(){
        return (float) Math.sqrt(Math.pow(Vx,2) + Math.pow(Vy,2));
    }


    public void add(Velocity velocity){
        this.Vx += velocity.Vx;
        this.Vy += velocity.Vy;
    }

    public float multiply(Vector3 vector){
        float xAxis = this.Vx * vector.x;
        float yAxis = this.Vy*vector.y;
        return xAxis + yAxis;
    }

    public void sub(Velocity velocity){
        this.Vx -= velocity.Vx;
        this.Vy -= velocity.Vy;
    }

    public String toString(){
        return "[" + this.Vx + ";" + this.Vy + "]";
    }

}
