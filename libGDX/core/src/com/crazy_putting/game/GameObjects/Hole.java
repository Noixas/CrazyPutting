package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.math.Vector2;

public class Hole extends GameObject {
    private Vector2 position;
    private int radius;

    public Hole(int radius){
        this.radius = radius;
        position = new Vector2();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setPositionX(float x){
        position.x = x;
    }

    public void setPositionY(float y){
        position.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
