package com.crazy_putting.game.Others;

public class Velocity {
    // actually from speed and angle you can derive x and y, but I think it might be easier to still have all
    // of these variables
    public float x;
    public float y;
    public float speed;
    public float angle;

    public Velocity(){

    }

    public Velocity(float x, float y, float speed, float angle){
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.angle = angle;
    }
}
