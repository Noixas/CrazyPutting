package com.crazy_putting.game.Bot;

import com.badlogic.gdx.math.Vector3;

public class LinearFunction {
    private float a;
    private float b;

    public LinearFunction(float a, float b){
        this.a = a;
        this.b = b;
    }

    public float calculate(float x){
        return a*x+b;
    }

    public boolean intersects(Vector3 point){
        return point.x*a+b==point.y;
    }

    public float getA() {
        return a;
    }

    public float getB() {
        return b;
    }
}
