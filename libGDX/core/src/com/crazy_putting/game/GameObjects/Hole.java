package com.crazy_putting.game.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.crazy_putting.game.Others.Velocity;

public class Hole extends GameObject {
    private Vector2 position;
    private int radius;

    public Hole(int radius){
        this.radius = radius;
        position = new Vector2();
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

    public Vector2 getPosition() {
        return this.position;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Texture getTexture() {
        return null;
    }


    public Velocity getVelocity() {
        return null;
    }

    public float getMass() {
        return 0;
    }

    public void setVelocity(float speed, float angle) {
        return;
    }
    public void setSpeed(float speed){
        return;
    }

    @Override
    public boolean inTheWater() {
        return false;
    }

    @Override
    public Vector2 getPreviousPosition() {
        return null;
    }

    @Override
    public float getSpeed() {
        return 0;
    }

}
