package com.crazy_putting.game.Others;

public class Velocity {
    public float Vx;
    public float Vy;
    public float speed;
    public float angle;

    public Velocity(){
        setAngle(0);
        setSpeed(0);
    }

    public Velocity(float speed, float angle){
        setAngle(angle);
        setSpeed(speed);
    }


    //getters and setters for speed components
    public void setSpeed(float speed){
        this.speed = speed;
        this.Vx = (float) (speed * Math.cos(Math.toRadians(angle)));
        this.Vy = (float) (speed * Math.sin(Math.toRadians(angle)));
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



}
