package com.crazy_putting.game.Bot;

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
}
